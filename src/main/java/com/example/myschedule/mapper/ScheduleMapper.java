package com.example.myschedule.mapper;

import com.example.myschedule.dto.request.ScheduleRequestDTO;
import com.example.myschedule.dto.request.UpdateScheduleDTO;
import com.example.myschedule.dto.response.ScheduleResponseDTO;
import com.example.myschedule.entity.Schedule;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ScheduleMapper {

    Schedule toEntity(ScheduleRequestDTO scheduleRequestDTO);

    ScheduleResponseDTO toDto(Schedule schedule);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UpdateScheduleDTO dto, @MappingTarget Schedule entity);
}
