package com.example.demo.service;


import com.example.demo.dto.TimeRecordRequestDto;
import com.example.demo.dto.TimeRecordResponseDto;
import com.example.demo.dto.TimeRecordUpdateDto;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

public interface TimeRecordService {
    Long createTimeRecord(String username, TimeRecordRequestDto timeRecordRequestDto) throws AccessDeniedException;
    TimeRecordResponseDto getTimeRecord(String username, Long id) throws AccessDeniedException;
    TimeRecordResponseDto getLastTimeRecord(String username) throws AccessDeniedException;
    List<TimeRecordResponseDto> getTimeRecordList(String username, Integer range);
    Long updateTimeRecord(String username, Long id, TimeRecordUpdateDto timeRecordDto);
    Boolean deleteTimeRecord(String username, Long id);

    List<TimeRecordResponseDto> getTimeRecordListByDate(String username, String date);
}
