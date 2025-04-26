package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.ScreenRequest;
import com.example.movieticketbookingsystem.dto.response.ScreenResponse;
import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.exception.ScreenIdNotFoundException;
import com.example.movieticketbookingsystem.exception.TheaterOwnerIdException;
import com.example.movieticketbookingsystem.repository.ScreenRepository;
import com.example.movieticketbookingsystem.repository.TheaterRepository;
import com.example.movieticketbookingsystem.service.ScreenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;
    private final SeatServiceImpl seatService;

    @Override
    public ScreenResponse addScreen(String theaterId, ScreenRequest screenRequest) {
        Optional<Theater> theaterOptional = theaterRepository.findById(theaterId);

        if (theaterOptional.isEmpty()){
            throw new TheaterOwnerIdException("Theater id Not Found");
        }else {
            Theater theater = theaterOptional.get();
            Screen screen = new Screen();

            screen.setScreenType(screenRequest.screenType());
            screen.setCapacity(screenRequest.capacity());
            screen.setNoOfRows(screenRequest.noOfRows());
            screen.setTheater(theater);

            List<Screen> screens = new ArrayList<>();
            screens.add(screen);
            theater.setScreen(screens);

            theaterRepository.save(theater);
            screenRepository.save(screen);

            seatService.generateSeatLayout(screen);


            return new ScreenResponse(
                    screen.getScreenType(),
                    screen.getCapacity(),
                    screen.getNoOfRows()
            );
        }

    }

    @Override
    public ScreenResponse findScreen(String screenId, ScreenRequest screenRequest) {
        Optional<Screen> screen = screenRepository.findById(screenId);

        if (screen.isEmpty()){
            throw new ScreenIdNotFoundException("Screen Id Not Found ");
        }else {
            Screen screen1 = screen.get();
            return new ScreenResponse(
                    screen1.getScreenType(),
                    screen1.getCapacity(),
                    screen1.getNoOfRows()
            );
        }
    }
}
