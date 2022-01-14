package com.kanboo.www.domain.repository.project.dslsupport;

import com.kanboo.www.domain.entity.project.Git;
import com.kanboo.www.domain.entity.project.QGit;
import com.kanboo.www.domain.entity.project.QProject;
import com.kanboo.www.dto.project.GitDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class GitDslRepositoryImpl implements GitDslRepository{

	private final EntityManager em;

	@Override
	public GitDTO findRepoAddress(Long prjctIdx) {
		QGit qGit = QGit.git;
		QProject qProject = QProject.project;

		JPAQueryFactory query = new JPAQueryFactory(em);
		Git git = query.selectFrom(qGit)
				.rightJoin(qGit.project, qProject)
				.fetchJoin()
				.where(qGit.project.prjctIdx.eq(prjctIdx))
				.fetchOne();
		return git.entityToDto();
	}
}
