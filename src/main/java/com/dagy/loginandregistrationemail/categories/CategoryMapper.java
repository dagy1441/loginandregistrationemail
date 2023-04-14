package com.dagy.loginandregistrationemail.categories;

public class CategoryMapper {
    public static CategoryResponse fromEntity(Category category){
        if (category == null) return null;
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public static Category toEntity(CategoryRequest categoryRequest){
        if (categoryRequest == null) return null;
        Category category = new Category();
        category.setId(categoryRequest.id());
        category.setName(categoryRequest.name());
        category.setDescription(categoryRequest.description());
        return category;
    }
}
