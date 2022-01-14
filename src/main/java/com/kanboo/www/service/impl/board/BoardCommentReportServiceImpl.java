package com.kanboo.www.service.impl.board;

import com.kanboo.www.domain.entity.board.Board;
import com.kanboo.www.domain.entity.board.Comment;
import com.kanboo.www.domain.entity.board.CommentReport;
import com.kanboo.www.domain.entity.member.Member;
import com.kanboo.www.domain.repository.board.BoardCommentReportRepository;
import com.kanboo.www.domain.repository.board.BoardRepository;
import com.kanboo.www.domain.repository.board.CommentRepository;
import com.kanboo.www.domain.repository.member.MemberRepository;
import com.kanboo.www.dto.board.CommentReportDTO;
import com.kanboo.www.service.inter.board.BoardCommentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardCommentReportServiceImpl implements BoardCommentReportService {
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final BoardCommentReportRepository boardCommentReportRepository;
    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public CommentReportDTO updateCommentReport(Map<String, Object> map) {
        Member member = memberRepository.findByMemIdx(Long.parseLong(map.get("memIdx") + ""));
        Comment comment = commentRepository.findById(Long.parseLong(map.get("answerIdx") + "")).get();

        CommentReport commentReport = CommentReport.builder()
                .answerReportResn(map.get("reportReason") + "")
                .comment(comment)
                .member(member)
                .build();
        CommentReport savedCommentReport = boardCommentReportRepository.save(commentReport);
        Board board = boardRepository.findById(savedCommentReport.getComment().getBoard().getBoardIdx()).get();
        Long reportCnt = boardCommentReportRepository.getReportCnt(board.getBoardIdx());
        if(reportCnt >= 5) {
            board.decreaseTotalComments();
        }

        return savedCommentReport.entityToDto();
    }

    @Override
    @Transactional
    public int deleteCommentReport(Map<String, Object> map) {
        int i = boardCommentReportRepository
                .deleteByMember_MemIdxAndComment_AnswerIdx(
                        Long.parseLong(map.get("memIdx") + ""),
                        Long.parseLong(map.get("answerIdx") + "")
                );
        Comment comment = commentRepository.findById(Long.parseLong(map.get("answerIdx") + "")).get();
        Board board = boardRepository.findById(comment.getBoard().getBoardIdx()).get();
        Long reportCnt = boardCommentReportRepository.getReportCnt(board.getBoardIdx());
        if(reportCnt < 5) {
            board.increaseTotalComments();
        }
        return i;
    }
}
