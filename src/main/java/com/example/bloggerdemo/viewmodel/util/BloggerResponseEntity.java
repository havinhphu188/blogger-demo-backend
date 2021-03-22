package com.example.bloggerdemo.viewmodel.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BloggerResponseEntity<T> extends ResponseEntity<T> {

    /**
     * Create a new {@code ResponseEntity} with the given status code, and no body nor headers.
     *
     * @param status the status code
     */
    public BloggerResponseEntity(HttpStatus status) {
        super(status);
    }

    public static ResponseEntity<?> ok(ViewModel viewModel) {
        return ok(viewModel.toView());
    }
}
