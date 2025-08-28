package com.fotocapture.dms_backend.controller;

import com.fotocapture.dms_backend.dto.DocumentTypeUserResponse;
import com.fotocapture.dms_backend.entity.DocumentTypeEntity;
import com.fotocapture.dms_backend.service.DocumentTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/document-types")
public class UserDocumentTypeController {

    private final DocumentTypeService service;

    public UserDocumentTypeController(DocumentTypeService service) {
        this.service = service;
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<DocumentTypeUserResponse>> listActiveByDepartment(
            @PathVariable Integer departmentId
    ) {
        List<DocumentTypeUserResponse> out = service.listByDepartment(departmentId).stream()
                .filter(e -> "ACTIVE".equalsIgnoreCase(e.getStatus()))
                .map(service::toUserResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentTypeUserResponse> getForUser(@PathVariable Integer id) {
        DocumentTypeEntity e = service.getById(id);
        if (!"ACTIVE".equalsIgnoreCase(e.getStatus())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.toUserResponse(e));
    }

    /** Optional convenience for UI: preview resolved folder/file given sample metadata */
    @PostMapping("/{id}/preview")
    public ResponseEntity<Map<String, String>> preview(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> sampleMetadata
    ) {
        String folder = service.previewFolderPath(id, sampleMetadata);
        String file = service.previewFileName(id, sampleMetadata);
        Map<String, String> out = new HashMap<>();
        out.put("folderPath", folder);
        out.put("fileName", file);
        return ResponseEntity.ok(out);
    }

    /** List all ACTIVE department types globally (for Batch picker) */
    @GetMapping("/active")
    public List<DocumentTypeUserResponse> listAllActive() {
        return service.listAllActive();
    }
}
