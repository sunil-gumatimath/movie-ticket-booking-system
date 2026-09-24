package com.example.movieticketbookingsystem;

import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Shows;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.entity.TheaterOwner;
import com.example.movieticketbookingsystem.enums.Certificate;
import com.example.movieticketbookingsystem.enums.Genre;
import com.example.movieticketbookingsystem.enums.UserRole;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.repository.ScreenRepository;
import com.example.movieticketbookingsystem.repository.ShowRepository;
import com.example.movieticketbookingsystem.repository.TheaterRepository;
import com.example.movieticketbookingsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "jwt.secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
@Transactional
class ShowPersistenceIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TheaterRepository theaterRepository;
    @Autowired
    private ScreenRepository screenRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ShowRepository showRepository;

    @Test
    void supportsMultipleNonOverlappingShowsOnOneScreen() {
        TheaterOwner owner = new TheaterOwner();
        owner.setUsername("show_owner");
        owner.setEmail("show_owner@gmail.com");
        owner.setPassword("encoded");
        owner.setUserRole(UserRole.ROLE_THEATER_OWNER);
        userRepository.saveAndFlush(owner);

        Theater theater = new Theater();
        theater.setName("Test Theater");
        theater.setAddress("Test Address");
        theater.setCity("Test City");
        theater.setLandmark("Test Landmark");
        theater.setOwner(owner);
        theaterRepository.saveAndFlush(theater);

        Screen screen = new Screen();
        screen.setScreenType(com.example.movieticketbookingsystem.enums.ScreenType.TWO_D);
        screen.setCapacity(10);
        screen.setNoOfRows(2);
        screen.setTheater(theater);
        screenRepository.saveAndFlush(screen);

        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setDescription("Test Description");
        movie.setRuntime(Duration.ofHours(2));
        movie.setCertificate(Certificate.U);
        movie.setGenre(Genre.DRAMA);
        movie.setCastList(Set.of("Actor"));
        movieRepository.saveAndFlush(movie);

        Instant start = Instant.now().plusSeconds(3600);
        Shows first = show(start, start.plusSeconds(3600), theater, screen, movie);
        Shows second = show(start.plusSeconds(7200), start.plusSeconds(10800), theater, screen, movie);
        showRepository.saveAllAndFlush(java.util.List.of(first, second));

        assertThat(showRepository.countByScreenScreenId(screen.getScreenId())).isEqualTo(2);
    }

    private Shows show(Instant start, Instant end, Theater theater, Screen screen, Movie movie) {
        Shows show = new Shows();
        show.setTheater(theater);
        show.setScreen(screen);
        show.setMovie(movie);
        show.setStartsAt(start);
        show.setEndsAt(end);
        return show;
    }
}
