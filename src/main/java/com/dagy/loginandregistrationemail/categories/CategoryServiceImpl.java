package com.dagy.loginandregistrationemail.categories;

import com.dagy.loginandregistrationemail.exceptions.EntityAllReadyExistException;
import com.dagy.loginandregistrationemail.validators.ObjectsValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ObjectsValidator<CategoryRequest> validator;

    @Override
    public CategoryResponse save(CategoryRequest request) {
        validator.validate(request);
        Optional<Category> category = categoryRepository.findByName(request.name());
        if (category.isPresent()) {
            log.warn("Categorie with name {} allready exist DB ", request.name());
            throw new EntityAllReadyExistException(
                    "La categorie avec le nom "
                            + request.name() +
                            " existe dèjà en BD");
        }
        return CategoryMapper.fromEntity(
                categoryRepository.save(CategoryMapper.toEntity(request)
                )
        );
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        return null;
    }

    @Override
    public CategoryResponse findById(Long id) {

        if (id == null) {
            log.error("Category ID est null");
            return null;
        }
        Optional<Category> category = categoryRepository.findById(id);
        return Optional.of(
                        CategoryMapper.fromEntity(category.get()))
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Aucune categorie avec l'ID " + id + " n'a été trouvée")
                );
    }

    @Override
    public CategoryResponse findByName(String name) {
        return null;
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            log.error("Article ID est null");
            return;
        }
        categoryRepository.deleteById(id);
    }
}
