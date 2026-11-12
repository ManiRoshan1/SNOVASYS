package com.ledgerline.movement;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="idempotency_records",uniqueConstraints=@UniqueConstraint(columnNames={"actorId","key"}))
public class IdempotencyRecord {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private UUID actorId;
 @Column(name="key",nullable=false) private String key;
 @Column(nullable=false) private UUID transactionId;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected IdempotencyRecord(){}
 public IdempotencyRecord(UUID actorId,String key,UUID transactionId){this.actorId=actorId;this.key=key;this.transactionId=transactionId;}
 public UUID getTransactionId(){return transactionId;}
}
