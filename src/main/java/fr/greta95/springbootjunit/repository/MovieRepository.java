package fr.greta95.springbootjunit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.greta95.springbootjunit.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	List<Movie> findByGenre(String genre);
}
