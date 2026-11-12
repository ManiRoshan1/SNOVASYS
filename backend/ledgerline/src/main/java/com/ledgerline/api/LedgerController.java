package com.ledgerline.api;

import com.ledgerline.account.*;
import com.ledgerline.audit.*;
import com.ledgerline.ledger.*;
import com.ledgerline.movement.*;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController @RequestMapping("/api")
public class LedgerController {
    private final MoneyMovementService service; private final LedgerEntryRepository ledger; private final AuditRepository audit; private final AccountRepository accounts;
    public LedgerController(MoneyMovementService service, LedgerEntryRepository ledger, AuditRepository audit, AccountRepository accounts){this.service=service;this.ledger=ledger;this.audit=audit;this.accounts=accounts;}
    private UUID actor(Authentication a){return UUID.nameUUIDFromBytes(a.getName().getBytes(StandardCharsets.UTF_8));}
    @GetMapping("/health") public Map<String,String> health(){return Map.of("status","ok");}
    @GetMapping("/accounts") public List<Account> accounts(Authentication a){return service.accounts(actor(a));}
    @PostMapping("/accounts") public Account create(Authentication a){return service.create(actor(a));}
    public record AmountRequest(@NotNull @DecimalMin("0.01") BigDecimal amount){}
    public record TransferRequest(@NotNull UUID fromAccountId,@NotNull UUID toAccountId,@NotNull @DecimalMin("0.01") BigDecimal amount){}
    @PostMapping("/accounts/{id}/deposit") public ResponseEntity<Void> deposit(Authentication a,@PathVariable UUID id,@RequestHeader("Idempotency-Key") String key,@RequestBody AmountRequest r){service.deposit(actor(a),id,r.amount(),key);return ResponseEntity.ok().build();}
    @PostMapping("/transfers") public Map<String,UUID> transfer(Authentication a,@RequestHeader("Idempotency-Key") String key,@RequestBody TransferRequest r){return Map.of("transactionId",service.transfer(actor(a),r.fromAccountId(),r.toAccountId(),r.amount(),key));}
    @GetMapping("/accounts/{id}/ledger") public List<LedgerEntry> entries(Authentication a,@PathVariable UUID id){var account=accounts.findById(id).orElseThrow();if(!account.getOwnerId().equals(actor(a))&&!a.getAuthorities().stream().anyMatch(x->x.getAuthority().equals("ROLE_AUDITOR")||x.getAuthority().equals("ROLE_ADMIN")))throw new SecurityException("Account is not accessible");return ledger.findByAccountIdOrderByCreatedAtDesc(id);}
    @GetMapping("/audit") public List<AuditRecord> audits(Authentication a){if(a.getAuthorities().stream().noneMatch(x->x.getAuthority().equals("ROLE_AUDITOR")||x.getAuthority().equals("ROLE_ADMIN")))throw new SecurityException("Audit access denied");return audit.findTop100ByOrderByCreatedAtDesc();}
}
