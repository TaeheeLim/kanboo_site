package com.kanboo.www.service.impl.member;

import com.kanboo.www.domain.entity.member.Member;
import com.kanboo.www.domain.entity.member.ProjectMember;
import com.kanboo.www.domain.entity.project.Project;
import com.kanboo.www.domain.repository.member.MemberRepository;
import com.kanboo.www.domain.repository.project.ProjectMemberRepository;
import com.kanboo.www.domain.repository.project.ProjectRepository;
import com.kanboo.www.security.JwtSecurityService;
import com.kanboo.www.service.inter.member.PageRoleCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PageRoleCheckServiceImpl implements PageRoleCheckService {

    private final JwtSecurityService jwtSecurityService;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public Map<String, Object> checkProject(String token, Long projectIdx) {
        String memTag = jwtSecurityService.getToken(token);
        Map<String, Object> resultMap = new HashMap<>();

        if(memTag != null) {
            Member member = memberRepository.findByMemTag(memTag);
            Project project = projectRepository.findByPrjctIdx(projectIdx);
            ProjectMember role = projectMemberRepository.findByProjectPrjctIdxAndMember_MemIdx(project.getPrjctIdx(), member.getMemIdx());
            try {
                resultMap.put("isRole", member.getMemNick() != null && project.getPrjctNm() != null);
                resultMap.put("isPm", role.getPrjctMemRole().contains("PM"));
            } catch (Exception e) {
                return null;
            }
        }

        return resultMap;
    }
}
