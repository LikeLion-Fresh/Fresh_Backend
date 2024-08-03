package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "시간 측정 업데이트 DTO")
public class TimeRecordUpdateDto {
    private Long id;
    private Float breakTimes;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}