package com.PIDEV.Blog_Formation_Service.Services;

import com.PIDEV.Blog_Formation_Service.Entities.Rating;
import com.PIDEV.Blog_Formation_Service.Entities.Blog;
import com.PIDEV.Blog_Formation_Service.Entities.User;
import com.PIDEV.Blog_Formation_Service.Repositories.RatingRepository;
import com.PIDEV.Blog_Formation_Service.Repositories.BlogRepository;
import com.PIDEV.Blog_Formation_Service.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService implements IRatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Rating addRating(Rating rating) {
        // Vérifier si le blog et l'utilisateur existent
        Blog blog = blogRepository.findById(rating.getBlog().getId())
                .orElseThrow(() -> new RuntimeException("Blog non trouvé"));
        User user = userRepository.findById(rating.getUser().getIduser())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Ajouter l'évaluation au blog et à l'utilisateur
        rating.setBlog(blog);
        rating.setUser(user);

        return ratingRepository.save(rating);
    }

    @Override
    public Rating updateRating(Rating rating) {
        // Vérifier si le rating existe
        Rating existingRating = ratingRepository.findById(rating.getId())
                .orElseThrow(() -> new RuntimeException("Rating non trouvé"));
        existingRating.setScore(rating.getScore());
        return ratingRepository.save(existingRating);
    }

    @Override
    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }

    @Override
    public List<Rating> retrieveAllRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating retrieveRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating non trouvé"));
    }

    @Override
    public List<Rating> retrieveRatingsByBlog(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog non trouvé"));
        return ratingRepository.findByBlog(blog);
    }

    @Override
    public List<Rating> retrieveRatingsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return ratingRepository.findByUser(user);
    }
}
