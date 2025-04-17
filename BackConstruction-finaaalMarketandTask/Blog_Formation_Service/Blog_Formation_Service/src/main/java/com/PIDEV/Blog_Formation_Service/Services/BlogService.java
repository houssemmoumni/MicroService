package com.PIDEV.Blog_Formation_Service.Services;

import com.PIDEV.Blog_Formation_Service.Entities.Blog;
import com.PIDEV.Blog_Formation_Service.Entities.User;
import com.PIDEV.Blog_Formation_Service.Repositories.BlogRepository;
import com.PIDEV.Blog_Formation_Service.Repositories.UserRepository;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class BlogService implements IBlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Blog addBlog(Blog blog, Long userId) {
        // Vérifie si l'utilisateur existe
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Associer l'utilisateur au blog
        blog.setAuthor(author);

        // Si une photo est fournie, upload sur Cloudinary
        if (blog.getPhoto() != null && !blog.getPhoto().isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(blog.getPhoto(), ObjectUtils.emptyMap());
                String photoUrl = (String) uploadResult.get("url");
                blog.setPhoto(photoUrl); // Stocker l'URL de l'image Cloudinary
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de l'upload de l'image");
            }
        }

        // Enregistrer le blog
        return blogRepository.save(blog);
    }


    @Override
    public Blog updateBlog(Blog blog) {



        // Vérifie si le blog existe
        Blog existingBlog = blogRepository.findById(blog.getId())
                .orElseThrow(() -> new RuntimeException("Blog non trouvé"));
        existingBlog.setTitle(blog.getTitle());
        existingBlog.setContent(blog.getContent());
        existingBlog.setPhoto(blog.getPhoto());
        existingBlog.setDate(blog.getDate());
        return blogRepository.save(existingBlog);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> retrieveAllBlogs() {
        return blogRepository.findAll();
    }

    @Override
    public Blog retrieveBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog non trouvé"));
    }

    @Override
    public List<Blog> retrieveBlogsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return blogRepository.findByAuthor(user);
    }
}