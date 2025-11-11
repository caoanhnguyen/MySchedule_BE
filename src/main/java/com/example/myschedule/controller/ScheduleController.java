package com.example.myschedule.controller;

import com.example.myschedule.service.ScheduleService;
import com.example.myschedule.dto.request.ScheduleRequestDTO;
import com.example.myschedule.dto.request.UpdateScheduleDTO;
import com.example.myschedule.dto.response.ResponseData;
import com.example.myschedule.dto.response.ScheduleResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/schedules")
@Validated
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final MessageSource messageSource;

    public ScheduleController(ScheduleService scheduleService, MessageSource messageSource) {
        this.scheduleService = scheduleService;
        this.messageSource = messageSource;
    }

    @GetMapping("/all")
    public ResponseData<?> getAllSchedules(@Valid @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate scheduleDate, Locale locale) {
        List<ScheduleResponseDTO> schedules = scheduleService.getAllSchedules(scheduleDate);
        return ResponseData.builder()
                .status(200)
                .message(messageSource.getMessage("common.success", null, locale))
                .data(schedules)
                .build();

    }

    @PostMapping("")
    public ResponseData<?> createSchedule(@Valid @RequestBody ScheduleRequestDTO dto, Locale locale) {
        ScheduleResponseDTO createdSchedule = scheduleService.createSchedule(dto);
        return ResponseData.builder()
                .status(201)
                .message(messageSource.getMessage("schedule.create.success", null, locale))
                .data(createdSchedule)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseData<?> updateSchedule(@Min(value = 1, message = "common.id.positive") @PathVariable Long id,
                                          @Valid @RequestBody UpdateScheduleDTO dto, Locale locale) {
        ScheduleResponseDTO updatedSchedule = scheduleService.updateSchedule(id, dto);
        return ResponseData.builder()
                .status(200)
                .message(messageSource.getMessage("schedule.update.success", null, locale))
                .data(updatedSchedule)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseData<?> deleteSchedule(@Min(value = 1, message = "common.id.positive") @PathVariable Long id, Locale locale) {
        scheduleService.deleteSchedule(id);
        return ResponseData.builder()
                .status(200)
                .message(messageSource.getMessage("schedule.delete.success", null, locale))
                .build();
    }
}
