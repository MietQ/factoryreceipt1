package com.factoryreceipt.factoryreceipt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UploadController {


    @Value("${app.base-url}")
    private String baseUrl;

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No file selected");
        }

        try {
            // Używamy absolutnej ścieżki, np. /opt/factoryreceipt/uploads
            String uploadDirPath = "/opt/factoryreceipt/uploads";
            File uploadsDir = new File(uploadDirPath);
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs();
            }

            // Generujemy unikalną nazwę pliku
            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID().toString() + extension;

            // Zapisujemy plik na dysku
            File destination = new File(uploadsDir, newFileName);
            file.transferTo(destination);

            // Budujemy URL – zakładając, że domena to factoryreceipt.com
            String fileUrl = "https://factoryreceipt.com/uploads/" + newFileName;

            // Zwracamy JSON z kluczem "url"
            return ResponseEntity.ok("{\"url\":\"" + fileUrl + "\"}");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving file: " + e.getMessage());
        }
    }

}
