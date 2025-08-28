// src/main/java/com/fotocapture/dms_backend/service/impl/DocumentTypeServiceImpl.java
package com.fotocapture.dms_backend.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fotocapture.dms_backend.dto.*;
import com.fotocapture.dms_backend.entity.DocumentTypeEntity;
import com.fotocapture.dms_backend.exception.BadRequestException;
import com.fotocapture.dms_backend.exception.ConflictException;
import com.fotocapture.dms_backend.exception.NotFoundException;
import com.fotocapture.dms_backend.repository.DocumentTypeRepository;
import com.fotocapture.dms_backend.service.DocumentTypeService;
import com.fotocapture.dms_backend.util.TemplateResolver;
import com.fotocapture.dms_backend.util.ValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private static final String ACTIVE = "ACTIVE";
    private static final String INACTIVE = "INACTIVE";

    private final DocumentTypeRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    public DocumentTypeServiceImpl(DocumentTypeRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public DocumentTypeEntity create(DocumentTypeCreateRequest req, String actor) {
        if (req.getDepartmentId() == null) throw new BadRequestException("departmentId is required");
        if (req.getName() == null || req.getName().trim().isEmpty()) throw new BadRequestException("name is required");

        if (repo.existsByDepartmentIdAndNameIgnoreCase(req.getDepartmentId(), req.getName()))
            throw new ConflictException("DocumentType with same name already exists in this department");

        ValidationUtil.validateIndexingFields(req.getIndexingFields());
        ValidationUtil.validateTemplates(req.getFolderTemplate(), req.getFileTemplate(), req.getIndexingFields());

        DocumentTypeEntity e = new DocumentTypeEntity(req.getDepartmentId(), req.getName());
        e.setAvgIndexTime(req.getAvgIndexTime());
        e.setAvgQualityTime(req.getAvgQualityTime());
        e.setIndexingField(writeJson(req.getIndexingFields()));
        e.setFolderFormat(req.getFolderTemplate());
        e.setFileFormat(req.getFileTemplate());
        e.setExportType(nullTo(req.getExportType(), "FILESYSTEM"));
        e.setExportFormat(nullTo(req.getExportFormat(), "PDF"));
        e.setColorFormat(nullTo(req.getColorFormat(), "GRAYSCALE"));
        e.setStatus(ACTIVE);
        e.setCreatedAt(LocalDateTime.now());
        e.setCreatedBy(actor);
        e.setUpdatedAt(LocalDateTime.now());
        e.setUpdatedBy(actor);
        return repo.save(e);
    }

    @Override
    @Transactional
    public DocumentTypeEntity update(Integer id, DocumentTypeUpdateRequest req, String actor) {
        DocumentTypeEntity e = repo.findById(id).orElseThrow(() -> new NotFoundException("DocumentType not found"));

        if (req.getName() != null && !req.getName().trim().isEmpty()) {
            if (!req.getName().equalsIgnoreCase(e.getName()) &&
                    repo.existsByDepartmentIdAndNameIgnoreCase(e.getDepartmentId(), req.getName())) {
                throw new ConflictException("Another DocumentType with same name exists in this department");
            }
            e.setName(req.getName());
        }

        if (req.getIndexingFields() != null) {
            ValidationUtil.validateIndexingFields(req.getIndexingFields());
            e.setIndexingField(writeJson(req.getIndexingFields()));
        }

        if (req.getFolderTemplate() != null || req.getFileTemplate() != null) {
            List<IndexingFieldDTO> fields = readFields(e.getIndexingField());
            String folder = req.getFolderTemplate() != null ? req.getFolderTemplate() : e.getFolderFormat();
            String file   = req.getFileTemplate() != null ? req.getFileTemplate() : e.getFileFormat();
            ValidationUtil.validateTemplates(folder, file, fields);
            e.setFolderFormat(folder);
            e.setFileFormat(file);
        }

        if (req.getAvgIndexTime() != null) e.setAvgIndexTime(req.getAvgIndexTime());
        if (req.getAvgQualityTime() != null) e.setAvgQualityTime(req.getAvgQualityTime());
        if (req.getExportType() != null) e.setExportType(req.getExportType());
        if (req.getExportFormat() != null) e.setExportFormat(req.getExportFormat());
        if (req.getColorFormat() != null) e.setColorFormat(req.getColorFormat());
        if (req.getStatus() != null) e.setStatus(req.getStatus());

        e.setUpdatedAt(LocalDateTime.now());
        e.setUpdatedBy(actor);
        return repo.save(e);
    }

    // --- Delete/Restore ---

    @Override
    @Transactional
    public void softDelete(Integer id, String actor) {
        DocumentTypeEntity e = repo.findById(id).orElseThrow(() -> new NotFoundException("DocumentType not found"));
        if (INACTIVE.equalsIgnoreCase(e.getStatus())) return;  // idempotent
        e.setStatus(INACTIVE);
        e.setUpdatedAt(LocalDateTime.now());
        e.setUpdatedBy(actor);
        repo.save(e);
    }

    @Override
    @Transactional
    public void restore(Integer id, String actor) {
        DocumentTypeEntity e = repo.findById(id).orElseThrow(() -> new NotFoundException("DocumentType not found"));
        if (ACTIVE.equalsIgnoreCase(e.getStatus())) return;
        e.setStatus(ACTIVE);
        e.setUpdatedAt(LocalDateTime.now());
        e.setUpdatedBy(actor);
        repo.save(e);
    }

    @Override
    @Transactional
    public void deleteHard(Integer id) {
        // Will throw EmptyResultDataAccessException if not found -> let it bubble or wrap
        repo.deleteById(id);
    }

    // --- Queries ---

    @Override
    @Transactional(readOnly = true)
    public DocumentTypeEntity getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("DocumentType not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentTypeEntity> listByDepartment(Integer departmentId) {
        // ACTIVE only for normal UI
        return repo.findByDepartmentIdAndStatus(departmentId, ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentTypeEntity> listDeletedByDepartment(Integer departmentId) {
        // INACTIVE only for restore screen
        return repo.findByDepartmentIdAndStatus(departmentId, INACTIVE);
    }

    // NEW: List all ACTIVE document types globally (for Batch picker)
    @Override
    @Transactional(readOnly = true)
    public List<DocumentTypeUserResponse> listAllActive() {
        return repo.findByStatusOrderByUpdatedAtDesc(ACTIVE)
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    // --- Projections ---

    @Override
    @Transactional(readOnly = true)
    public DocumentTypeAdminResponse toAdminResponse(DocumentTypeEntity e) {
        DocumentTypeAdminResponse r = new DocumentTypeAdminResponse();
        r.setId(e.getId());
        r.setDepartmentId(e.getDepartmentId());
        r.setName(e.getName());
        r.setAvgIndexTime(e.getAvgIndexTime());
        r.setAvgQualityTime(e.getAvgQualityTime());
        r.setIndexingFields(readFields(e.getIndexingField()));
        r.setFolderTemplate(e.getFolderFormat());
        r.setFileTemplate(e.getFileFormat());
        r.setExportType(e.getExportType());
        r.setExportFormat(e.getExportFormat());
        r.setColorFormat(e.getColorFormat());
        r.setStatus(e.getStatus());
        r.setCreatedAt(e.getCreatedAt());
        r.setCreatedBy(e.getCreatedBy());
        r.setUpdatedAt(e.getUpdatedAt());
        r.setUpdatedBy(e.getUpdatedBy());
        return r;
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentTypeUserResponse toUserResponse(DocumentTypeEntity e) {
        DocumentTypeUserResponse r = new DocumentTypeUserResponse();
        r.setId(e.getId());
        r.setName(e.getName());
        List<IndexingFieldDTO> all = readFields(e.getIndexingField());
        r.setFieldsVisibleToUser(ValidationUtil.filterVisible(all));
        r.setFolderTemplate(e.getFolderFormat());
        r.setFileTemplate(e.getFileFormat());
        r.setExportFormat(e.getExportFormat());
        r.setColorFormat(e.getColorFormat());
        return r;
    }

    // --- Preview ---

    @Override
    @Transactional(readOnly = true)
    public String previewFolderPath(Integer docTypeId, Map<String, Object> sampleMetadata) {
        DocumentTypeEntity e = getById(docTypeId);
        return TemplateResolver.resolve(e.getFolderFormat(), enrichBaseTokens(e, sampleMetadata));
    }

    @Override
    @Transactional(readOnly = true)
    public String previewFileName(Integer docTypeId, Map<String, Object> sampleMetadata) {
        DocumentTypeEntity e = getById(docTypeId);
        return TemplateResolver.resolve(e.getFileFormat(), enrichBaseTokens(e, sampleMetadata));
    }

    private Map<String, Object> enrichBaseTokens(DocumentTypeEntity e, Map<String, Object> meta) {
        Map<String, Object> map = meta == null ? new HashMap<>() : new HashMap<>(meta);
        map.putIfAbsent("Department", String.valueOf(e.getDepartmentId()));
        map.putIfAbsent("DocType", e.getName());
        return map;
    }

    // --- Helpers ---

    private String writeJson(Object obj) {
        try { return mapper.writeValueAsString(obj); }
        catch (Exception ex) { throw new BadRequestException("Failed to serialize JSON: " + ex.getMessage()); }
    }

    private List<IndexingFieldDTO> readFields(String json) {
        if (json == null || json.isEmpty()) return Collections.emptyList();
        try {
            return mapper.readValue(json, new TypeReference<List<IndexingFieldDTO>>() {});
        } catch (Exception ex) {
            throw new BadRequestException("Corrupted indexingField JSON: " + ex.getMessage());
        }
    }

    private static String nullTo(String v, String def) { return v == null ? def : v; }
}
