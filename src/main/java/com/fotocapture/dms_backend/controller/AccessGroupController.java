package com.fotocapture.dms_backend.controller;

import com.fotocapture.dms_backend.dto.*;
import com.fotocapture.dms_backend.entity.Batch;
import com.fotocapture.dms_backend.repository.BatchRepository;
import com.fotocapture.dms_backend.service.AccessGroupService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/access/groups")
public class AccessGroupController {

    private final AccessGroupService service;
    private final BatchRepository batchRepo;

    public AccessGroupController(AccessGroupService service, BatchRepository batchRepo) {
        this.service = service;
        this.batchRepo = batchRepo;
    }

    @GetMapping
    public ResponseEntity<Page<GroupResponse>> list(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(service.list(q, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping
    public ResponseEntity<GroupResponse> create(@RequestBody GroupCreateUpdateRequest req) {
        GroupResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/api/admin/access/groups/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponse> update(@PathVariable Long id,
                                                @RequestBody GroupCreateUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Helper endpoint for your UI to render the full batch list with names (for toggles table)
    @GetMapping("/_all-batches")
    public ResponseEntity<List<BatchPermissionDTO>> listAllBatchesAsPermissions() {
        List<BatchPermissionDTO> out = batchRepo.findAll().stream()
                .map(b -> new BatchPermissionDTO(b.getId(), b.getName(), false, false, false))
                .collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }
}
