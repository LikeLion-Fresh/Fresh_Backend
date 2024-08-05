package com.example.demo.service;


import com.example.demo.dto.TimeRecordRequestDto;
import com.example.demo.dto.TimeRecordResponseDto;
import com.example.demo.dto.TimeRecordUpdateDto;
import com.example.demo.model.TimeRecord;
import com.example.demo.model.User;
import com.example.demo.repository.TimeRecordRepository;
import com.example.demo.repository.UserRepository;
import com.mysql.cj.exceptions.WrongArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeRecordServiceImpl implements TimeRecordService {
    private final TimeRecordRepository timeRecordRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long createTimeRecord(String username, TimeRecordRequestDto timeRecordRequestDto) throws AccessDeniedException {
        User user = userRepository.findByUsername(username);
        if(timeRecordRequestDto.getType().equals("start")) {
            if(checkIsAlreadyStart(user)) {
                throw new WrongArgumentException("Is already started");
            }
            TimeRecord timeRecord = new TimeRecord();
            timeRecord.setUser(user);
            timeRecordRepository.save(timeRecord);
            return timeRecord.getId();
        } else if (timeRecordRequestDto.getType().equals("end")) {
            TimeRecord timeRecord = timeRecordRepository.findFirstByUserOrderByIdDesc(user).orElse(null);
            if(timeRecord == null) {
                throw new EntityNotFoundException("Entity not found");
            }
            if(timeRecord.getUser() != user && user.getRole() != "ROLE_ADMIN") {
                throw new AccessDeniedException("Access Denied");
            }
            if(timeRecord.getEndedAt() != null) {
                throw new WrongArgumentException("Is already ended");
            }
            timeRecord.setBreakTimes(timeRecordRequestDto.getBreakTimes());
            timeRecord.setEndedAt(LocalDateTime.now());
            timeRecordRepository.save(timeRecord);
            return timeRecord.getId();
        } else {
            throw new WrongArgumentException("Type is wrong");
        }
    }

    @Override
    public TimeRecordResponseDto getTimeRecord(String username, Long id) throws AccessDeniedException {
        User user = userRepository.findByUsername(username);
        TimeRecord timeRecord = timeRecordRepository.getById(id);
        if(timeRecord == null) {
            throw new EntityNotFoundException("Entity not found");
        } else if(timeRecord.getUser() != user && user.getRole() != "ROLE_ADMIN") {
            throw new AccessDeniedException("Access Denied");
        }
        TimeRecordResponseDto result = modelMapper.map(timeRecord, TimeRecordResponseDto.class);
        return result;
    }

    @Override
    public TimeRecordResponseDto getLastTimeRecord(String username) throws AccessDeniedException {
        User user = userRepository.findByUsername(username);
        TimeRecord timeRecord = timeRecordRepository.findFirstByUserOrderByIdDesc(user).orElse(null);
        if(timeRecord == null) {
            throw new EntityNotFoundException("Entity not found");
        } else if(timeRecord.getUser() != user && user.getRole() != "ROLE_ADMIN") {
            throw new AccessDeniedException("Access Denied");
        }
        TimeRecordResponseDto result = modelMapper.map(timeRecord, TimeRecordResponseDto.class);
        return result;
    }

    @Override
    public List<TimeRecordResponseDto> getTimeRecordList(String username, Integer range) {
        User user = userRepository.findByUsername(username);

        List<TimeRecord> timeRecords = timeRecordRepository.findAllByUserOrderByIdDesc(user).orElse(Collections.emptyList());

        LocalDateTime rangeDate = LocalDateTime.now().minusDays(range);

        List<TimeRecordResponseDto> results = timeRecords.stream()
                .filter(record -> record.getStartedAt().isAfter(rangeDate) || record.getStartedAt().isEqual(rangeDate))
                .map(timeRecord -> modelMapper.map(timeRecord, TimeRecordResponseDto.class))
                .collect(Collectors.toList());
        return results;
    }

    @Override
    public Long updateTimeRecord(String username, Long id, TimeRecordUpdateDto timeRecordDto) {
        User user = userRepository.findByUsername(username);
        TimeRecord timeRecord = modelMapper.map(timeRecordDto, TimeRecord.class);
        timeRecord.setId(id);
        timeRecordRepository.save(timeRecord);
        return timeRecord.getId();
    }

    @Override
    public Boolean deleteTimeRecord(String username, Long id) {
        User user = userRepository.findByUsername(username);
        if(timeRecordRepository.getById(id) == null) {
            throw new EntityNotFoundException("Entity not found");
        }
        timeRecordRepository.deleteById(id);
        return true;
    }

    @Override
    public List<TimeRecordResponseDto> getTimeRecordListByDate(String username, String date) {
        log.info(date);
        User user = userRepository.findByUsername(username);

        List<TimeRecord> timeRecords = timeRecordRepository.findAllByUserOrderByIdDesc(user).orElse(Collections.emptyList());

        LocalDateTime targetDate = LocalDateTime.parse(date + "T00:00:00");

        List<TimeRecordResponseDto> results = timeRecords.stream()
                .filter(record -> record.getStartedAt().toLocalDate().isEqual(targetDate.toLocalDate()))
                .map(timeRecord -> modelMapper.map(timeRecord, TimeRecordResponseDto.class))
                .collect(Collectors.toList());
        return results;
    }

    private boolean checkIsAlreadyStart(User user) {
        TimeRecord timeRecord = timeRecordRepository.findFirstByUserOrderByIdDesc(user).orElse(null);
        if(timeRecord == null) {
            return false; // 첫 기록
        }
        if(timeRecord.getEndedAt() == null && timeRecord.getStartedAt() != null) {
            return true;
        }
        return false;
    }
}
