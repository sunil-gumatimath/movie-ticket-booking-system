package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Seat;
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
        Screen existingScreen = screenRepository.findById(screen.getScreenId())
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        int noOfRows = screen.getNoOfRows();
        int seatsPerRow = screen.getCapacity() / noOfRows;

        List<Seat> seatList = new ArrayList<>();
        char rowName = 'A';

        for (int i = 1; i <= noOfRows; i++) {
            for (int j = 1; j <= seatsPerRow; j++) {
                Seat newSeat = new Seat();
                newSeat.setSeatName(rowName + String.valueOf(j));
                newSeat.setCreatedAt(Instant.ofEpochMilli(System.currentTimeMillis()));
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