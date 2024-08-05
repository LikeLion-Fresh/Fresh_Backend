package com.example.demo.controller;

import com.example.demo.model.Music;
import com.example.demo.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class MusicController {
    @Autowired
    private MusicService musicService;

    // 업로드 폼을 보여주고 현재 업로드된 음악 목록을 모델에 추가...
    @GetMapping("/")
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
}