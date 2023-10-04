package fr.greta95.springbootjunit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import fr.greta95.springbootjunit.model.Movie;
 

@DataJpaTest
public class MovieRepositoryTest {
	
	@Autowired		//injection du repository
	private MovieRepository movieRepository;
	
	private Movie avatarMovie; 
	private Movie titanicMovie;

	@BeforeEach		//exécuté avant chaque test
	void init() {
		avatarMovie = new Movie();
		avatarMovie.setName("Avatar");
		avatarMovie.setGenre("Action");
		avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 23));
		
		titanicMovie = new Movie();
		titanicMovie.setName("Titanic");
		titanicMovie.setGenre("Romance");
		titanicMovie.setReleaseDate(LocalDate.of(2004, Month.JANUARY, 10));
	}
	
	@Test
	@DisplayName("Doit sauvegarder un film dans la base")
	void save() {
		Movie newMovie = movieRepository.save(avatarMovie);
		assertNotNull(newMovie);
		assertThat(newMovie.getId()).isNotEqualTo(null);
		assertThat(newMovie.getName()).isEqualTo(avatarMovie.getName());
	}
	
	@Test
	@DisplayName("Doit retourner un nombre de films sauvegardés égal à 2")
	void getAllMovies() {
		movieRepository.save(avatarMovie);
		movieRepository.save(titanicMovie);
		
		List<Movie> list = movieRepository.findAll();
		
		assertNotNull(list);
		assertThat(list).isNotNull();
		assertEquals(2, list.size());
	}
	
	@Test
	@DisplayName("Doit retourner le film à partir de son id")
	void getMovieById() {
		movieRepository.save(avatarMovie);
		
		Movie newMovie = movieRepository.findById(avatarMovie.getId()).get();
		
		assertNotNull(newMovie);
		assertEquals("Action", newMovie.getGenre());
		assertThat(newMovie.getReleaseDate()).isBefore(LocalDate.of(2000, Month.APRIL, 24));
	}
	
	@Test
	@DisplayName("Doit retourner les films de genre ROMANCE")
	void getMoviesByGenera() {
		
		movieRepository.save(avatarMovie);
		movieRepository.save(titanicMovie);
		
		List<Movie> list = movieRepository.findByGenre("Romance");
		
		assertNotNull(list);
		assertThat(list.size()).isEqualTo(1);
	}
	
	@Test
	@DisplayName("Doit mettre à jour un film avec le genre FANTACY")
	void updateMovie() {
		
		movieRepository.save(avatarMovie);
		
		Movie existingMovie = movieRepository.findById(avatarMovie.getId()).get();
		existingMovie.setGenre("Fantacy");
		Movie updatedMovie = movieRepository.save(existingMovie);
		
		assertEquals("Fantacy", updatedMovie.getGenre());
		assertEquals("Avatar", updatedMovie.getName());
	}
	
	@Test
	@DisplayName("Doit supprimer le film")
	void deleteMovie() {
		
		movieRepository.save(avatarMovie);
		Long id = avatarMovie.getId();
		
		movieRepository.save(titanicMovie);
		
		movieRepository.delete(avatarMovie);
		
		List<Movie> list = movieRepository.findAll();
		
		Optional<Movie> exitingMovie = movieRepository.findById(id);
		
		assertEquals(1, list.size());
		assertThat(exitingMovie).isEmpty();
		
	}
}



























