package fr.greta95.springbootjunit.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.greta95.springbootjunit.model.Movie;
import fr.greta95.springbootjunit.service.MovieService;

@WebMvcTest
public class MovieControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MovieService movieService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Movie avatarMovie;
	private Movie titanicMovie;
	
	@BeforeEach
	void init() {
		avatarMovie = new Movie();
		avatarMovie.setId(1L);
		avatarMovie.setName("Avatar");
		avatarMovie.setGenre("Action");
		avatarMovie.setReleaseDate(LocalDate.of(1999, Month.APRIL, 22));
		
		titanicMovie = new Movie();
		avatarMovie.setId(2L);
		titanicMovie.setName("Titanic");
		titanicMovie.setGenre("Romance");
		titanicMovie.setReleaseDate(LocalDate.of(2004, Month.JANUARY, 10));
	}
	
	@Test
	void shouldCreateNewMovie() throws Exception {	
		//conversion de l'objet java avatarMovie en String json
		//{"id":2,"name":"Avatar","genre":"Action","releaseDate":"1999-04-22"}
		String s = objectMapper.writeValueAsString(avatarMovie); 

		when(movieService.save(any(Movie.class))).thenReturn(avatarMovie);
		
		mockMvc.perform(post("/movies")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(avatarMovie)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.name", is(avatarMovie.getName())))
		.andExpect(jsonPath("$.genre", is(avatarMovie.getGenre())))
		.andExpect(jsonPath("$.releaseDate", is(avatarMovie.getReleaseDate().toString())));	
	}
	
	@Test
	void shouldFetchAllMovies() throws Exception {
		
		List<Movie> list = new ArrayList<>();
		list.add(avatarMovie);
		list.add(titanicMovie);
		
		when(movieService.getAllMovies()).thenReturn(list);
		
		mockMvc.perform(get("/movies"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()", is(list.size())));
	}
	
	@Test
	void shouldFetchOneMovieById() throws Exception {
		
		when(movieService.getMovieById(anyLong())).thenReturn(avatarMovie);
		
		mockMvc.perform(get("/movies/{id}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(avatarMovie.getName())))
			.andExpect(jsonPath("$.genre", is(avatarMovie.getGenre())));
			//.andExpect(jsonPath("$.id", is(avatarMovie.getId())))
	}
	
	@Test
	void shouldDeleteMovie() throws Exception {
		
		doNothing().when(movieService).deleteMovie(anyLong());
		
		mockMvc.perform(delete("/movies/{id}", 1L))
			.andExpect(status().isNoContent());
			
	}
	
	@Test
	void shouldUpdateMovie() throws Exception {
		
		when(movieService.updateMovie(any(Movie.class), anyLong())).thenReturn(avatarMovie);		
		mockMvc.perform(put("/movies/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(avatarMovie)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is(avatarMovie.getName())))
		.andExpect(jsonPath("$.genre", is(avatarMovie.getGenre())));
	}
}




























