package com.dagy.loginandregistrationemail.categories;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public CategoryResponse save(CategoryRequest request) {
        return null;
    }

    @Override
    public CategoryResponse findById(Long id) {
        return null;
    }

    @Override
    public CategoryResponse findByName(String name) {
        return null;
    }

    @Override
    public List<CategoryResponse> findAll() {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
