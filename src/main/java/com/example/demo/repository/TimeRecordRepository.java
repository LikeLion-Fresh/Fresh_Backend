package com.example.demo.repository;


import com.example.demo.model.TimeRecord;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {
    Optional<TimeRecord> findFirstByUserOrderByIdDesc(User user);
    Optional<List<TimeRecord>> findAllByUserOrderByIdDesc(User user);
}
