package com.example.movieticketbookingsystem.utility;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ErrorStructure<T> {

    private String type;
    private int status;        //404
    private String message;         // failed to update the user (error message)

}
