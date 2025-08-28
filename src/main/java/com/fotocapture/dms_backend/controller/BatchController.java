package com.fotocapture.dms_backend.controller;

import com.fotocapture.dms_backend.dto.*;
import com.fotocapture.dms_backend.service.BatchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService service;

    public BatchController(BatchService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BatchResponse> create(@Valid @RequestBody BatchCreateUpdateRequest req) {
        return ResponseEntity.status(201).body(service.create(req));
    }

    @GetMapping("/{id}")
    public BatchResponse get(@PathVariable Integer id) {
        return service.get(id);
    }

    @GetMapping
    public PageResponse<BatchListItem> list(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return service.list(q, page, size);
    }

    @PutMapping("/{id}")
    public BatchResponse update(@PathVariable Integer id, @Valid @RequestBody BatchCreateUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** returns available(active) doc types + selected for this batch */
    @GetMapping("/{id}/document-types")
    public BatchDocTypesResponse docTypes(@PathVariable Integer id) {
        return service.docTypesForBatch(id);
    }
}
