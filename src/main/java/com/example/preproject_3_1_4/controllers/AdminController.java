package com.example.preproject_3_1_4.controllers;

import com.example.preproject_3_1_4.dto.UserDto;
import com.example.preproject_3_1_4.entities.Role;
import com.example.preproject_3_1_4.entities.User;
import com.example.preproject_3_1_4.services.RoleService;
import com.example.preproject_3_1_4.services.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public String show(Model model, Principal principal) {
        String username = principal.getName();
        User currentUser = userService.findByUsername(username);
        if (currentUser == null) {
            throw new UsernameNotFoundException("ERROR");
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/new")
    public String registration(Model model, Principal principal) {
        String username = principal.getName();
        User currentUser = userService.findByUsername(username);
        User user = new User();
        Collection<Role> roles = roleService.getAllRoles();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "new";
    }

    @PostMapping("/users")
    public String create(@ModelAttribute("user") UserDto userDto) {
        List<Role> roles = roleService.getRoleByIds(userDto.getRoles());
        if (userService.findByUsername(userDto.getUsername()) == null) {
            User user = new User(
                    userDto.getUsername(),
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getAge(),
                    userDto.getPassword(),
                    new HashSet<>(roles)
            );
            userService.add(user);
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user";
    }


    @PatchMapping("/{id}")
    public String editUser(@ModelAttribute("user") UserDto userDto) {
        User user = userService.getUserById(userDto.getId());
        List<Role> roles = roleService.getRoleByIds(userDto.getRoles());

        Set<Role> setRoles = new HashSet<>(roles);
        user.setRoles(setRoles);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }
        user.setAge(userDto.getAge());
        if (!userDto.getPassword().isEmpty()) {
            userService.edit(user);
        }
        return "redirect:/admin/users";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") Long id, Principal principal, Model model) {
        String logUsername = principal.getName();
        User currentUser = userService.findByUsername(logUsername);
        User userToDel = userService.getUserById(id);
        if (!userToDel.equals(currentUser))
            { userService.removeUserById(id);
        }
        return "redirect:/admin/users";
    }
}
