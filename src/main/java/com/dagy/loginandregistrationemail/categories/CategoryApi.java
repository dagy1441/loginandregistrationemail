package com.dagy.loginandregistrationemail.categories;

import com.dagy.loginandregistrationemail.utilities.helpers.ApiDataResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface CategoryApi {
    @PostMapping(
            value = "/ng-spring-blog/api/v1/categories",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiDataResponse> save(@RequestBody CategoryRequest request);

    @GetMapping(
            value = "/ng-spring-blog/api/v1/categories/{idCategory}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiDataResponse> findById(@PathVariable("idCategory") Long idCategory);

//    ResponseEntity<ApiDataResponse> findByName(@PathVariable("name") String name);

    @GetMapping(
            value = "/ng-spring-blog/api/v1/categories",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiDataResponse> findAll();

    @DeleteMapping(
            value = "/ng-spring-blog/api/v1/categories/{idCategory}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiDataResponse> delete(@PathVariable("idCategory") Long idCategory);


}
