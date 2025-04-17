package com.PIDEV.Blog_Formation_Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dx4alpzbh", // Remplacez par votre cloud_name
                "api_key", "453758563137554", // Remplacez par votre api_key
                "api_secret", "GBBfbkB2DQ80G11apZ-UYg4JU3Y" // Remplacez par votre api_secret
        ));
    }
}
