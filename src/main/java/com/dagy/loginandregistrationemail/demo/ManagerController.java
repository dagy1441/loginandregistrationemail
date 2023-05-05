package com.dagy.loginandregistrationemail.demo;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/manager")
@Tag(name = "Manager")
public class ManagerController {

    @Operation(
            description = "Get endpoint for manager",
            summary = "This is a summary for management get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }

    )
    @GetMapping()
    public String get() {
        return "GET:: get manager controller";
    }

    @PostMapping()
//    @Hidden permet de cacher un end point dans la doc openapi
    public String post() {
        return "POST:: post manager controller";
    }

    @PutMapping()
    public String put() {
        return "PUT:: put manager controller";
    }

    @DeleteMapping()
    public String delete() {
        return "DELETE:: delete manager controller";
    }
}
