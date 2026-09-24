package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Seat;
import com.example.movieticketbookingsystem.exception.ScreenIdNotFoundException;
import com.example.movieticketbookingsystem.repository.ScreenRepository;
import com.example.movieticketbookingsystem.repository.SeatRepository;
import com.example.movieticketbookingsystem.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;

    @Override
    @Transactional
    public void generateSeatLayout(Screen screen) {
        if (screen == null || screen.getScreenId() == null) {
            throw new IllegalArgumentException("Screen and screen ID cannot be null");
        }

        Screen existingScreen = screenRepository.findById(screen.getScreenId())
                .orElseThrow(() -> new ScreenIdNotFoundException("Screen not found with ID: " + screen.getScreenId()));

        if (existingScreen.getSeats() != null && !existingScreen.getSeats().isEmpty()) {
            return;
        }

        int noOfRows = existingScreen.getNoOfRows();
        int capacity = existingScreen.getCapacity();
        validateLayout(capacity, noOfRows);

        int seatsPerRow = capacity / noOfRows;
        List<Seat> seatList = new ArrayList<>(capacity);

        for (int rowIndex = 0; rowIndex < noOfRows; rowIndex++) {
            char rowName = (char) ('A' + rowIndex);
            for (int seatIndex = 1; seatIndex <= seatsPerRow; seatIndex++) {
                Seat seat = new Seat();
                seat.setSeatName(rowName + String.valueOf(seatIndex));
                seat.setScreen(existingScreen);
                seatList.add(seat);
            }
        }

        seatRepository.saveAll(seatList);
        existingScreen.setSeats(seatList);
        screenRepository.save(existingScreen);
    }

    private void validateLayout(int capacity, int noOfRows) {
        if (capacity <= 0 || capacity > 1000 || noOfRows <= 0 || noOfRows > 26) {
            throw new IllegalArgumentException("Screen capacity and rows are outside the supported range");
        }
        if (capacity % noOfRows != 0) {
            throw new IllegalArgumentException("Capacity must be evenly divisible by number of rows");
        }
    }
}
