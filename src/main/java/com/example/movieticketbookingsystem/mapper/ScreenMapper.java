package com.example.movieticketbookingsystem.mapper;

import com.example.movieticketbookingsystem.dto.response.ScreenResponse;
import com.example.movieticketbookingsystem.dto.response.ScreenDetailResponse;
import com.example.movieticketbookingsystem.dto.response.SeatResponse;
import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
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

    public ScreenDetailResponse toScreenDetailResponse(Screen screen, List<Seat> seats) {
        if (screen == null) {
            return null;
        }

        List<SeatResponse> seatResponses = seatMapper.toResponseList(seats);

        return new ScreenDetailResponse(
                screen.getScreenId(),
                screen.getScreenType(),
                screen.getCapacity(),
                screen.getNoOfRows(),
                seatResponses
        );
    }
}
