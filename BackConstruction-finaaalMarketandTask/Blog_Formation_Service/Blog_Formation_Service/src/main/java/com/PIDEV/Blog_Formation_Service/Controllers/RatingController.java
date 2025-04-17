package com.PIDEV.Blog_Formation_Service.Controllers;

import com.PIDEV.Blog_Formation_Service.Entities.Rating;
import com.PIDEV.Blog_Formation_Service.Services.IRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private IRatingService ratingService;

    // Ajouter une évaluation
    @PostMapping
    public Rating addRating(@RequestBody Rating rating) {
        return ratingService.addRating(rating);
    }

    // Mettre à jour une évaluation
    @PutMapping
    public Rating updateRating(@RequestBody Rating rating) {
        return ratingService.updateRating(rating);
    }

    // Supprimer une évaluation
    @DeleteMapping("/{id}")
    public void deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
    }

    // Récupérer toutes les évaluations
    @GetMapping
    public List<Rating> retrieveAllRatings() {
        return ratingService.retrieveAllRatings();
    }

    // Récupérer une évaluation par ID
    @GetMapping("/{id}")
    public Rating retrieveRatingById(@PathVariable Long id) {
        return ratingService.retrieveRatingById(id);
    }

    // Récupérer les évaluations par blog
    @GetMapping("/blog/{blogId}")
    public List<Rating> retrieveRatingsByBlog(@PathVariable Long blogId) {
        return ratingService.retrieveRatingsByBlog(blogId);
    }

    // Récupérer les évaluations par utilisateur
    @GetMapping("/user/{userId}")
    public List<Rating> retrieveRatingsByUser(@PathVariable Long userId) {
        return ratingService.retrieveRatingsByUser(userId);
    }
}
