package com.dagy.loginandregistrationemail.categories;

import com.dagy.loginandregistrationemail.exceptions.EntityAllReadyExistException;
import com.dagy.loginandregistrationemail.validators.ObjectsValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if (category.isPresent()){
            log.warn("Categorie with name {} allready exist DB ", request.name());
            throw new EntityAllReadyExistException(
                    "La categorie avec le nom "
                            +request.name()+
                            " existe dèjà en BD");
        }
        return CategoryMapper.fromEntity(
                categoryRepository.save(CategoryMapper.toEntity(request)
                )
        );
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
