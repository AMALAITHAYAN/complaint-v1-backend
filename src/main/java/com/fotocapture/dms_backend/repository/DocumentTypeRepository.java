package com.fotocapture.dms_backend.repository;

import com.fotocapture.dms_backend.entity.DocumentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentTypeEntity, Integer> {

    List<DocumentTypeEntity> findByDepartmentId(Integer departmentId);

    boolean existsByDepartmentIdAndNameIgnoreCase(Integer departmentId, String name);

    List<DocumentTypeEntity> findByDepartmentIdAndStatus(Integer departmentId, String status);
    List<DocumentTypeEntity> findByStatusOrderByUpdatedAtDesc(String status);

}
