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
        return ResponseEntity.ok(
                ApiDataResponse.builder()
                        .time(now())
                        .message("Liste des catégories")
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("categories", categoryService.findById(idCategory)))
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiDataResponse> findAll() {
        return ResponseEntity.ok(
                ApiDataResponse.builder()
                        .time(now())
                        .message("Liste des catégories")
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("categories", categoryService.findAll()))
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiDataResponse> delete(Long idCategory) {
        categoryService.delete(idCategory);
        return ResponseEntity.ok(
                ApiDataResponse.builder()
                        .time(now())
                        .message("Catégorie supprimée")
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }


}
