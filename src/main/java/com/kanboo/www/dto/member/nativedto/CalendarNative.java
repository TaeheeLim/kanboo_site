package com.kanboo.www.dto.member.nativedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarNative {

    private Long calIdx;
    private Date calStartDate;
    private Date calEndDate;
    private String calColor;
    private String calCn;
    private String calTitle;
    private String calDelAt;
    private String calIsAllDay;
    private String calIsDeletable;
    private String calIsResizable;
}
