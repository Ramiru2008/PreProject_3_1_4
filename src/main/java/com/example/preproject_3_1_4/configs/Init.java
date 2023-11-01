package com.example.preproject_3_1_4.configs;

import com.example.preproject_3_1_4.entities.Role;
import com.example.preproject_3_1_4.entities.User;
import com.example.preproject_3_1_4.services.RoleService;
import com.example.preproject_3_1_4.services.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class Init {
    private final UserService userService;
    private final RoleService roleService;

    public Init(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void initUser() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(roleAdmin);
        roleService.add(roleAdmin);

        Role roleUser = new Role("ROLE_USER");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleUser);
        roleService.add(roleUser);

        User admin = new User("Ramiru2008", "Roman", "Kudasov", 44, "123", adminRoles);
        userService.add(admin);

        User user = new User("Slipknot", "Corey", "Taylor", 43, "123", userRoles);
        userService.add(user);

    }
}

