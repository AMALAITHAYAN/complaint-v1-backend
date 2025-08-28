package com.fotocapture.dms_backend.service;

import com.fotocapture.dms_backend.dto.*;
import com.fotocapture.dms_backend.entity.DocumentTypeEntity;

import java.util.List;
import java.util.Map;

public interface DocumentTypeService {

    DocumentTypeEntity create(DocumentTypeCreateRequest req, String actor);

    DocumentTypeEntity update(Integer id, DocumentTypeUpdateRequest req, String actor);

    void softDelete(Integer id, String actor);      // move to INACTIVE
    void restore(Integer id, String actor);         // INACTIVE -> ACTIVE
    void deleteHard(Integer id);                    // physical delete

    DocumentTypeEntity getById(Integer id);

    List<DocumentTypeEntity> listByDepartment(Integer departmentId);
    List<DocumentTypeEntity> listDeletedByDepartment(Integer departmentId);

    // NEW: for Batch UI to load all active document types
    List<DocumentTypeUserResponse> listAllActive();

    // Projections for controllers:
    DocumentTypeAdminResponse toAdminResponse(DocumentTypeEntity e);

    DocumentTypeUserResponse toUserResponse(DocumentTypeEntity e);

    String previewFolderPath(Integer docTypeId, Map<String, Object> sampleMetadata);

    String previewFileName(Integer docTypeId, Map<String, Object> sampleMetadata);
}
