package com.example.movieticketbookingsystem.mapper;

import com.example.movieticketbookingsystem.dto.response.ScreenResponse;
import com.example.movieticketbookingsystem.dto.response.ScreenResponseList;
import com.example.movieticketbookingsystem.dto.response.SeatResponse;
import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Seat;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ScreenMapper {

    private final SeatMapper seatMapper;

    public ScreenResponse toScreenResponse(Screen screen) {
        if (screen == null) {
            return null;
        }

        return new ScreenResponse(
                screen.getScreenId(),
                screen.getScreenType(),
                screen.getCapacity(),
                screen.getNoOfRows()
        );
    }

    public ScreenResponseList toScreenResponseList(Screen screen, List<Seat> seats) {
        if (screen == null) {
            return null;
        }

        List<SeatResponse> seatResponses = seatMapper.toResponseList(seats);

        return new ScreenResponseList(
                screen.getScreenId(),
                screen.getScreenType(),
                screen.getCapacity(),
                screen.getNoOfRows(),
                seatResponses
        );
    }
}
