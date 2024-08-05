package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "시간 측정 반환 DTO")
public class TimeRecordResponseDto {
    private Long id;
    private Integer breakTimes;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
