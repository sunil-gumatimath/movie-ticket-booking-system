package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.TheaterRequest;
import com.example.movieticketbookingsystem.dto.response.TheaterResponse;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.entity.TheaterOwner;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.enums.UserRole;
import com.example.movieticketbookingsystem.exception.TheaterOwnerIdException;
import com.example.movieticketbookingsystem.exception.UserNotFoundByEmailException;
import com.example.movieticketbookingsystem.repository.TheaterOwnerRepository;
import com.example.movieticketbookingsystem.repository.TheaterRepository;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.service.TheaterService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class TheaterServiceImpl implements TheaterService {

    private final TheaterRepository theaterRepository;
    private final UserRepository userRepository;
    private final TheaterOwnerRepository theaterOwnerRepository;

    @Override
    public TheaterResponse createTheater(String email, TheaterRequest theaterRequest) {
        Optional<UserDetails> optionalUserDetails = userRepository.findByEmail(email);
        if (optionalUserDetails.isEmpty()) {
            throw new UserNotFoundByEmailException("User not found with email: " + email);
        }else{
            UserDetails userDetails=optionalUserDetails.get();
            if (userDetails.getUserRole() != UserRole.THEATER_OWNER){
                throw new UserNotFoundByEmailException("this user not theater owner");
            }else{
            Theater newTheater = new Theater();
            newTheater.setName(theaterRequest.name());
            newTheater.setAddress(theaterRequest.address());
            newTheater.setCity(theaterRequest.city());
            newTheater.setLandmark(theaterRequest.landmark());
            newTheater.setCreatedAt(System.currentTimeMillis());
            //  newTheater.setCreatedBy(userDetails.getUserId());

            newTheater.setOwner((TheaterOwner) userDetails);

            List<Theater> theaterList=new ArrayList<Theater>();
            theaterList.add(newTheater);

            TheaterOwner theaterOwner=new TheaterOwner();
            theaterOwner.setTheaters(theaterList);

            theaterOwnerRepository.save(theaterOwner);
            theaterRepository.save(newTheater);
            return new TheaterResponse(
                    newTheater.getName(),
                    newTheater.getAddress(),
                    newTheater.getCity(),
                    newTheater.getLandmark()
            );
        }
    }
}

    @Override
    public TheaterResponse findTheater(String id, TheaterRequest theaterRequest) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new TheaterOwnerIdException("Theater not found with id: " + id));

        return new TheaterResponse(
                theater.getName(),
                theater.getAddress(),
                theater.getCity(),
                theater.getLandmark()
        );
    }


    @Override
    public TheaterResponse updateTheater(String id, TheaterRequest theaterRequest) {
        return null;
    }

}