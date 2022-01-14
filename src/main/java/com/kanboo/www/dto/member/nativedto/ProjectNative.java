package com.kanboo.www.dto.member.nativedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectNative {

    private Long prjctIdx;
    private String prjctNm;
    private Date prjctStartDate;
    private Date prjctEndDate;
    private Long prjctProgress;
    private String prjctDelAt;
    private String prjctComplAt;
    private String prjctReadMe;
    private List<IssueNative> issueList = new ArrayList<>();
    private List<CalendarNative> calendarList = new ArrayList<>();
}
