package com.example.myschedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateScheduleDTO {

    @NotBlank(message = "{schedule.task.notblank}")
    @Size(max = 100, message = "{schedule.task.size}")
    String task;

    @Size(max = 200, message = "{schedule.note.size}")
    String note;

    @NotNull(message = "{schedule.startTime.notnull}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @JsonProperty("start_time")
    String startTime;

    @NotNull(message = "{schedule.endTime.notnull}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @JsonProperty("end_time")
    String endTime;
}
