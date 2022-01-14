package com.kanboo.www.service.impl.project;

import com.kanboo.www.domain.entity.global.Role;
import com.kanboo.www.domain.entity.member.Member;
import com.kanboo.www.domain.entity.member.ProjectMember;
import com.kanboo.www.domain.entity.project.Chat;
import com.kanboo.www.domain.entity.project.Project;
import com.kanboo.www.domain.repository.member.MemberRepository;
import com.kanboo.www.domain.repository.project.ChattingRepository;
import com.kanboo.www.domain.repository.project.ProjectMemberRepository;
import com.kanboo.www.domain.repository.project.ProjectRepository;
import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.dto.member.ProjectMemberDTO;
import com.kanboo.www.dto.project.ProjectDTO;
import com.kanboo.www.service.inter.project.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;
    private final ChattingRepository chattingRepository;

    @Override
    public List<ProjectMemberDTO> getProjectMemberData(Long prjctIdx) {
        Project project = projectRepository.findByPrjctIdx(prjctIdx);

        List<ProjectMember> projectMemberList = projectMemberRepository.getAllProjectMember(prjctIdx);
        List<ProjectMemberDTO> projectMemberDTOList = new ArrayList<>();


        for (ProjectMember projectMember : projectMemberList) {
            Member member = memberRepository.findByMemIdx(projectMember.getMember().getMemIdx());
            ProjectMember buildProjectMember = ProjectMember.builder()
                    .project(project)
                    .member(member)
                    .prjctMemRole(projectMember.getPrjctMemRole())
                    .build();
            projectMemberDTOList.add(buildProjectMember.entityToDto());
        }
        return projectMemberDTOList;
    }

    @Override
    public ProjectDTO getProjectData(Long prjctIdx) {
        Project project = projectRepository.findByPrjctIdx(prjctIdx);

        return project.entityToDto();
    }

    @Override
    public List<MemberDTO> searchProjectMember(String searchKeyword) {
        List<Member> memberList = memberRepository.searchMemberByKeyword(searchKeyword);
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(Member member : memberList){
            memberDTOList.add(member.entityToDto());
        }
        return memberDTOList;
    }

    @Override
    public List<ProjectMemberDTO> addProjectMember(List<ProjectMemberDTO> projectMemberDTOList) {
        List<ProjectMemberDTO> resultList = new ArrayList<>();
        Project project = projectRepository.findByPrjctIdx(projectMemberDTOList.get(0).getProject().getPrjctIdx());
        projectMemberDTOList.forEach(mem -> {
            Member member = memberRepository.findByMemIdx(mem.getMember().getMemIdx());
            ProjectMember pm = ProjectMember.builder()
                    .member(member)
                    .project(project)
                    .prjctMemRole(mem.getPrjctMemRole())
                    .build();
            ProjectMember save = projectMemberRepository.save(pm);

            MemberDTO memberDto = MemberDTO.builder()
                    .memId(member.getMemId())
                    .memIdx(member.getMemIdx())
                    .memNick(member.getMemNick())
                    .memTag(member.getMemTag())
                    .memImg(member.getMemImg())
                    .build();

            ProjectDTO projectDto = ProjectDTO.builder()
                    .prjctIdx(project.getPrjctIdx())
                    .prjctNm(project.getPrjctNm())
                    .prjctStartDate(project.getPrjctStartDate())
                    .prjctEndDate(project.getPrjctEndDate())
                    .prjctProgress(project.getPrjctProgress())
                    .prjctDelAt(project.getPrjctDelAt())
                    .prjctComplAt(project.getPrjctComplAt())
                    .prjctReadMe(project.getPrjctReadMe())
                    .build();

            ProjectMemberDTO pmDto = ProjectMemberDTO.builder()
                    .member(memberDto)
                    .project(projectDto)
                    .prjctMemRole(save.getPrjctMemRole())
                    .build();

            Chat chat = Chat.builder()
                    .member(member)
                    .project(project)
                    .build();

            chattingRepository.save(chat);

            resultList.add(pmDto);
        });

        return resultList;
    }

    @Override
    public void updateProject(ProjectDTO projectDTO) {
        Project project = projectDTO.dtoToEntity();

        projectRepository.save(project);
    }

    @Override
    public ProjectMemberDTO checkProjectMember(MemberDTO memberDTO) {
        Member member = memberRepository.findByMemIdx(memberDTO.getMemIdx());



        return null;
    }


}
