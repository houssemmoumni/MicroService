package com.PIDEV.Blog_Formation_Service.Services;

import com.PIDEV.Blog_Formation_Service.Entities.Blog;

import java.util.List;

public interface IBlogService {

     Blog addBlog(Blog blog, Long userId);


    Blog updateBlog(Blog blog);


    void deleteBlog(Long id);


    List<Blog> retrieveAllBlogs();


    Blog retrieveBlogById(Long id);


    List<Blog> retrieveBlogsByUser(Long userId);
}