package com.example.demo.service;

import com.example.demo.model.Music;
import com.example.demo.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

@Service
public class MusicService {
    @Autowired
    private MusicRepository musicRepository;

    // 업로드된 파일을 저장할 디렉토리 경로
    private final String UPLOAD_DIR = "uploads/";

    // 음악 파일 업로드 메서드
    public Music uploadMusic(MultipartFile file, String title, String artist) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // 업로드 디렉토리가 존재하지 않으면 생성
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일을 업로드 디렉토리에 저장
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        // 파일 경로를 URL 호환 형식으로 변환
        String urlPath = fileName.replace("\\", "/");

        // 음악 정보를 데이터베이스에 저장
        Music music = new Music();
        music.setTitle(title);
        music.setArtist(artist);
        music.setFilePath(urlPath);
        music.setUploadTime(new Timestamp(System.currentTimeMillis()));

        return musicRepository.save(music);
    }

    // 모든 음악 파일을 가져오는 메서드
    public List<Music> getAllMusic() {
        return musicRepository.findAll();
    }
}
