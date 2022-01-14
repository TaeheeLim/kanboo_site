package com.kanboo.www.domain.repository.project.dslsupport;

import com.kanboo.www.domain.entity.member.*;
import com.kanboo.www.domain.entity.project.*;
import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.dto.member.ProjectMemberDTO;
import com.kanboo.www.dto.project.CalendarDTO;
import com.kanboo.www.dto.project.IssueDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProjectMemberDslRepositoryImpl implements ProjectMemberDslRepository {

    private final EntityManager em;

    @Override
    public List<ProjectMemberDTO> getAllList(String memTag) {
        QMember member = QMember.member;
        QProject project = QProject.project;
        QProjectMember projectMember = QProjectMember.projectMember;
        QIssue issue = QIssue.issue;
        QCalendar calendar = QCalendar.calendar;

        JPAQueryFactory query = new JPAQueryFactory(em);


        List<ProjectMemberDTO> resultList = new ArrayList<>();
        List<ProjectMember> projectMembers = query
                .select(projectMember).distinct()
                .from(projectMember)
                .innerJoin(projectMember.member, member)
                .innerJoin(projectMember.project, project)
                .leftJoin(project.issueList, issue)
                .leftJoin(project.calendarList, calendar)
                .where(
                        projectMember.member.memTag.eq(memTag)
                                .and(project.prjctDelAt.eq("n"))
                )
                .fetch();

        projectMembers.forEach(item -> {
            ProjectMemberDTO projectMemberDTO = item.entityToDto();
            List<Issue> issueList = item.getProject().getIssueList();
            List<IssueDTO> issueDTOS = new ArrayList<>();
            int issueCnt = 0;
            for(Issue iss : issueList) {
                if(issueCnt > 3) {
                    break;
                }
                issueDTOS.add(iss.entityToDto());
                issueCnt++;
            }

            List<Calendar> calendarList = item.getProject().getCalendarList();
            List<CalendarDTO> calendarDTOS = new ArrayList<>();
            int calendarCnt = 0;
            for(Calendar cal : calendarList) {
                if(calendarCnt > 2) {
                    break;
                }
                calendarDTOS.add(cal.entityToDto());
                calendarCnt++;
            }

            projectMemberDTO.getProject().setIssueList(issueDTOS);
            projectMemberDTO.getProject().setCalendarList(calendarDTOS);

            resultList.add(projectMemberDTO);
        });

        if(resultList.isEmpty()) {
            Member findMember = query.select(member)
                    .from(member)
                    .where(member.memTag.eq(memTag))
                    .fetchOne();

            MemberDTO build = MemberDTO.builder()
                    .memImg(findMember.getMemImg())
                    .memNick(findMember.getMemNick())
                    .build();

            ProjectMemberDTO buildpm = ProjectMemberDTO.builder()
                    .member(build)
                    .build();

            resultList.add(buildpm);
        }

        return resultList;
    }

    @Override
    public List<ProjectMember> findAllByMemIdx(Long memIdx) {
        QProjectMember qProjectMember = QProjectMember.projectMember;
        QProject qproject = QProject.project;
        QMember qmember = QMember.member;
        QBan qBan = QBan.ban;

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(qProjectMember)
                .from(qProjectMember)
                .rightJoin(qProjectMember.member, qmember)
                .fetchJoin()
                .leftJoin(qmember.ban, qBan)
                .fetchJoin()
                .innerJoin(qProjectMember.project, qproject)
                .fetchJoin()
                .where(qProjectMember.member.memIdx.eq(memIdx))
                .fetch();
    }

    @Override
    public List<ProjectMember> getAllProjectMemberList(String selected, String key, int articleOnView) {

        JPAQueryFactory query = new JPAQueryFactory(em);

        QProjectMember qProjectMember = QProjectMember.projectMember;
        QProject qProject = QProject.project;
        QMember qMember = QMember.member;
        QBan qBan = QBan.ban;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        switch (selected) {
            case "prjctNm":
                booleanBuilder.and(qProject.prjctNm.contains(key));
                break;
            case "memId":
                booleanBuilder.and(qProjectMember.member.memNick.contains(key));
                break;
            case "All":
                booleanBuilder.or(qProject.prjctNm.contains(key)).or(qProjectMember.member.memId.contains(key));
                break;
        }

        return query
                .selectFrom(qProjectMember)
                .innerJoin(qProjectMember.project, qProject)
                .fetchJoin()
                .innerJoin(qProjectMember.member, qMember)
                .fetchJoin()
                .leftJoin(qMember.ban, qBan)
                .fetchJoin()
                .where(
                        qProjectMember.prjctMemRole.eq("PM")
                                .and(booleanBuilder)
                )
                .orderBy(qProject.prjctIdx.desc())
                .offset(articleOnView)
                .limit(10)
                .fetch();
    }

    @Override
    public List<ProjectMember> getAllProjectMember(Long prjctIdx) {

        QProjectMember qProjectMember = QProjectMember.projectMember;
        QMember qMember = QMember.member;
        QProject qProject = QProject.project;

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(qProjectMember)
                .from(qMember, qProjectMember)
                .where(qMember.memIdx.eq(qProjectMember.member.memIdx).and(
                        qProjectMember.project.prjctIdx.eq(prjctIdx)

                )).fetch();

    }

    @Override
    public void insertProjectMember(ProjectMember projectMember) {
        QProjectMember qProjectMember = QProjectMember.projectMember;
        QMember qMember = QMember.member;
        QProject qProject = QProject.project;

        JPAQueryFactory query = new JPAQueryFactory(em);

        query.insert(qProjectMember).
                columns(qMember.memIdx, qProject.prjctIdx, qProjectMember.prjctMemRole,
                        qProjectMember).execute();

    }

}
