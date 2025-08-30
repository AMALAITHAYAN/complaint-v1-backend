package com.fotocapture.dms_backend.repository;

import com.fotocapture.dms_backend.entity.GroupBatchPermission;
import com.fotocapture.dms_backend.entity.AccessGroup;
import com.fotocapture.dms_backend.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupBatchPermissionRepository extends JpaRepository<GroupBatchPermission, Long> {
    List<GroupBatchPermission> findByGroupId(Long groupId);
    void deleteByGroupId(Long groupId);
    Optional<GroupBatchPermission> findByGroupAndBatch(AccessGroup group, Batch batch);
}
