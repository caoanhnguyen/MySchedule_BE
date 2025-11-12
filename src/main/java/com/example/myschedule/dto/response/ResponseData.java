package com.example.myschedule.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseData<T> {
    final int status;
    final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data;
}
