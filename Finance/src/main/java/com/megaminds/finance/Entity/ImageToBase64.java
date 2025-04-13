package com.megaminds.finance.Entity;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageToBase64 {
    public static void main(String[] args) throws Exception {
        // Path to your signature image
        String imagePath = "C:\\Users\\asust\\Downloads\\mega1.png"; // Make sure the file has .png extension

          // Read image bytes
        byte[] fileContent = Files.readAllBytes(Paths.get(imagePath));

        // Convert to Base64
        String base64String = Base64.getEncoder().encodeToString(fileContent);

        // Print base64 string with image type (e.g., PNG)
        System.out.println("data:image/png;base64," + base64String);
    }
}
