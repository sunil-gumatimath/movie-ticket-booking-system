package com.example.movieticketbookingsystem.utility;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorStructure {

    private int statusCode;

    @JsonProperty("error_message")
    private String message;

    private String timestamp;

    private String path;
}
