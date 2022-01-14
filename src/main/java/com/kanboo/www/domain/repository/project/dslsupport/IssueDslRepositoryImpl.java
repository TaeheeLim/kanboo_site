package com.kanboo.www.domain.repository.project.dslsupport;

import com.kanboo.www.domain.entity.member.QBan;
import com.kanboo.www.domain.entity.member.QMember;
import com.kanboo.www.domain.entity.project.Issue;
import com.kanboo.www.domain.entity.project.QIssue;
import com.kanboo.www.domain.entity.project.QProject;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class IssueDslRepositoryImpl implements IssueDslRepository {

	private final EntityManager em;

	@Override
	public List<Issue> findAllByProjectIdxDesc(Long projectIdx) {
		QIssue qIssue = QIssue.issue;
		QProject qProject = QProject.project;
		QMember qMember = QMember.member;
		QBan qBan = QBan.ban;

		JPAQueryFactory query = new JPAQueryFactory(em);

		return query.selectFrom(qIssue)
				.rightJoin(qIssue.project, qProject)
				.fetchJoin()
				.rightJoin(qIssue.member, qMember)
				.fetchJoin()
				.leftJoin(qMember.ban, qBan)
				.fetchJoin()
				.where(qIssue.project.prjctIdx.eq(projectIdx))
				.orderBy(qIssue.issueDate.desc())
				.fetch();
	}

	@Override
	public List<Issue> getDashBoardIssue(Long prjctIdx) {

		QIssue issue = QIssue.issue;
		QProject project = QProject.project;

		JPAQueryFactory query = new JPAQueryFactory(em);
		return query.selectFrom(issue)
				.rightJoin(issue.project, project)
				.fetchJoin()
				.where(
						issue.project.prjctIdx.eq(prjctIdx)
				)
				.limit(6).offset(0)
				.fetch();
	}


}
