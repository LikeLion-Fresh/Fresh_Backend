package com.example.demo.controller;

import com.example.demo.model.Music;
import com.example.demo.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class MusicController {
    @Autowired
    private MusicService musicService;

    // 업로드 폼을 보여주고 현재 업로드된 음악 목록을 모델에 추가...
    @GetMapping("/music")
    public String showUploadForm(Model model) {
        List<Music> musicList = musicService.getAllMusic();
        model.addAttribute("musicList", musicList);
        return "upload";
    }

    // 음악 파일 업로드 처리...
    @PostMapping("/upload")
    public String uploadMusic(@RequestParam("file") MultipartFile file,
                              @RequestParam("title") String title,
                              @RequestParam("artist") String artist,
                              Model model) throws IOException {
        musicService.uploadMusic(file, title, artist);
        return "redirect:/";  // 업로드 후 메인 페이지로 리다이렉트...
    }

    // 업로드된 파일을 제공하는 메서드...
    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
