package com.kanboo.www.controller.access;

import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.dto.member.ProjectMemberDTO;
import com.kanboo.www.dto.project.ProjectDTO;
import com.kanboo.www.security.JwtSecurityService;
import com.kanboo.www.service.inter.member.MemberService;
import com.kanboo.www.service.inter.member.ProjectMemberService;
import com.kanboo.www.service.inter.project.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor

@RequestMapping("/setting")
public class SettingController {

    private final SettingService settingService;
    private final JwtSecurityService jwtSecurityService;
    private final MemberService memberService;
    private final ProjectMemberService projectMemberService;

    @PostMapping("/loadProjectMember")
    public List<ProjectMemberDTO> getProjectMemberData(@RequestBody Map<String, String> map){
        String prjctIdx = map.get("prjctIdx");
        Long longPrjctIdx = Long.parseLong(prjctIdx);
        return settingService.getProjectMemberData(longPrjctIdx);
    }

    @PostMapping("/loadProject")
    public ProjectDTO getProjectData(@RequestBody Map<String, String> map){
        String prjctIdx = map.get("prjctIdx");
        Long longPrjctIdx = Long.parseLong(prjctIdx);
        System.out.println(longPrjctIdx);
        return settingService.getProjectData(longPrjctIdx);
    }

    @PostMapping("/searchProjectMember")
    public List<MemberDTO> searchProjectMember(@RequestBody Map<String, String> map){
        String searchKeyword = map.get("searchKeyword");

        return settingService.searchProjectMember(searchKeyword);
    }

    @PostMapping("/addProjectMember")
    public Object addProjectMember(@RequestBody Map<String, List<ProjectMemberDTO>> map){
        List<ProjectMemberDTO> projectMemberDTOList = map.get("params");
        return settingService.addProjectMember(projectMemberDTOList);
    }

    @PostMapping("/changeProjectDate")
    public void changeProjectDate(@RequestBody Map<String, ProjectDTO> map){
        ProjectDTO projectDTO = map.get("params");
        settingService.updateProject(projectDTO);
    }

    @PostMapping("/modifyProject")
    public void modifyProject(@RequestBody Map<String, ProjectDTO> map){
        ProjectDTO projectDTO = map.get("params");
        settingService.updateProject(projectDTO);
    }

    @PostMapping("/modifyProjectMember")
    public void modifyProjectMember(@RequestBody Map<String, List<ProjectMemberDTO>> map){
        List<ProjectMemberDTO> projectMemberDTOList = map.get("params");
        settingService.addProjectMember(projectMemberDTOList);
    }
}
