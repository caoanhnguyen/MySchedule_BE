package com.example.myschedule.repository;

import com.example.myschedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s.id, s.task, s.note, s.scheduleDate, s.startTime, s.endTime " +
            "FROM Schedule s " +
            "WHERE s.scheduleDate = :scheduleDate " +
            "ORDER BY s.startTime ASC")
    List<Object[]> findAllByScheduleDate(@Param("scheduleDate") LocalDate scheduleDate);
}
