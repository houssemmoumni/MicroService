package com.megaminds.material.service;

import com.megaminds.material.dto.ImageModel;
import com.megaminds.material.entity.Image;
import com.megaminds.material.entity.Material;
import com.megaminds.material.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Service
@AllArgsConstructor
public class ImageService {
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    public ResponseEntity<Map> uploadImage( Material material,ImageModel imageModel) {
        try {
            if (imageModel.getName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (imageModel.getFile().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Image image = new Image();
            image.setName(imageModel.getName());
            image.setMaterial(material);
            image.setUrl(cloudinaryService.uploadFile(imageModel.getFile(), "folder_1"));
            if(image.getUrl() == null) {
                return ResponseEntity.badRequest().build();
            }

            imageRepository.save(image);
            return ResponseEntity.ok().body(Map.of("url", image.getUrl()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
