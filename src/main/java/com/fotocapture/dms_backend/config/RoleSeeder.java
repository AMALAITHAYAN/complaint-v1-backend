package com.fotocapture.dms_backend.config;

import com.fotocapture.dms_backend.entity.Role;
import com.fotocapture.dms_backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        List<String> roleNames = List.of("ROLE_ADMIN", "ROLE_SCANNER", "ROLE_REVIEWER", "ROLE_VIEWER");

        for (String roleName : roleNames) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                System.out.println("Seeded role: " + roleName);
            }
        }
    }
}
