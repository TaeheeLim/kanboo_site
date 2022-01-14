package com.kanboo.www.service.impl.project;

import com.kanboo.www.domain.entity.member.Member;
import com.kanboo.www.domain.entity.project.Issue;
import com.kanboo.www.domain.entity.project.Project;
import com.kanboo.www.domain.repository.member.MemberRepository;
import com.kanboo.www.domain.repository.project.IssueRepository;
import com.kanboo.www.domain.repository.project.ProjectRepository;
import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.dto.project.IssueDTO;
import com.kanboo.www.dto.project.ProjectDTO;
import com.kanboo.www.service.inter.project.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

	private final IssueRepository issueRepository;
	private final ProjectRepository projectRepository;
	private final MemberRepository memberRepository;

	@Override
	public IssueDTO insertIssue(IssueDTO issueDTO) {
		Project project = projectRepository.findByPrjctIdx(issueDTO.getProject().getPrjctIdx());
		Member member = memberRepository.findByMemIdx(issueDTO.getMember().getMemIdx());

		Issue buildIssue = Issue.builder()
				.member(member)
				.project(project)
				.issueCn(issueDTO.getIssueCn())
				.issueDate(issueDTO.getIssueDate())
				.issueState(issueDTO.getIssueState())
				.issueGitFile(issueDTO.getIssueGitFile())
				.build();
		issueRepository.save(buildIssue).entityToDto();

		IssueDTO dto = buildIssue.entityToDto();
		dto.setMember(buildIssue.getMember().entityToDto());
		dto.setProject(buildIssue.getProject().entityToDto());

		return dto;
	}

	@Override
	public List<IssueDTO> IssueHandler(Long projectIdx) {
		List<Issue> issues = issueRepository.findAllByProjectIdxDesc(projectIdx);
		List<IssueDTO> returnIssues = new ArrayList<>();
		for (Issue issue : issues) {
			ProjectDTO buildProject = ProjectDTO.builder()
					.prjctIdx(issue.getProject().getPrjctIdx())
					.prjctComplAt(issue.getProject().getPrjctComplAt())
					.prjctNm(issue.getProject().getPrjctNm())
					.build();
			MemberDTO buildMember = MemberDTO.builder()
					.memIdx(issue.getMember().getMemIdx())
					.memNick(issue.getMember().getMemNick())
					.memTag(issue.getMember().getMemTag())
					.memId(issue.getMember().getMemId())
					.build();
			returnIssues.add(  IssueDTO.builder()
					.issueIdx(issue.getIssueIdx())
					.issueCn(issue.getIssueCn())
					.issueDate(issue.getIssueDate())
					.issueState(issue.getIssueState())
					.issueGitFile(issue.getIssueGitFile())
					.project(buildProject)
					.member(buildMember)
					.build() );
		}
		return returnIssues;
	}

	@Override
	@Transactional
	public IssueDTO updateIssue(IssueDTO issueDTO, String selectedIndex) {
		Issue issue = issueRepository.findByIssueIdx(issueDTO.getIssueIdx());
		issue.changeIssueState(selectedIndex);
		return issue.entityToDto();
	}

	@Override
	public List<IssueDTO> getLastestIssue(Long projectIdx) {
		List<Issue> issueList = issueRepository.findAllByProjectIdxDesc(projectIdx);
		List<IssueDTO> issueDTOS = new ArrayList<>();
		issueList.forEach(item -> {
			issueDTOS.add(item.entityToDto());
		});
		return issueDTOS;
	}

}
