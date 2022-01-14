package com.kanboo.www.domain.repository.board.boardQueryDSL;

import com.kanboo.www.domain.entity.board.*;
import com.kanboo.www.domain.entity.global.QCodeDetail;
import com.kanboo.www.domain.entity.member.QMember;
import com.kanboo.www.dto.board.BoardDTO;
import com.kanboo.www.dto.board.CommentDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BoardDSLRepositoryImpl implements BoardDSLRepository{

    private final EntityManager em;
    private static final Logger logger
            = LoggerFactory.getLogger(BoardDSLRepositoryImpl.class);

    @Override
    public List<BoardDTO> getAllList(String selected, String key, int articleOnvView, String codeDetails, long memId) {
        QBoard board = QBoard.board;
        QLikes likes = QLikes.likes;
        QBoardReport reports = QBoardReport.boardReport;
        QMember member = QMember.member;
        QCodeDetail codeDetail = QCodeDetail.codeDetail;
        QBoardFile boardFile = QBoardFile.boardFile;

        JPAQueryFactory query = new JPAQueryFactory(em);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(selected.equals("memNick")) {
            booleanBuilder.and(
                    board.member.memNick.contains(key)
            );
        } else if(selected.equals("boardCN")) {
            booleanBuilder.and(
                    board.boardCn.contains(key)
            );
        } else if(selected.equals("All")){
            booleanBuilder.or(board.member.memNick.contains(key))
                    .or(board.boardCn.contains(key));
        }

        if("8".equals(codeDetails)) {
            booleanBuilder.and(
                    member.memIdx.eq(memId)
            );
        }

        List<Board> boardList = query
                .select(board)
                .from(board)
                .innerJoin(board.member, member)
                .fetchJoin()
                .innerJoin(board.codeDetail, codeDetail)
                .fetchJoin()
                .leftJoin(board.boardFile, boardFile)
                .fetchJoin()
                .where(
                        (JPAExpressions
                                .select(reports.count())
                                .from(reports)
                                .where(reports.board.boardIdx.eq(board.boardIdx))
                        ).lt(5L)
                                .and(board.delAt.eq("N"))
                                .and(board.codeDetail.codeDetailIdx.eq(codeDetails))
                                .and(booleanBuilder)
                )
                .distinct()
                .offset(articleOnvView).limit(10)
                .orderBy(board.boardIdx.desc())
                .fetch();

        List<BoardDTO> resultList = new ArrayList<>();

        boardList.forEach(item -> {
            BoardDTO boardDTO = BoardDTO.builder()
                    .boardIdx(item.getBoardIdx())
                    .member(item.getMember().entityToDto())
                    .boardCn(item.getBoardCn())
                    .boardDate(item.getBoardDate())
                    .delAt(item.getDelAt())
                    .codeDetail(item.getCodeDetail().entityToDto())
                    .fileAt(item.getFileAt())
                    .totalComments(item.getTotalComments())
                    .totalLikes(item.getTotalLikes())
                    .boardFileDTO(item.getBoardFile() == null ? null : item.getBoardFile().entityToDto())
                    .build();

            item.getLikesList().forEach(l -> {
                if(l.getMember().getMemIdx().equals(memId)) {
                    boardDTO.setLike(true);
                }
            });
            item.getReportsList().forEach(r -> {
                if(r.getMember().getMemIdx().equals(memId)) {
                    boardDTO.setReport(true);
                }
            });
            resultList.add(boardDTO);
        });
        return resultList;
    }
    //프로젝트 게시물
    @Override
    public List<BoardDTO> getAllProjectList(String selected, String key, int articleOnvView, String codeDetails, long memId, long projectIdx) {
        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QCodeDetail codeDetail = QCodeDetail.codeDetail;
        QBoardFile boardFile = QBoardFile.boardFile;
        QProjectBoard projectBoard = QProjectBoard.projectBoard;

        JPAQueryFactory query = new JPAQueryFactory(em);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(selected.equals("memNick")) {
            booleanBuilder.and(
                    board.member.memNick.contains(key)
            );
        } else if(selected.equals("boardCN")) {
            booleanBuilder.and(
                    board.boardCn.contains(key)
            );
        } else if(selected.equals("All")){
            booleanBuilder.or(board.member.memNick.contains(key))
                    .or(board.boardCn.contains(key));
        }

        List<Board> boardList = query
                .select(board)
                .from(board, projectBoard)
                .innerJoin(board.member, member)
                .fetchJoin()
                .innerJoin(board.codeDetail, codeDetail)
                .fetchJoin()
                .leftJoin(board.boardFile, boardFile)
                .fetchJoin()
                .where(projectBoard.project.prjctIdx.eq(projectIdx)
                                .and(board.boardIdx.eq(projectBoard.board.boardIdx))
                                .and(board.delAt.eq("N"))
                                .and(board.codeDetail.codeDetailIdx.eq(codeDetails))
                                .and(booleanBuilder)
                )
                .distinct()
                .offset(articleOnvView).limit(10)
                .orderBy(board.boardIdx.desc())
                .fetch();

        List<BoardDTO> resultList = new ArrayList<>();

        boardList.forEach(item -> {
            BoardDTO boardDTO = BoardDTO.builder()
                    .boardIdx(item.getBoardIdx())
                    .member(item.getMember().entityToDto())
                    .boardCn(item.getBoardCn())
                    .boardDate(item.getBoardDate())
                    .delAt(item.getDelAt())
                    .codeDetail(item.getCodeDetail().entityToDto())
                    .fileAt(item.getFileAt())
                    .totalComments(item.getTotalComments())
                    .totalLikes(item.getTotalLikes())
                    .boardFileDTO(item.getBoardFile() == null ? null : item.getBoardFile().entityToDto())
                    .build();

            item.getLikesList().forEach(l -> {
                if(l.getMember().getMemIdx().equals(memId)) {
                    boardDTO.setLike(true);
                }
            });
            resultList.add(boardDTO);
        });
        return resultList;
    }

    @Override
    public long getArticleNum(String keyword, String selected, String codeDetails, Long memIdx){
        JPAQueryFactory query = new JPAQueryFactory(em);
        QBoard board = QBoard.board;
        QBoardReport reports = QBoardReport.boardReport;
        QMember member = QMember.member;
        QCodeDetail codeDetail = QCodeDetail.codeDetail;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(selected.equals("memNick")){
            booleanBuilder.and(board.member.memNick.contains(keyword));
        } else if(selected.equals("boardCN")){
            booleanBuilder.and(board.boardCn.contains(keyword));
        } else if(selected.equals("All")){
            booleanBuilder.and(board.member.memNick.contains(keyword))
                    .or(board.boardCn.contains(keyword));
        }

        if("8".equals(codeDetails)) {
            booleanBuilder.and(
                    member.memIdx.eq(memIdx)
            );
        }

        return query.select(board.count())
                .from(board)
                .join(board.codeDetail, codeDetail)
                .join(board.member, member)
                .where(
                        board.delAt.eq("N")
                        .and(booleanBuilder)
                        .and((JPAExpressions
                                .select(reports.count())
                                .from(reports)
                                .where(reports.board.boardIdx.eq(board.boardIdx))
                        ).lt(5L))
                        .and(codeDetail.codeDetailIdx.eq(codeDetails))
                )
                .fetchCount();
    }

    @Override
    public long getProjectArticleNum(String keyword, String selected, String codeDetails, long projectIdx){
        JPAQueryFactory query = new JPAQueryFactory(em);
        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QProjectBoard projectBoard = QProjectBoard.projectBoard;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(selected.equals("memNick")){
            booleanBuilder.and(board.member.memNick.contains(keyword));
        } else if(selected.equals("boardCN")){
            booleanBuilder.and(board.boardCn.contains(keyword));
        } else if(selected.equals("All")){
            booleanBuilder.and(board.member.memNick.contains(keyword))
                    .or(board.boardCn.contains(keyword));
        }

        return query.select(board.count())
                .from(board, member, projectBoard)
                .where(board.codeDetail.codeDetailIdx.eq(codeDetails)
                        .and(projectBoard.project.prjctIdx.eq(projectIdx))
                        .and(board.boardIdx.eq(projectBoard.board.boardIdx))
                        .and(board.member.memIdx.eq(member.memIdx))
                        .and(board.delAt.eq("N"))
                        .and(booleanBuilder)
                )
                .fetchCount();
    }

    @Override
    public List<CommentDTO> getComments(long boardIdx, int commentsOnView, long memIdx) {
        QComment comment = QComment.comment;
        QBoard qBoard = QBoard.board;
        QMember qMember = QMember.member;
        QCommentReport commentReport = QCommentReport.commentReport;

        JPAQueryFactory query = new JPAQueryFactory(em);

        List<Comment> commentList = query.select(comment)
                .from(comment)
                .innerJoin(comment.board, qBoard)
                .fetchJoin()
                .innerJoin(comment.member, qMember)
                .fetchJoin()
                .where(
                        (JPAExpressions
                                .select(commentReport.count())
                                .from(commentReport)
                                .where(commentReport.comment.answerIdx.eq(comment.answerIdx))
                        ).lt(5L)
                                .and(comment.board.boardIdx.eq(boardIdx))
                                .and(comment.answerDelAt.eq("N")))
                .offset(commentsOnView)
                .limit(5)
                .orderBy(comment.answerIdx.desc())
                .fetch();
        List<CommentDTO> resultList = new ArrayList<>();

        commentList.forEach(item -> {
            CommentDTO commentDTO = CommentDTO.builder()
                    .answerIdx(item.getAnswerIdx())
                    .answerDate(item.getAnswerDate())
                    .answerDelAt(item.getAnswerDelAt())
                    .answerCn(item.getAnswerCn())
                    .board(item.getBoard().entityToDto())
                    .member(item.getMember().entityToDto())
                    .build();

            item.getCommentReportList().forEach(c -> {
                if(c.getMember().getMemIdx().equals(memIdx)){
                    commentDTO.setReport(true);
                }
            });
            resultList.add(commentDTO);
        });

        return resultList;
    }

    @Override
    public List<Board> findByPrjctIdxOnFive(Long projectIdx) {
        QBoard board = QBoard.board;
        QProjectBoard projectBoard = QProjectBoard.projectBoard;

        JPAQuery<Board> query = new JPAQuery<>(em);

        return query.from(projectBoard, board)
                .where(projectBoard.project.prjctIdx.eq(projectIdx))
                .orderBy(board.boardDate.desc())
                .offset(0) .limit(6)
                .fetch();
    }

    @Override
    public List<Board> getAllQnaList(String selected, String key, int articleOnView, String codeDetails) {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;

        JPAQueryFactory query = new JPAQueryFactory(em);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        switch (selected) {
            case "memNick":
                booleanBuilder.and(board.member.memNick.contains(key));
                break;
            case "boardCn":
                booleanBuilder.and(board.boardCn.contains(key));
                break;
            case "All":
                booleanBuilder.or(board.member.memNick.contains(key)).or(board.boardCn.contains(key));
                break;
        }

        return query
                .selectFrom(board).distinct()
                .leftJoin(board.commentList, comment)
                .fetchJoin()
                .where((board.delAt.eq("N")
                        .and(board.codeDetail.codeDetailIdx.eq(codeDetails))
                        .and(booleanBuilder))
                )
                .orderBy(board.boardDate.desc())
                .offset(articleOnView).limit(5)
                .fetch();
    }

}
