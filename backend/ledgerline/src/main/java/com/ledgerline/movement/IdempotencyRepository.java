package com.ledgerline.movement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface IdempotencyRepository extends JpaRepository<IdempotencyRecord,Long>{Optional<IdempotencyRecord> findByActorIdAndKey(UUID actorId,String key);}
