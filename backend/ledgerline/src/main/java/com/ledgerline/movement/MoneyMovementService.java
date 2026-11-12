package com.ledgerline.movement;

import com.ledgerline.account.*;
import com.ledgerline.audit.*;
import com.ledgerline.ledger.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class MoneyMovementService {
    private final AccountRepository accounts;
    private final LedgerEntryRepository ledger;
    private final AuditRepository audit;
    private final IdempotencyRepository idempotency;
    public MoneyMovementService(AccountRepository accounts, LedgerEntryRepository ledger, AuditRepository audit, IdempotencyRepository idempotency) {
        this.accounts=accounts; this.ledger=ledger; this.audit=audit; this.idempotency=idempotency;
    }
    @Transactional
    public UUID transfer(UUID actor, UUID fromId, UUID toId, BigDecimal amount, String key) {
        if (fromId.equals(toId) || amount == null || amount.signum() <= 0 || amount.scale() > 2) throw new IllegalArgumentException("Invalid transfer");
        var old = idempotency.findByActorIdAndKey(actor, key);
        if (old.isPresent()) return old.get().getTransactionId();
        UUID first=fromId.toString().compareTo(toId.toString())<0?fromId:toId, second=first.equals(fromId)?toId:fromId;
        Account from=accounts.findByIdForUpdate(first).orElseThrow(), to=accounts.findByIdForUpdate(second).orElseThrow();
        if (!from.getOwnerId().equals(actor)) throw new SecurityException("Account is not owned by actor");
        if (from.getBalance().compareTo(amount)<0) throw new IllegalArgumentException("Insufficient funds");
        UUID tx=UUID.randomUUID(); from.debit(amount); to.credit(amount);
        ledger.save(new LedgerEntry(tx,fromId,amount.negate())); ledger.save(new LedgerEntry(tx,toId,amount));
        audit.save(new AuditRecord(actor,"TRANSFER",fromId+" -> "+toId+" amount="+amount+" transaction="+tx));
        idempotency.save(new IdempotencyRecord(actor,key,tx)); return tx;
    }
    @Transactional
    public Account create(UUID owner) { return accounts.save(new Account(owner)); }
    @Transactional
    public void deposit(UUID actor, UUID id, BigDecimal amount, String key) {
        if (amount==null || amount.signum()<=0 || amount.scale()>2) throw new IllegalArgumentException("Invalid amount");
        if (idempotency.findByActorIdAndKey(actor,key).isPresent()) return;
        Account a=accounts.findByIdForUpdate(id).orElseThrow();
        if (!a.getOwnerId().equals(actor)) throw new SecurityException("Account is not owned by actor");
        UUID tx=UUID.randomUUID(); a.credit(amount); ledger.save(new LedgerEntry(tx,id,amount)); audit.save(new AuditRecord(actor,"DEPOSIT",id+" amount="+amount)); idempotency.save(new IdempotencyRecord(actor,key,tx));
    }
    public List<Account> accounts(UUID actor) { return accounts.findByOwnerId(actor); }
}

