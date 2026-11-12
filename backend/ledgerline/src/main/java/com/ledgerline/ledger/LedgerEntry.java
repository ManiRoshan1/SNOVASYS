package com.ledgerline.ledger;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="ledger_entries")
public class LedgerEntry {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private UUID transactionId;
 @Column(nullable=false) private UUID accountId;
 @Column(nullable=false,precision=19,scale=2) private BigDecimal amount;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected LedgerEntry(){}
 public LedgerEntry(UUID transactionId,UUID accountId,BigDecimal amount){this.transactionId=transactionId;this.accountId=accountId;this.amount=amount;}
 public Long getId(){return id;} public UUID getTransactionId(){return transactionId;} public UUID getAccountId(){return accountId;} public BigDecimal getAmount(){return amount;} public Instant getCreatedAt(){return createdAt;}
}
