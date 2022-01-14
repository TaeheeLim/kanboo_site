package com.kanboo.www.dto.member.nativedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueNative {

    private Long issueIdx;
    private String issueCn;
    private Date issueDate;
    private String issueState;
    private String issueGitFile;
}
