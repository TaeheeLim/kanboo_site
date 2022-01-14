package com.kanboo.www.service.inter.project;


import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.dto.member.ProjectMemberDTO;
import com.kanboo.www.dto.project.ProjectDTO;

import java.util.List;

public interface SettingService {

    List<ProjectMemberDTO> getProjectMemberData(Long prjctIdx);

    ProjectDTO getProjectData(Long prjctIdx);

    List<MemberDTO> searchProjectMember(String searchKeyword);

    List<ProjectMemberDTO> addProjectMember(List<ProjectMemberDTO> projectMemberDTOList);

    void updateProject(ProjectDTO projectDTO);

    ProjectMemberDTO checkProjectMember(MemberDTO memberDTO);
}
