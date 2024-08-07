package com.example.demo.controller;


import com.example.demo.dto.TimeRecordRequestDto;
import com.example.demo.dto.TimeRecordResponseDto;
import com.example.demo.dto.TimeRecordUpdateDto;
import com.example.demo.service.TimeRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Api(value = "시간 측정 컨트롤러")
@Slf4j
public class TimeRecordController {
    private final TimeRecordService timeRecordService;

    @GetMapping("/timerecords")
    @ApiOperation(value = "시간 측정 기록 목록 조회", notes = "범위를 지정하여 범위내 기록들을 조회합니다.")
    public ResponseEntity<List<TimeRecordResponseDto>> findTimeRecords(@RequestParam(name = "range", required = true) Integer range,
                                                                       Principal principal) {
        if(principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
//        String username = "test";
        List<TimeRecordResponseDto> results = timeRecordService.getTimeRecordList(username, range);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/timerecords/date")
    @ApiOperation(value = "시간 측정 기록 조회", notes = "날짜를 이용해 시간 측정 기록을 조회합니다.")
    public ResponseEntity<List<TimeRecordResponseDto>> findTimeRecordsByDate(@RequestParam String date,
                                                                             Principal principal) {
        if(principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
//            String username = "test";
            List<TimeRecordResponseDto> results = timeRecordService.getTimeRecordListByDate(username, date);
            return ResponseEntity.ok(results);
    }

    @PostMapping("/timerecords")
    @ApiOperation(value = "시간 측정 기록 생성", notes = "시간 측정 기록을 생성합니다.")
    public ResponseEntity<Long> createTimeRecord(@RequestBody TimeRecordRequestDto timeRecordRequestDto,
                                                 Principal principal) throws AccessDeniedException, AccessDeniedException {
        if(principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
//        String username = "test";
        Long result = timeRecordService.createTimeRecord(username, timeRecordRequestDto);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/timerecords/{id}")
    @ApiOperation(value = "시간 측정 기록 조회", notes = "id를 활용해 시간 측정 기록을 조회합니다.")
    public ResponseEntity<TimeRecordResponseDto> findTimeRecord(@PathVariable(name = "id", required = true) Long id,
                                                                Principal principal) throws AccessDeniedException {
        if(principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        TimeRecordResponseDto result = timeRecordService.getTimeRecord(username, id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/timerecords/{id}")
    @ApiOperation(value = "시간 측정 기록 변경", notes = "id와 DTO를 활용해서 시간 측정 기록을 변경합니다.")
    public ResponseEntity<Long> updateTimeRecord(@PathVariable(name = "id", required = true) Long id,
                                                 @RequestBody(required = true) TimeRecordUpdateDto timeRecordUpdateDto,
                                                 Principal principal) {
        if(principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        Long result = timeRecordService.updateTimeRecord(username, id, timeRecordUpdateDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/timerecords/{id}")
    @ApiOperation(value = "시간 측정 기록 삭제", notes = "id를 활용해서 시간 측정 기록을 삭제합니다.")
    public ResponseEntity<Boolean> deleteTimeRecord(@PathVariable(name = "id", required = true) Long id,
                                                    Principal principal) {
        if(principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        Boolean result = timeRecordService.deleteTimeRecord(username, id);
        return ResponseEntity.ok(result);
    }
}
