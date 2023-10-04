package fr.greta95.springbootjunit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.greta95.springbootjunit.model.Movie;
import fr.greta95.springbootjunit.repository.MovieRepository;
//import fr.greta95.springbootjunit.service.MovieService;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
	
	@Mock
	private MovieRepository movieRepository;
	
	@InjectMocks
	private MovieService movieService;
	
	private Movie avatarMovie;
	private Movie titanicMovie;
	
	@BeforeEach
	void init() {
		avatarMovie = new Movie();
		avatarMovie.setId(1L);
		avatarMovie.setName("Avatar");
		avatarMovie.setGenre("Action");
		avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 23));
		
		titanicMovie = new Movie();
		titanicMovie.setId(2L);
		titanicMovie.setName("Titanic");
		titanicMovie.setGenre("Romance");
		titanicMovie.setReleaseDate(LocalDate.of(2004, Month.JANUARY, 10));
	}
	
	@Test
	void save() {
		
		when(movieRepository.save(any(Movie.class))).thenReturn(avatarMovie);
		
		Movie newMovie = movieService.save(avatarMovie);
		
		assertNotNull(newMovie);
		assertThat(newMovie.getName()).isEqualTo("Avatar");
	}
	
	@Test
	void getMovies() {
		
		List<Movie> list = new ArrayList<>();
		list.add(avatarMovie);
		list.add(titanicMovie);
		
		when(movieRepository.findAll()).thenReturn(list);
		
		List<Movie> movies = movieService.getAllMovies();
		
		assertEquals(2, movies.size());
		assertNotNull(movies);
	}
	
	@Test
	void getMovieById() {
		when(movieRepository.findById(1L)).thenReturn(Optional.of(avatarMovie));
		Movie existingMovie = movieService.getMovieById(avatarMovie.getId());
		assertNotNull(existingMovie);
		
		System.out.println(existingMovie.getId());
		assertThat(existingMovie.getId()).isNotEqualTo(null);
		assertThat(existingMovie.getId()).isEqualTo(avatarMovie.getId());
		assertThat(existingMovie.getId()).isNotEqualTo(titanicMovie.getId());
	}
	
	@Test
	void getMovieByIdForException() {
		
		when(movieRepository.findById(1L)).thenReturn(Optional.of(avatarMovie));
		assertThrows(RuntimeException.class, () -> {
			movieService.getMovieById(2L);  
			//avec 2 la RunTime exception de MovieService est levée, 
			//							le test est ok
			//avec 1 la RunTime exception n'est pas levée
		});
	}
	
	@Test
	void updateMovie() {
		
		when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
		
		when(movieRepository.save(any(Movie.class))).thenReturn(avatarMovie);
		avatarMovie.setGenre("Fantacy");
		Movie existingMovie = movieService.updateMovie(avatarMovie, avatarMovie.getId());
		
		assertNotNull(existingMovie);
		assertEquals("Fantacy", avatarMovie.getGenre());
	}
	
	@Test
	void deleteMovie() {
		
		Long movieId = 1L;
		when(movieRepository.findById(movieId)).thenReturn(Optional.of(avatarMovie));
		
		//ne retourne rien à l'exécution de delete() du movieRepository
		doNothing().when(movieRepository).delete(any(Movie.class));
		
		movieService.deleteMovie(movieId);
		
		//vérifie que la méthode delete() de movieRepository
		//(donc de JpaRepository) a été appelée 1 fois
		verify(movieRepository, times(1)).delete(avatarMovie);
		
	}
}




















