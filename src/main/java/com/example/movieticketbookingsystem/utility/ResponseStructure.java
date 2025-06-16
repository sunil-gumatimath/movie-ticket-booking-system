package com.example.movieticketbookingsystem.utility;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseStructure<T> {
    private int status;
    private String message;
    private T data;
}
