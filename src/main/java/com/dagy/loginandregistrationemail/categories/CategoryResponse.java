package com.dagy.loginandregistrationemail.categories;

import lombok.Builder;
import lombok.Getter;

@Builder
public record CategoryResponse(
        Long id,
        String name,
        String description
) {
}
