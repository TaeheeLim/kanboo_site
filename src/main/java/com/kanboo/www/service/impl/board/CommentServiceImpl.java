package com.kanboo.www.service.impl.board;

import com.kanboo.www.domain.entity.board.Board;
import com.kanboo.www.domain.entity.board.Comment;
import com.kanboo.www.domain.entity.member.Member;
import com.kanboo.www.domain.repository.board.BoardRepository;
import com.kanboo.www.domain.repository.board.CommentRepository;
import com.kanboo.www.domain.repository.member.MemberRepository;
import com.kanboo.www.dto.board.BoardDTO;
import com.kanboo.www.dto.board.CommentDTO;
import com.kanboo.www.security.JwtSecurityService;
import com.kanboo.www.service.inter.board.BoardService;
import com.kanboo.www.service.inter.board.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final JwtSecurityService jwtSecurityService;

    @Override
    public CommentDTO insertComment(CommentDTO commentDTO) {
        Member member = null;

        if(commentDTO.getMember().getMemIdx() != null) {
            member = memberRepository.findByMemIdx(commentDTO.getMember().getMemIdx());
        } else {
            String memTag = jwtSecurityService.getToken(commentDTO.getMember().getMemTag());
            member = memberRepository.findByMemTag(memTag);
        }

        Board board = boardRepository.findByBoardIdx(commentDTO.getBoard().getBoardIdx());

        Comment comment = Comment.builder()
                .answerCn(commentDTO.getAnswerCn())
                .answerDate(commentDTO.getAnswerDate())
                .answerDelAt(commentDTO.getAnswerDelAt())
                .board(board)
                .commentReportList(new ArrayList<>())
                .member(member)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return savedComment.entityToDto();
    }

    @Override
    public CommentDTO updateComment(Map<String, Object> map) {
        Comment comment = commentRepository.findById(Long.parseLong(map.get("answerIdx") + "")).get();

        if(comment != null){
            comment.changeCommentCn(map.get("content") + "");
            Comment savedComment = commentRepository.save(comment);
            return savedComment.entityToDto();
        }

        return null;
    }

    @Override
    public CommentDTO deleteComment(Map<String, Object> map) {
        Comment comment = commentRepository.findById(Long.parseLong(map.get("answerIdx") + "")).get();

        if(comment != null){
            BoardDTO boardDTO = boardService.decreaseTotalComments(comment.getBoard().getBoardIdx());

            if (boardDTO != null){
                comment.changeAnswerDelAt("Y");
                Comment save = commentRepository.save(comment);
                return save.entityToDto();
            }
        }
        return null;
    }


}
