package com.PIDEV.Blog_Formation_Service.Services;

import com.PIDEV.Blog_Formation_Service.Entities.Rating;

import java.util.List;

public interface IRatingService {
    Rating addRating(Rating rating);
    Rating updateRating(Rating rating);
    void deleteRating(Long id);
    List<Rating> retrieveAllRatings();
    Rating retrieveRatingById(Long id);
    List<Rating> retrieveRatingsByBlog(Long blogId);
    List<Rating> retrieveRatingsByUser(Long userId);
}
