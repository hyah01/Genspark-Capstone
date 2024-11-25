package com.example.photo_service.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class PhotoController {

    @GetMapping("/photo/{filename}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
        try {
            Path file = Paths.get("FrontEnd/amaJohn/public").toAbsolutePath().resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() && resource.isReadable()){
                String content = Files.probeContentType(file);
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,content).body(resource);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
