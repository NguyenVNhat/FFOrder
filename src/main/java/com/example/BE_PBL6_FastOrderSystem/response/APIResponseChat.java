package com.example.BE_PBL6_FastOrderSystem.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIResponseChat<T> {
    @JsonProperty("DT")
    private T DT;

    @JsonProperty("EC")
    private int EC;

    @JsonProperty("EM")
    private String EM;
}
