package com.dagy.loginandregistrationemail.demo;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin")
//@SecurityRequirement(name = "bearerAuth") à utilisé quand on a plusieurs SecuritySchemes au lieu de SecurityScheme dans la config
public class AdminController {

    @GetMapping()
    @PreAuthorize("hasAuthority('admin:read')")
    public String get() {
        return "GET:: get admin controller";
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('admin:create')")
    public String post() {
        return "POST:: post admin controller";
    }

    @PutMapping()
    @PreAuthorize("hasAuthority('admin:update')")
    public String put() {
        return "PUT:: put admin controller";
    }

    @DeleteMapping()
    @PreAuthorize("hasAuthority('admin:delete')")
    public String delete() {
        return "DELETE:: delete admin controller";
    }
}
