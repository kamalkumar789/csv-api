package com.file.csv.Exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
@Builder
public class CommercetoolException {
    String statusCode;
    String message;
    List<Error> errors;

}
