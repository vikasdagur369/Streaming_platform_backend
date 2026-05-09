package com.netflix.contentservice.dto;

import com.netflix.contentservice.model.Genre;
import com.netflix.contentservice.model.VideoStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
    private String Id;
    private String title;
    private String description;
    private Genre genre;
    private String director;
    private String cast;
    private String releaseDate;
    private double rating;
    private String thumbnailUrl;
    private int durationMinutes;
    private String videoKey;
    private String hlsUrl;
    private VideoStatus videoStatus;


}
