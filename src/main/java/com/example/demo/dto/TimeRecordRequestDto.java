package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "시간 측정 생성 DTO")
public class TimeRecordRequestDto {
    private String type;
    private Integer breakTimes;
}
