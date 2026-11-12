package com.ledgerline.audit;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="audit_records")
public class AuditRecord {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private UUID actorId;
 @Column(nullable=false) private String action;
 @Column(nullable=false,length=2000) private String details;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected AuditRecord(){}
 public AuditRecord(UUID actorId,String action,String details){this.actorId=actorId;this.action=action;this.details=details;}
 public Long getId(){return id;} public UUID getActorId(){return actorId;} public String getAction(){return action;} public String getDetails(){return details;} public Instant getCreatedAt(){return createdAt;}
}
