package com.example.musicupload.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "musics")
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String artist;
    private String filePath;
    private Timestamp uploadTime;

    // 이 클래스는 데이터베이스 테이블 'musics'와 매핑되는 엔터티...
    // JPA가 자동으로 테이블을 생성 및 관리...
}
