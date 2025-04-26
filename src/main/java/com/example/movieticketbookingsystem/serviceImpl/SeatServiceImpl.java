package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Seat;
import com.example.movieticketbookingsystem.repository.ScreenRepository;
import com.example.movieticketbookingsystem.repository.SeatRepository;
import com.example.movieticketbookingsystem.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;

    @Override
    public void generateSeatLayout(Screen screen) {

        Screen existingScreen = screenRepository.findById(screen.getScreenId()).get();

        List<Seat> seatList = new ArrayList<Seat>();

        int noOfRows = screen.getNoOfRows();
        int seatsPerRow = screen.getCapacity()/noOfRows;

        char rowName = 'A';
        int colomNumber =1;

        for (int i=1;i<noOfRows;i++){
            for (int j = 1; j<=seatsPerRow; j++){
                Seat seat = new Seat();

                seat.setSeatName(rowName+String.valueOf(j));
                seat.setCreatedAt(System.currentTimeMillis());
                seat.setScreen(existingScreen);
            }
            rowName++;
        }
        existingScreen.setSeat(seatList);
        screenRepository.save(existingScreen);
    }
}
