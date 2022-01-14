package com.kanboo.www.controller.access;

import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.dto.project.GitDTO;
import com.kanboo.www.dto.project.IssueDTO;
import com.kanboo.www.dto.project.ProjectDTO;
import com.kanboo.www.security.JwtSecurityService;
import com.kanboo.www.service.inter.member.MemberService;
import com.kanboo.www.service.inter.project.GitService;
import com.kanboo.www.service.inter.project.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gitAndIssue")
public class GitAndIssueController {

	private final JwtSecurityService jwtSecurityService;
	private final IssueService issueService;
	private final GitService gitService;
	private final MemberService memberService;

	@PostMapping(value="/insert")
	public IssueDTO insertIssue(IssueDTO issueDTO){
		return issueService.insertIssue(issueDTO);
	}

	@GetMapping(value = "/getAllList")
	public List<IssueDTO> IssueHandler(ProjectDTO project){
		return issueService.IssueHandler(project.getPrjctIdx());
	}

	@PostMapping(value = "/updateIssue")
	public IssueDTO updateIssue(IssueDTO issueDTO, String selectedIndex){
		return	issueService.updateIssue(issueDTO, selectedIndex);
	}

	@PostMapping("/getInfo")
	public MemberDTO getToken(String token){
		String decodedToken = jwtSecurityService.getToken(token);
		return memberService.getUserInfo(decodedToken);
	}

	@PostMapping("/getAdd")
	public Map<String, Object> getKey(GitDTO gitDTO){
		GitDTO repoAddress = gitService.getRepoAddress(gitDTO);
		Map<String, Object> map = new HashMap<>();
		map.put("address", repoAddress.getGitRepo());
		return map;
	}

	@PostMapping("/insertRepo")
	public void insertRepo(GitDTO gitDTO){
		System.out.println(gitDTO);
		gitService.insertRepoAddress(gitDTO);
	}
}
