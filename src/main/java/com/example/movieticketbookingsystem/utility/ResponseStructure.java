package com.example.movieticketbookingsystem.utility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStructure<T> {
    private String status;
    private String message;
    private T data;


}
