package com.fotocapture.dms_backend.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fotocapture.dms_backend.dto.*;
import com.fotocapture.dms_backend.entity.Batch;
import com.fotocapture.dms_backend.entity.BatchDocumentType;
import com.fotocapture.dms_backend.entity.DocumentTypeEntity;
import com.fotocapture.dms_backend.exception.BadRequestException;
import com.fotocapture.dms_backend.exception.NotFoundException;
import com.fotocapture.dms_backend.repository.BatchDocumentTypeRepository;
import com.fotocapture.dms_backend.repository.BatchRepository;
import com.fotocapture.dms_backend.repository.DocumentTypeRepository;
import com.fotocapture.dms_backend.service.BatchService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepo;
    private final BatchDocumentTypeRepository linkRepo;
    private final DocumentTypeRepository docTypeRepo;
    private final ObjectMapper om = new ObjectMapper();

    public BatchServiceImpl(BatchRepository batchRepo,
                            BatchDocumentTypeRepository linkRepo,
                            DocumentTypeRepository docTypeRepo) {
        this.batchRepo = batchRepo;
        this.linkRepo = linkRepo;
        this.docTypeRepo = docTypeRepo;
    }

    @Override
    public BatchResponse create(BatchCreateUpdateRequest req) {
        validateWorkflow(req.getWorkflow());
        Batch b = mapToEntity(new Batch(), req);
        b = batchRepo.save(b);
        upsertLinks(b.getId(), req.getSelectedDocumentTypeIds());
        return toResponse(b, fetchSelected(b.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public BatchResponse get(Integer id) {
        Batch b = batchRepo.findById(id).orElseThrow(() -> new NotFoundException("Batch not found"));
        return toResponse(b, fetchSelected(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BatchListItem> list(String q, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), Math.min(100, size));
        Page<Batch> p = (q == null || q.isBlank())
                ? batchRepo.findAll(pageable)
                : batchRepo.findByNameContainingIgnoreCase(q.trim(), pageable);
        List<BatchListItem> items = p.getContent().stream()
                .map(b -> new BatchListItem(b.getId(), b.getName(), b.getDepartmentName()))
                .collect(Collectors.toList());
        return new PageResponse<>(items, p.getTotalElements(), p.getNumber() + 1, p.getSize());
    }

    @Override
    public BatchResponse update(Integer id, BatchCreateUpdateRequest req) {
        validateWorkflow(req.getWorkflow());
        Batch b = batchRepo.findById(id).orElseThrow(() -> new NotFoundException("Batch not found"));
        b = mapToEntity(b, req);
        b = batchRepo.save(b);
        upsertLinks(id, req.getSelectedDocumentTypeIds());
        return toResponse(b, fetchSelected(id));
    }

    @Override
    public void delete(Integer id) {
        if (!batchRepo.existsById(id)) throw new NotFoundException("Batch not found");
        linkRepo.deleteByBatchId(id);
        batchRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BatchDocTypesResponse docTypesForBatch(Integer batchId) {
        Batch b = batchRepo.findById(batchId).orElseThrow(() -> new NotFoundException("Batch not found"));

        // available = ALL ACTIVE document types (global)
        List<DocumentTypeEntity> active =
                docTypeRepo.findByStatusOrderByUpdatedAtDesc("ACTIVE");

        Set<Integer> selectedIds = fetchSelected(batchId).stream()
                .map(DocumentTypeEntity::getId).collect(Collectors.toSet());

        BatchDocTypesResponse resp = new BatchDocTypesResponse();
        resp.setAvailable(active.stream()
                .map(dt -> new BatchDocTypesResponse.DocTypeBrief(dt.getId(), dt.getName()))
                .collect(Collectors.toList()));
        resp.setSelected(new ArrayList<>(selectedIds));
        return resp;
    }

    /* helpers */

    private void validateWorkflow(List<String> wf) {
        if (wf == null) throw new BadRequestException("workflow is required");
        Set<String> allowed = Set.of("SCAN", "INDEX", "QUALITY", "EXPORT");
        if (!allowed.containsAll(wf)) {
            throw new BadRequestException("workflow must be subset of [SCAN, INDEX, QUALITY, EXPORT]");
        }
    }

    private Batch mapToEntity(Batch b, BatchCreateUpdateRequest req) {
        b.setDepartmentName(req.getDepartmentName());
        b.setName(req.getName());
        b.setNamingFormula(req.getNamingFormula());
        b.setExpectedScanTimeSec(req.getExpectedScanTimeSec());
        try {
            b.setWorkflowJson(om.writeValueAsString(req.getWorkflow()));
        } catch (Exception e) {
            throw new BadRequestException("Invalid workflow");
        }
        Batch.SeparationMethod method = Batch.SeparationMethod.valueOf(req.getSeparation().getMethod());
        b.setSeparationMethod(method);
        b.setSeparationInfo(req.getSeparation().getInfo());
        b.setQualityPercentage(req.getQualityPercentage());
        b.setAutoImportPath(req.getAutoImportPath());
        b.setAutoProcessImported(Boolean.TRUE.equals(req.getAutoProcessImported()));
        return b;
    }

    private void upsertLinks(Integer batchId, List<Integer> docTypeIds) {
        linkRepo.deleteByBatchId(batchId);
        if (docTypeIds != null && !docTypeIds.isEmpty()) {
            List<BatchDocumentType> links = docTypeIds.stream()
                    .map(id -> new BatchDocumentType(batchId, id))
                    .collect(Collectors.toList());
            linkRepo.saveAll(links);
        }
    }

    private List<DocumentTypeEntity> fetchSelected(Integer batchId) {
        List<Integer> ids = linkRepo.findByBatchId(batchId).stream()
                .map(BatchDocumentType::getDocumentTypeId).collect(Collectors.toList());
        if (ids.isEmpty()) return List.of();
        return docTypeRepo.findAllById(ids);
    }

    private BatchResponse toResponse(Batch b, List<DocumentTypeEntity> selected) {
        BatchResponse r = new BatchResponse();
        r.setId(b.getId());
        r.setDepartmentName(b.getDepartmentName());
        r.setName(b.getName());
        r.setNamingFormula(b.getNamingFormula());
        r.setExpectedScanTimeSec(b.getExpectedScanTimeSec());
        try {
            List<String> wf = om.readValue(
                    b.getWorkflowJson() == null ? "[]" : b.getWorkflowJson(),
                    new TypeReference<List<String>>() {});
            r.setWorkflow(wf);
        } catch (Exception e) {
            r.setWorkflow(List.of());
        }
        r.setSeparationMethod(b.getSeparationMethod().name());
        r.setSeparationInfo(b.getSeparationInfo());
        r.setQualityPercentage(b.getQualityPercentage());
        r.setAutoImportPath(b.getAutoImportPath());
        r.setAutoProcessImported(b.isAutoProcessImported());
        r.setSelectedDocumentTypes(
                selected.stream()
                        .map(dt -> new BatchResponse.DocTypeBrief(dt.getId(), dt.getName()))
                        .collect(Collectors.toList()));
        return r;
    }
}
