package com.kanboo.www.service.impl.project;

import com.kanboo.www.domain.entity.project.Git;
import com.kanboo.www.domain.entity.project.Project;
import com.kanboo.www.domain.repository.project.GitRepository;
import com.kanboo.www.dto.project.GitDTO;
import com.kanboo.www.service.inter.project.GitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitServiceImpl implements GitService {

    private final GitRepository gitRepository;

    @Override
    public GitDTO getRepoAddress(GitDTO gitDTO) {
        return gitRepository.findRepoAddress(gitDTO.getProject().getPrjctIdx());
    }

    @Override
    public void insertRepoAddress(GitDTO gitDTO) {
        Git git = Git.builder()
                .project(Project.builder().prjctIdx(gitDTO.getProject().getPrjctIdx()).build())
                .gitRepo(gitDTO.getGitRepo())
                .build();
        gitRepository.save(git);
    }
}
