package com.kanboo.www.dto.project;

import com.kanboo.www.domain.entity.project.Issue;
import com.kanboo.www.dto.member.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDTO {

    private Long issueIdx;
    private ProjectDTO project;
    private MemberDTO member;
    private String issueCn;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issueDate;
    private String issueState;
    private String issueGitFile;

    public Issue dtoToEntity() {
        return Issue.builder()
                .issueIdx(issueIdx)
                .project(project.dtoToEntity())
                .member(member.dtoToEntity())
                .issueCn(issueCn)
                .issueDate(issueDate)
                .issueState(issueState)
                .issueGitFile(issueGitFile)
                .build();
    }

}
