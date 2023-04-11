package com.dagy.loginandregistrationemail.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ObjectNotValidException extends  RuntimeException{
    private final Set<String> errorMessages;
}
