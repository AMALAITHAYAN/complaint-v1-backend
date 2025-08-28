package com.fotocapture.dms_backend.repository;

import com.fotocapture.dms_backend.entity.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<Batch, Integer> {
    Page<Batch> findByNameContainingIgnoreCase(String q, Pageable pageable);
}
