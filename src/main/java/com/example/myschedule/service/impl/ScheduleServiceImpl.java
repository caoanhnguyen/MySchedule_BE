package com.example.myschedule.service.impl;

import com.example.myschedule.exception.InvalidRangeTimeException;
import com.example.myschedule.exception.OverlapRangeTimeException;
import com.example.myschedule.repository.ScheduleRepository;
import com.example.myschedule.service.ScheduleService;
import com.example.myschedule.dto.request.ScheduleRequestDTO;
import com.example.myschedule.dto.request.UpdateScheduleDTO;
import com.example.myschedule.dto.response.ScheduleResponseDTO;
import com.example.myschedule.entity.Schedule;
import com.example.myschedule.exception.ResourceNotFoundException;
import com.example.myschedule.mapper.ScheduleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleMapper = scheduleMapper;
    }

    @Override
    public List<ScheduleResponseDTO> getAllSchedules(LocalDate scheduleDate) {
        if(scheduleDate==null) {
            throw new ResourceNotFoundException("schedule.scheduleDate.notfound");
        }
        return getSchedulesByDate(scheduleDate);
    }

    @Override
    @Transactional
    public ScheduleResponseDTO createSchedule(ScheduleRequestDTO scheduleRequestDTO) {

        if(!isValidTimeRange(scheduleRequestDTO.getStartTime(), scheduleRequestDTO.getEndTime())) {
            throw new InvalidRangeTimeException("schedule.invalid.time.range");
        }

        List<ScheduleResponseDTO> existingSchedules = getSchedulesByDate(scheduleRequestDTO.getScheduleDate());
        if (hasOverlap(existingSchedules, scheduleRequestDTO.getStartTime(), scheduleRequestDTO.getEndTime(), null)) {
            throw new OverlapRangeTimeException("schedule.time.overlap");
        }

        Schedule schedule = scheduleMapper.toEntity(scheduleRequestDTO);
        schedule = scheduleRepository.save(schedule);

        return scheduleMapper.toDto(schedule);
    }

    @Override
    @Transactional
    public ScheduleResponseDTO updateSchedule(Long id, UpdateScheduleDTO dto) {
        Schedule existingSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("schedule.notfound"));

        if(!isValidTimeRange(LocalTime.parse(dto.getStartTime()), LocalTime.parse(dto.getEndTime()))) {
            throw new InvalidRangeTimeException("schedule.invalid.time.range");
        }

        List<ScheduleResponseDTO> existingSchedules = getSchedulesByDate(existingSchedule.getScheduleDate());
        if (hasOverlap(existingSchedules, LocalTime.parse(dto.getStartTime()), LocalTime.parse(dto.getEndTime()), id)) {
            throw new OverlapRangeTimeException("schedule.time.overlap");
        }

        scheduleMapper.updateEntityFromDto(dto, existingSchedule);
        existingSchedule = scheduleRepository.save(existingSchedule);

        return scheduleMapper.toDto(existingSchedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new ResourceNotFoundException("schedule.notfound"));

        scheduleRepository.delete(schedule);
    }

    private List<ScheduleResponseDTO> getSchedulesByDate(LocalDate scheduleDate) {
        List<Object[]> schedules = scheduleRepository.findAllByScheduleDate(scheduleDate);
        if (schedules.isEmpty()) {
            return List.of();
        }
        return schedules.stream().map(record -> {
            ScheduleResponseDTO dto = new ScheduleResponseDTO();
            dto.setId(record[0] != null ? (Long) record[0] : null);
            dto.setTask(record[1] != null ? (String) record[1] : null);
            dto.setNote(record[2] != null ? (String) record[2] : null);
            dto.setScheduleDate(record[3] != null ? (java.time.LocalDate) record[3] : null);
            dto.setStartTime(record[4] != null ? (java.time.LocalTime) record[4] : null);
            dto.setEndTime(record[5] != null ? (java.time.LocalTime) record[5] : null);
            return dto;
        }).toList();
    }

    private boolean isOverlap(LocalTime s1, LocalTime e1, LocalTime s2, LocalTime e2) {
        return s1.isBefore(e2) && s2.isBefore(e1);
    }

    // Kiểm tra overlap cho lịch mới hoặc cập nhật
    private boolean hasOverlap(List<ScheduleResponseDTO> schedules, LocalTime startTime, LocalTime endTime, Long excludeId) {
        for (ScheduleResponseDTO s : schedules) {
            // Với update, có thể bỏ qua chính lịch đang update
            if (s.getId().equals(excludeId)) continue;
            if (isOverlap(s.getStartTime(), s.getEndTime(), startTime, endTime)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidTimeRange(LocalTime startTime, LocalTime endTime) {
        return startTime.isBefore(endTime);
    }
}
