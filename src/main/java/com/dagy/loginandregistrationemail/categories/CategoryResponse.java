package com.dagy.loginandregistrationemail.categories;

import lombok.Builder;

@Builder
public record CategoryResponse(
        Long id,
        String name,
        String description
) {
}
