package com.fotocapture.dms_backend.repository;

import com.fotocapture.dms_backend.entity.BatchDocumentType;
import com.fotocapture.dms_backend.entity.BatchDocumentType.PK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchDocumentTypeRepository extends JpaRepository<BatchDocumentType, PK> {
    List<BatchDocumentType> findByBatchId(Integer batchId);
    void deleteByBatchId(Integer batchId);
}
