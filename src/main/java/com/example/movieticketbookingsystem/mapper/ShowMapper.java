package com.example.movieticketbookingsystem.mapper;

import com.example.movieticketbookingsystem.dto.response.ShowResponse;
import com.example.movieticketbookingsystem.entity.Show;
import org.springframework.stereotype.Component;

@Component
public class ShowMapper {

    public ShowResponse toShowResponse(Show show) {
        if (show == null) {
            return null;
        }

        return new ShowResponse(
                show.getShowId(),
                show.getMovie().getTitle(),
                show.getTheater().getName(),
                show.getScreen().getScreenId(),
                show.getStartsAt().toEpochMilli(),
                show.getEndsAt().toEpochMilli()
        );
    }
}
