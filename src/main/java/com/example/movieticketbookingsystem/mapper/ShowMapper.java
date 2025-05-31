package com.example.movieticketbookingsystem.mapper;

import com.example.movieticketbookingsystem.dto.response.ShowResponse;
import com.example.movieticketbookingsystem.entity.Shows;
import org.springframework.stereotype.Component;

@Component
public class ShowMapper {

    public ShowResponse toShowResponse(Shows shows) {
        if (shows == null) {
            return null;
        }
        
        return new ShowResponse(
                shows.getShowId(),
                shows.getMovie().getTitle(),
                shows.getTheater().getName(),
                shows.getScreen().getScreenId(),
                shows.getStartsAt().toEpochMilli(),
                shows.getEndsAt().toEpochMilli()
        );
    }
}
