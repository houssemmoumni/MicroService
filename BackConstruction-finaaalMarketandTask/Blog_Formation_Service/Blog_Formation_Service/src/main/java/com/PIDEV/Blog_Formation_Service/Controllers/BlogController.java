package com.PIDEV.Blog_Formation_Service.Controllers;

import com.PIDEV.Blog_Formation_Service.Entities.Blog;
import com.PIDEV.Blog_Formation_Service.Repositories.UserRepository;
import com.PIDEV.Blog_Formation_Service.Services.CloudinaryService;
import com.PIDEV.Blog_Formation_Service.Services.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private IBlogService blogService;


    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    UserRepository userRepository ;




    @PostMapping("/ajouter")
    public ResponseEntity<Blog> ajouterBlog(
            @RequestParam("userId") Long userId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "photo", required = false) MultipartFile photo) { // Recevoir l'image

        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);

        // Upload de l’image sur Cloudinary et stockage de l’URL
        if (photo != null && !photo.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(photo); // ✅ Utilisation correcte
            blog.setPhoto(imageUrl);
        }

        // Sauvegarde du blog
        Blog savedBlog = blogService.addBlog(blog, userId);

        return ResponseEntity.ok(savedBlog);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Blog> updateBlog(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "userId", required = false) Long userId) {

        // Vérifie si le blog existe
        Blog existingBlog = blogService.retrieveBlogById(id);

        // Mise à jour des informations du blog
        existingBlog.setTitle(title);
        existingBlog.setContent(content);

        // Si une nouvelle photo est fournie, uploader et mettre à jour l'URL
        if (photo != null && !photo.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(photo);
            existingBlog.setPhoto(imageUrl);
        }

        // Mise à jour de l'auteur si `userId` est fourni
//        if (userId != null) {
//            User author = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
//            existingBlog.setAuthor(author);
//        }

        // Sauvegarde du blog mis à jour
        Blog updatedBlog = blogService.updateBlog(existingBlog);
        return ResponseEntity.ok(updatedBlog);
    }



    @DeleteMapping("/{id}")
    public void deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
    }

    @GetMapping
    public List<Blog> retrieveAllBlogs() {
        return blogService.retrieveAllBlogs();
    }

    @GetMapping("/{id}")
    public Blog retrieveBlogById(@PathVariable Long id) {
        return blogService.retrieveBlogById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Blog> retrieveBlogsByUser(@PathVariable Long userId) {
        return blogService.retrieveBlogsByUser(userId);
    }
}