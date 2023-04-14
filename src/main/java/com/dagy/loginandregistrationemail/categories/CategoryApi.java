package com.dagy.loginandregistrationemail.categories;

import com.dagy.loginandregistrationemail.utilities.helpers.ApiDataResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CategoryApi {
    @PostMapping(
            value = "/ng-spring-blog/api/v1/categories/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiDataResponse> save(@RequestBody CategoryRequest request);
    ResponseEntity<ApiDataResponse> findById(@PathVariable("idCategory") Long idCategory);

//    ResponseEntity<ApiDataResponse> findByName(@PathVariable("name") String name);
    ResponseEntity<ApiDataResponse> findAll();
    void delete(@PathVariable("idCategory") Long id);


}
