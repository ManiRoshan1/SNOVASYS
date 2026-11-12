package com.ledgerline.audit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AuditRepository extends JpaRepository<AuditRecord,Long>{List<AuditRecord> findTop100ByOrderByCreatedAtDesc();}
