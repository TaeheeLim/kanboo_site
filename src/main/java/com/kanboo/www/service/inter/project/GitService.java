package com.kanboo.www.service.inter.project;

import com.kanboo.www.dto.project.GitDTO;

public interface GitService {
    GitDTO getRepoAddress(GitDTO gitDTO);

    void insertRepoAddress(GitDTO gitDTO);
}
