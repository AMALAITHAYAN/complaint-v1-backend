package com.fotocapture.dms_backend.service;

import com.fotocapture.dms_backend.dto.UserCreateRequest;
import com.fotocapture.dms_backend.dto.UserResponse;
import com.fotocapture.dms_backend.dto.UserUpdateRequest;
import com.fotocapture.dms_backend.entity.AccessGroup;
import com.fotocapture.dms_backend.entity.Role;
import com.fotocapture.dms_backend.entity.User;
import com.fotocapture.dms_backend.entity.UserGroup;
import com.fotocapture.dms_backend.exception.BadRequestException;
import com.fotocapture.dms_backend.exception.NotFoundException;
import com.fotocapture.dms_backend.repository.AccessGroupRepository;
import com.fotocapture.dms_backend.repository.RoleRepository;
import com.fotocapture.dms_backend.repository.UserGroupRepository;
import com.fotocapture.dms_backend.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserAccessService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final AccessGroupRepository groupRepo;
    private final UserGroupRepository userGroupRepo;
    private final PasswordEncoder passwordEncoder;

    public UserAccessService(UserRepository userRepo,
                             RoleRepository roleRepo,
                             AccessGroupRepository groupRepo,
                             UserGroupRepository userGroupRepo,
                             PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.groupRepo = groupRepo;
        this.userGroupRepo = userGroupRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------------------------
    // Users - Query
    // ---------------------------
    public Page<UserResponse> list(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());

        // If you already have a repo method like findByUsernameContainingIgnoreCase(q, pageable),
        // you can use it here. To avoid tight coupling, we filter in-memory while preserving paging.
        Page<User> pageData = userRepo.findAll(PageRequest.of(0, Integer.MAX_VALUE, Sort.by("username").ascending()));
        List<User> filtered;
        if (q == null || q.isBlank()) {
            filtered = pageData.getContent();
        } else {
            String needle = q.trim().toLowerCase();
            filtered = pageData.getContent().stream()
                    .filter(u ->
                            (u.getUsername() != null && u.getUsername().toLowerCase().contains(needle)) ||
                                    (safeGetFullName(u) != null && safeGetFullName(u).toLowerCase().contains(needle))
                    )
                    .collect(Collectors.toList());
        }

        int from = Math.min(page * size, filtered.size());
        int to = Math.min(from + size, filtered.size());
        List<UserResponse> slice = filtered.subList(from, to).stream().map(this::toResponse).collect(Collectors.toList());
        return new PageImpl<>(slice, pageable, filtered.size());
    }

    public UserResponse get(Long id) {
        User u = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return toResponse(u);
    }

    // ---------------------------
    // Users - Create / Update
    // ---------------------------
    @Transactional
    public UserResponse create(UserCreateRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank()) {
            throw new BadRequestException("Username is required");
        }
        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        User u = new User();
        u.setUsername(req.getUsername());
        setFullNameIfPresent(u, req.getFullName());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        setDailyTargetIfPresent(u, req.getDailyTargetMinutes());

        // roles
        if (req.getRoles() != null && !req.getRoles().isEmpty()) {
            Set<Role> roles = req.getRoles().stream()
                    .map(name -> roleRepo.findByName(name)
                            .orElseThrow(() -> new BadRequestException("Unknown role: " + name)))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            u.setRoles(roles);
        }

        u = userRepo.save(u);

        // groups
        if (req.getGroupIds() != null && !req.getGroupIds().isEmpty()) {
            List<AccessGroup> groups = requireExistingGroups(req.getGroupIds());
            for (AccessGroup g : groups) {
                userGroupRepo.save(new UserGroup(u, g));
            }
        }

        return toResponse(u);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest req) {
        User u = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        if (req.getFullName() != null) {
            setFullNameIfPresent(u, req.getFullName());
        }
        if (req.getDailyTargetMinutes() != null) {
            setDailyTargetIfPresent(u, req.getDailyTargetMinutes());
        }

        if (req.getRoles() != null) {
            Set<Role> roles = req.getRoles().stream()
                    .map(name -> roleRepo.findByName(name)
                            .orElseThrow(() -> new BadRequestException("Unknown role: " + name)))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            u.setRoles(roles);
        }

        u = userRepo.save(u);

        if (req.getGroupIds() != null) {
            // reset → add requested
            userGroupRepo.deleteByUserId(id);
            if (!req.getGroupIds().isEmpty()) {
                List<AccessGroup> groups = requireExistingGroups(req.getGroupIds());
                for (AccessGroup g : groups) {
                    userGroupRepo.save(new UserGroup(u, g));
                }
            }
        }

        return toResponse(u);
    }

    // ---------------------------
    // Helpers
    // ---------------------------
    /** Validate all ids exist; otherwise throws a single BadRequest listing missing ids. */
    private List<AccessGroup> requireExistingGroups(Collection<Long> ids) {
        List<AccessGroup> found = groupRepo.findAllById(ids);
        Set<Long> foundIds = found.stream().map(AccessGroup::getId).collect(Collectors.toSet());
        List<Long> missing = ids.stream().filter(id -> !foundIds.contains(id)).sorted().collect(Collectors.toList());
        if (!missing.isEmpty()) {
            throw new BadRequestException("Unknown group id(s): " + missing);
        }
        // preserve caller order as much as possible
        Map<Long, AccessGroup> byId = found.stream().collect(Collectors.toMap(AccessGroup::getId, g -> g));
        return ids.stream().distinct().map(byId::get).collect(Collectors.toList());
    }

    private String safeGetFullName(User u) {
        try {
            Method m = u.getClass().getMethod("getFullName");
            Object v = m.invoke(u);
            return v == null ? null : String.valueOf(v);
        } catch (Exception ignore) {
            return null;
        }
    }

    private void setFullNameIfPresent(User u, String fullName) {
        if (fullName == null) return;
        try {
            Method m = u.getClass().getMethod("setFullName", String.class);
            m.invoke(u, fullName);
        } catch (Exception ignore) {
            // entity may not have fullName; ignore gracefully
        }
    }

    private void setDailyTargetIfPresent(User u, Integer minutes) {
        if (minutes == null) return;
        // Prefer setter if present
        try {
            Method setter = u.getClass().getMethod("setDailyTargetMinutes", Integer.class);
            setter.invoke(u, minutes);
            return;
        } catch (Exception ignored) { }
        // Fallback: direct field access if it exists (Integer)
        try {
            Field f = u.getClass().getDeclaredField("dailyTargetMinutes");
            f.setAccessible(true);
            f.set(u, minutes);
        } catch (Exception ignored) { }
    }

    private Integer getDailyTargetIfPresent(User u) {
        // Prefer getter
        try {
            Method getter = u.getClass().getMethod("getDailyTargetMinutes");
            Object v = getter.invoke(u);
            return (v instanceof Integer) ? (Integer) v : null;
        } catch (Exception ignored) { }
        // Fallback field
        try {
            Field f = u.getClass().getDeclaredField("dailyTargetMinutes");
            f.setAccessible(true);
            Object v = f.get(u);
            return (v instanceof Integer) ? (Integer) v : null;
        } catch (Exception ignored) { }
        return null;
    }

    private UserResponse toResponse(User u) {
        List<String> roleNames = (u.getRoles() == null)
                ? Collections.emptyList()
                : u.getRoles().stream().map(Role::getName).sorted().collect(Collectors.toList());

        List<String> groupNames = userGroupRepo.findByUserId(u.getId()).stream()
                .map(UserGroup::getGroup)
                .filter(Objects::nonNull)
                .map(AccessGroup::getName)
                .sorted()
                .collect(Collectors.toList());

        Integer daily = getDailyTargetIfPresent(u);
        String fullName = safeGetFullName(u);

        return new UserResponse(u.getId(), u.getUsername(), fullName, daily, roleNames, groupNames);
    }
}
