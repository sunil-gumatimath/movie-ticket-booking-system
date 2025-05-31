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

import java.time.Instant;
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

        int noOfRows = screen.getNoOfRows();
        int capacity = screen.getCapacity();

        if (noOfRows <= 0 || capacity <= 0) {
            throw new IllegalArgumentException("Number of rows and capacity must be positive");
        }

        if (capacity % noOfRows != 0) {
            throw new IllegalArgumentException("Capacity must be evenly divisible by number of rows");
        }

        int seatsPerRow = capacity / noOfRows;

        List<Seat> seatList = new ArrayList<>();
        char rowName = 'A';

        for (int i = 1; i <= noOfRows; i++) {
            for (int j = 1; j <= seatsPerRow; j++) {
                Seat newSeat = new Seat();
                newSeat.setSeatName(rowName + String.valueOf(j));
                newSeat.setScreen(existingScreen);
                seatList.add(newSeat);
            }
            rowName++;
        }

        seatRepository.saveAll(seatList);
        existingScreen.setSeats(seatList);
        screenRepository.save(existingScreen);
    }
}