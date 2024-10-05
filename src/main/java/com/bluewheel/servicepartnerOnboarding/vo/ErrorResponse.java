package com.bluewheel.servicepartnerOnboarding.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ErrorResponse  {

    @Serial
    private static final long serialVersionUID = - 6280161636575631840L;
    private String code;
    private String message;
    private List<?> errors;
}
