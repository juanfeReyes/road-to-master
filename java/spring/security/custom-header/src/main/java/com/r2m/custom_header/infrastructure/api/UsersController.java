package com.r2m.custom_header.infrastructure.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @GetMapping()
    @PreAuthorize("hasRole('READER')")
    public List<String> getUsers()
    {
        return List.of("user1");
    }

    @GetMapping("/managers")
    public List<String> getManagers()
    {
        return List.of("manager1");
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public List<String> getAdmin()
    {
        return List.of("admin1");
    }
}
