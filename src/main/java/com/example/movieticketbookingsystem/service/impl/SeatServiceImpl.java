package com.example.movieticketbookingsystem.service.impl;

import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Seat;
import com.example.movieticketbookingsystem.repository.SeatRepository;
import com.example.movieticketbookingsystem.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    /**
     * Creates seats A1..An, B1..Bn, ... for a newly saved screen. The screen's capacity and
     * row count were validated by {@code ScreenRequest} (rows 1-26, capacity divisible by rows).
     */
    @Override
    @Transactional
    public void generateSeatLayout(Screen screen) {
        if (!screen.getSeats().isEmpty()) {
            return;
        }

        int noOfRows = screen.getNoOfRows();
        int seatsPerRow = screen.getCapacity() / noOfRows;
        List<Seat> seats = new ArrayList<>(screen.getCapacity());

        for (int rowIndex = 0; rowIndex < noOfRows; rowIndex++) {
            char rowName = (char) ('A' + rowIndex);
            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                Seat seat = new Seat();
                seat.setSeatName(rowName + String.valueOf(seatNumber));
                seat.setScreen(screen);
                seats.add(seat);
            }
        }

        screen.getSeats().addAll(seatRepository.saveAll(seats));
    }
}
