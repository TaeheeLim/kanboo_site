package com.kanboo.www.domain.repository.project.dslsupport;

import com.kanboo.www.dto.project.GitDTO;

public interface GitDslRepository {
	GitDTO findRepoAddress(Long prjctIdx);
}
