package com.example.myschedule.service;

import com.example.myschedule.dto.request.ScheduleRequestDTO;
import com.example.myschedule.dto.request.UpdateScheduleDTO;
import com.example.myschedule.dto.response.ScheduleResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    List<ScheduleResponseDTO> getAllSchedules(LocalDate scheduleDate);

    ScheduleResponseDTO createSchedule(ScheduleRequestDTO scheduleRequestDTO);

    ScheduleResponseDTO updateSchedule(Long id, UpdateScheduleDTO dto);

    void deleteSchedule(Long scheduleId);
}
