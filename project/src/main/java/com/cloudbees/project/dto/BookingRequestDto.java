package com.cloudbees.project.dto;

import com.cloudbees.project.exceptions.CustomException;
import com.cloudbees.project.exceptions.ErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class BookingRequestDto {
    private String from;
    private String to;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("price_paid")
    private Integer pricePaid;

    public void isValid() throws CustomException {
        if(this.from==null || this.from.isBlank()
           || this.to==null || this.to.isBlank()
           || this.userId==null
           || this.pricePaid==null) throw new CustomException(ErrorCode.INVALID_REQUEST_BODY);

        if(!this.from.equals("London") || !this.to.equals("France") || this.pricePaid!=20)
            throw new CustomException(ErrorCode.INVALID_REQUEST_BODY);
    }
}
