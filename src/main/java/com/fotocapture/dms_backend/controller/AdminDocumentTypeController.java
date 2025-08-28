// src/main/java/com/fotocapture/dms_backend/controller/AdminDocumentTypeController.java
package com.fotocapture.dms_backend.controller;

import com.fotocapture.dms_backend.dto.*;
import com.fotocapture.dms_backend.entity.DocumentTypeEntity;
import com.fotocapture.dms_backend.service.DocumentTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/document-types")
public class AdminDocumentTypeController {

    private final DocumentTypeService service;

    public AdminDocumentTypeController(DocumentTypeService service) {
        this.service = service;
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DocumentTypeAdminResponse> create(@RequestBody DocumentTypeCreateRequest req,
                                                            Principal principal) {
        String actor = principal != null ? principal.getName() : "system";
        DocumentTypeEntity e = service.create(req, actor);
        DocumentTypeAdminResponse body = service.toAdminResponse(e);
        return ResponseEntity.created(URI.create("/api/document-types/" + e.getId())).body(body);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DocumentTypeAdminResponse> update(@PathVariable Integer id,
                                                            @RequestBody DocumentTypeUpdateRequest req,
                                                            Principal principal) {
        String actor = principal != null ? principal.getName() : "system";
        DocumentTypeEntity e = service.update(id, req, actor);
        return ResponseEntity.ok(service.toAdminResponse(e));
    }

    // --- Soft delete (move to INACTIVE) ---
    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, Principal principal) {
        String actor = principal != null ? principal.getName() : "system";
        service.softDelete(id, actor);
        return ResponseEntity.noContent().build();
    }

    // --- Restore (INACTIVE -> ACTIVE) ---
    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id, Principal principal) {
        String actor = principal != null ? principal.getName() : "system";
        service.restore(id, actor);
        return ResponseEntity.noContent().build();
    }

    // --- Hard delete (purge) ---
    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> deleteHard(@PathVariable Integer id) {
        service.deleteHard(id);
        return ResponseEntity.noContent().build();
    }

    // --- Lists ---
    // ACTIVE only (normal UI)
    // @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<DocumentTypeAdminResponse>> listByDepartment(@PathVariable Integer departmentId) {
        List<DocumentTypeAdminResponse> out = service.listByDepartment(departmentId)
                .stream().map(service::toAdminResponse).collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }

    // INACTIVE only (restore screen)
    // @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/department/{departmentId}/deleted")
    public ResponseEntity<List<DocumentTypeAdminResponse>> listDeletedByDepartment(@PathVariable Integer departmentId) {
        List<DocumentTypeAdminResponse> out = service.listDeletedByDepartment(departmentId)
                .stream().map(service::toAdminResponse).collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }

    // @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentTypeAdminResponse> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.toAdminResponse(service.getById(id)));
    }
}
