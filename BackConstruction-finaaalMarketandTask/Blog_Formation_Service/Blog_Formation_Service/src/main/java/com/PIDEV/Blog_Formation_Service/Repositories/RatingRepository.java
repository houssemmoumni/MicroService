package com.PIDEV.Blog_Formation_Service.Repositories;

import com.PIDEV.Blog_Formation_Service.Entities.Blog;
import com.PIDEV.Blog_Formation_Service.Entities.Rating;
import com.PIDEV.Blog_Formation_Service.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByBlog(Blog blog);

    List<Rating> findByUser(User user);
}