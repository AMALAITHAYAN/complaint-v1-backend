package com.fotocapture.dms_backend.service;

import com.fotocapture.dms_backend.dto.*;
import com.fotocapture.dms_backend.entity.*;
import com.fotocapture.dms_backend.exception.BadRequestException;
import com.fotocapture.dms_backend.exception.NotFoundException;
import com.fotocapture.dms_backend.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccessGroupService {

    private final AccessGroupRepository groupRepo;
    private final GroupBatchPermissionRepository gbpRepo;
    private final BatchRepository batchRepo;

    public AccessGroupService(AccessGroupRepository groupRepo,
                              GroupBatchPermissionRepository gbpRepo,
                              BatchRepository batchRepo) {
        this.groupRepo = groupRepo;
        this.gbpRepo = gbpRepo;
        this.batchRepo = batchRepo;
    }

    public Page<GroupResponse> list(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<AccessGroup> groups = (q == null || q.isBlank())
                ? groupRepo.findAll(pageable)
                : groupRepo.findByNameContainingIgnoreCase(q, pageable);

        return groups.map(g -> new GroupResponse(
                g.getId(),
                g.getName(),
                Collections.emptyList()  // details endpoint returns permissions
        ));
    }

    public GroupResponse get(Long id) {
        AccessGroup g = groupRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Group not found: " + id));

        List<GroupBatchPermission> perms = gbpRepo.findByGroupId(id);
        Map<Integer, String> batchNames = batchRepo.findAllById(
                perms.stream().map(p -> p.getBatch().getId()).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(Batch::getId, Batch::getName));

        List<BatchPermissionDTO> dto = new ArrayList<>();
        for (GroupBatchPermission p : perms) {
            Integer bid = p.getBatch().getId();
            dto.add(new BatchPermissionDTO(
                    bid,
                    batchNames.getOrDefault(bid, ""),
                    p.isCanScan(),
                    p.isCanIndex(),
                    p.isCanQuality()
            ));
        }
        return new GroupResponse(g.getId(), g.getName(), dto);
    }

    @Transactional
    public GroupResponse create(GroupCreateUpdateRequest req) {
        if (req.getName() == null || req.getName().isBlank()) {
            throw new BadRequestException("Group name is required");
        }
        groupRepo.findByNameIgnoreCase(req.getName()).ifPresent(x -> {
            throw new BadRequestException("Group name already exists");
        });
        AccessGroup g = new AccessGroup(req.getName());
        g = groupRepo.save(g);

        upsertPermissions(g, req.getBatchPermissions());

        return get(g.getId());
    }

    @Transactional
    public GroupResponse update(Long id, GroupCreateUpdateRequest req) {
        AccessGroup g = groupRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Group not found: " + id));

        if (req.getName() != null && !req.getName().isBlank()) {
            // if renaming, ensure uniqueness
            groupRepo.findByNameIgnoreCase(req.getName()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new BadRequestException("Another group already uses this name");
                }
            });
            g.setName(req.getName());
            groupRepo.save(g);
        }

        if (req.getBatchPermissions() != null) {
            // Replace entire permission set (this matches the UI where you toggle per batch)
            gbpRepo.deleteByGroupId(id);
            upsertPermissions(g, req.getBatchPermissions());
        }

        return get(id);
    }

    @Transactional
    public void delete(Long id) {
        if (!groupRepo.existsById(id)) {
            throw new NotFoundException("Group not found: " + id);
        }
        gbpRepo.deleteByGroupId(id);
        groupRepo.deleteById(id);
    }

    private void upsertPermissions(AccessGroup group, List<BatchPermissionDTO> incoming) {
        if (incoming == null || incoming.isEmpty()) return;

        Map<Integer, Batch> batchMap = batchRepo.findAllById(
                incoming.stream().map(BatchPermissionDTO::getBatchId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(Batch::getId, b -> b));

        for (BatchPermissionDTO dto : incoming) {
            Batch b = batchMap.get(dto.getBatchId());
            if (b == null) {
                throw new BadRequestException("Unknown batch id: " + dto.getBatchId());
            }
            GroupBatchPermission entity = new GroupBatchPermission(
                    group, b, dto.isScan(), dto.isIndex(), dto.isQuality()
            );
            gbpRepo.save(entity);
        }
    }
}
