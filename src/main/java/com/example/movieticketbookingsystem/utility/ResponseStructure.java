package com.example.movieticketbookingsystem.utility;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseStructure<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ResponseStructure<T> build(HttpStatus status, String message, T data){
        ResponseStructure<T> response=new ResponseStructure<>();
        response.status=status.value();
        response.message=message;
        response.data=data;
        return response;
    }
}
