package com.dagy.loginandregistrationemail.categories;

import com.dagy.loginandregistrationemail.utilities.helpers.ApiDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Map;

import static java.time.LocalDateTime.now;

@Controller
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {
    private final CategoryService categoryService;

    @Override
    public ResponseEntity<ApiDataResponse> save(CategoryRequest request) {
        return ResponseEntity.ok(
                ApiDataResponse.builder()
                        .time(now())
                        .message("Une nouvelle categorie ajoutée")
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("categories", categoryService.save(request)))
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiDataResponse> findById(Long idCategory) {
        return null;
    }

    @Override
    public ResponseEntity<ApiDataResponse> findAll() {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
