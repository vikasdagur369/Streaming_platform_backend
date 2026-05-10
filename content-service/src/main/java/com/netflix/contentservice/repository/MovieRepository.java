package com.netflix.contentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.netflix.contentservice.model.Movie;

public interface MovieRepository extends JpaRepository<Movie,String>{

    
} 
