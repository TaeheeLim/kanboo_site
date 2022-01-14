package com.kanboo.www.domain.repository.board.boardQueryDSL;

import com.kanboo.www.domain.entity.board.ProjectBoard;
import com.kanboo.www.domain.entity.board.QProjectBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class ProjectBoardDSLRepositoryImpl implements ProjectBoardDSLRepository {

    private final EntityManager em;

    @Override
    public List<ProjectBoard> getDashBoardArticle(Long prjctIdx) {

        JPAQueryFactory query = new JPAQueryFactory(em);
        QProjectBoard projectBoard = QProjectBoard.projectBoard;

         return query.selectFrom(projectBoard)
                .where(projectBoard.project.prjctIdx.eq(prjctIdx))
                .limit(6).offset(0)
                .fetch();
    }
}
