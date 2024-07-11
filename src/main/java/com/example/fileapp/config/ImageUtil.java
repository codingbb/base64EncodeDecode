package com.example.fileapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ImageUtil {
    public static String save(MultipartFile profile) {

        String profileFilename = UUID.randomUUID() + "_" + profile.getOriginalFilename();
        Path profilePath = Paths.get("./upload/" + profileFilename);

        try {
            Path uploadPath = Paths.get("./upload/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.write(profilePath, profile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("이미지 오류", e);
        }
        return profileFilename;
    }

}
