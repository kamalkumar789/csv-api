package com.file.csv.Exception;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {
    String code;
    String message;
    List<ExtensionError> extensionErrors;
    /*String detail;
    String resourceIdentifier;
    Map<String, String> itemIdentification;*/
}