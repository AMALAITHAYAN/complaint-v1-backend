package com.fotocapture.dms_backend.repository;

import com.fotocapture.dms_backend.entity.AccessGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessGroupRepository extends JpaRepository<AccessGroup, Long> {
    Optional<AccessGroup> findByNameIgnoreCase(String name);
    Page<AccessGroup> findByNameContainingIgnoreCase(String q, Pageable pageable);
}
