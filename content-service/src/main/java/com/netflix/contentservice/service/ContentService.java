package com.netflix.contentservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.netflix.contentservice.dto.MovieRequest;
import com.netflix.contentservice.dto.MovieResponse;
import com.netflix.contentservice.model.Genre;
import com.netflix.contentservice.model.Movie;
import com.netflix.contentservice.model.VideoStatus;
import com.netflix.contentservice.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentService {

    private final MovieRepository movieRepository;

    // add a new movie to a catalog
    public MovieResponse addMovie(MovieRequest request) {
        log.info("adding new movie", request.getTitle());

        Movie movie = new Movie();

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDirector(request.getDirector());
        movie.setCast(request.getCast());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setRating(request.getRating());
        movie.setThumbnailUrl(request.getThumbnailUrl());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setVideoStatus(VideoStatus.PENDING);

        Movie savedmovie = movieRepository.save(movie);

        log.info("movie added with ID : {}", savedmovie.getId());

        return mapToResponse(savedmovie);
    }

    // Get all the movies from catalog
    /*
     * for every movie:
     * convert movie into response DTO
     * return all converted DTOs
     */
    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // get Movie by Id
    public MovieResponse getMoviesById(String movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("movie not found with this id" + movieId));

        return mapToResponse(movie);
    }

    // get movie by genre
    public List<MovieResponse> getMoviesByGenre(Genre genre) {
        return movieRepository.findByGenre(genre)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // search movie by title
    public List<MovieResponse> searchMovies(String title) {
        return movieRepository.findByTitleContaingIgnoreCase(title).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // this method will be called by video-service to update s3 keys
    public void updateVideoKey(String movieId, String videoKey) {
        log.info("updating video key for movie", movieId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found" + movieId));

        movie.setVideoKey(videoKey);
        movie.setVideoStatus(VideoStatus.UPLOADED);

        movieRepository.save(movie);
    }

    // encoder-service will call this funciton
    public void updateHlsUrl(String movieId, String hlsUrl){
        log.info("updating hlsUrl for movie",movieId);
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"+movieId));

        movie.setHlsUrl(hlsUrl);
        movie.setVideoStatus(VideoStatus.READY);
        movieRepository.save(movie);
        log.info("movie is ready for streaming",movieId);
    }
    // Helper function
    private MovieResponse mapToResponse(Movie movie) {
        MovieResponse response = new MovieResponse();

        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setDescription(movie.getDescription());
        response.setDirector(movie.getDirector());
        response.setCast(movie.getCast());
        response.setReleaseDate(movie.getReleaseDate());
        response.setRating(movie.getRating());
        response.setThumbnailUrl(movie.getThumbnailUrl());
        response.setDurationMinutes(movie.getDurationMinutes());
        response.setVideoStatus(VideoStatus.PENDING);
        response.setDurationMinutes(movie.getDurationMinutes());
        response.setVideoKey(movie.getVideoKey());
        response.setVideoStatus(movie.getVideoStatus());
        response.setHlsUrl(movie.getHlsUrl());
        response.setCreatedAt(movie.getCreatedAt());

        return response;
    }
}
