package com.kanboo.www.dto.project;

import com.kanboo.www.domain.entity.project.Project;
import com.kanboo.www.dto.board.BoardDTO;
import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.dto.member.ProjectMemberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Long prjctIdx;
    private String prjctNm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate prjctStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate prjctEndDate;

    private int prjctProgress;
    private String prjctDelAt;
    private String prjctComplAt;
    private String prjctReadMe;
    private String prjctManager;

    private List<ProjectMemberDTO> projectMembers = new ArrayList<>();
    private List<MemberDTO> Members = new ArrayList<>();
    private List<IssueDTO> issueList = new ArrayList<>();
    private List<CalendarDTO> calendarList = new ArrayList<>();
    private GitDTO git;
    private List<BoardDTO> boardList = new ArrayList<>();
    private List<GanttDTO> ganttList = new ArrayList<>();

    public Project dtoToEntity() {
        return Project.builder()
                .prjctIdx(prjctIdx)
                .prjctNm(prjctNm)
                .prjctStartDate(prjctStartDate)
                .prjctEndDate(prjctEndDate)
                .prjctProgress(prjctProgress)
                .prjctDelAt(prjctDelAt)
                .prjctComplAt(prjctComplAt)
                .prjctReadMe(prjctReadMe)
                .prjctManager(prjctManager)
                .build();
    }
}
