package com.fotocapture.dms_backend.controller;

import com.fotocapture.dms_backend.dto.*;
import com.fotocapture.dms_backend.service.UserAccessService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/admin/access/users")
public class AccessUserController {

    private final UserAccessService service;

    public AccessUserController(UserAccessService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> list(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(service.list(q, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    // Role-based user creation
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserCreateRequest req) {
        UserResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/api/admin/access/users/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UserUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }
}
