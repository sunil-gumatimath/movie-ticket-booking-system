package com.example.movieticketbookingsystem.mapper;

import com.example.movieticketbookingsystem.dto.response.SeatResponse;
import com.example.movieticketbookingsystem.entity.Seat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeatMapper {

    /**
     * Maps a Seat entity to a SeatResponse DTO
     * 
     * @param seat The Seat entity
     * @return A new SeatResponse DTO
     */
    public SeatResponse toResponse(Seat seat) {
        if (seat == null) {
            return null;
        }
        
        return new SeatResponse(
                seat.getSeatId(),
                seat.getSeatName()
        );
    }
    
    /**
     * Maps a list of Seat entities to a list of SeatResponse DTOs
     * 
     * @param seats The list of Seat entities
     * @return A list of SeatResponse DTOs
     */
    public List<SeatResponse> toResponseList(List<Seat> seats) {
        if (seats == null) {
            return List.of();
        }
        
        return seats.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
