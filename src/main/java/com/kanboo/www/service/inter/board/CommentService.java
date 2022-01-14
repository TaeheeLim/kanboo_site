package com.kanboo.www.service.inter.board;

import com.kanboo.www.dto.board.CommentDTO;

import java.util.Map;

public interface CommentService {
    CommentDTO insertComment(CommentDTO comment);

    CommentDTO updateComment(Map<String, Object> map);

    CommentDTO deleteComment(Map<String, Object> map);
}
