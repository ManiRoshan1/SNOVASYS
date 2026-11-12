package com.ledgerline.ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry,Long>{ List<LedgerEntry> findByAccountIdOrderByCreatedAtDesc(UUID accountId); }
