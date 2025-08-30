package com.fotocapture.dms_backend.repository;

import com.fotocapture.dms_backend.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    List<UserGroup> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
