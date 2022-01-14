package com.kanboo.www.service.inter.board;

import com.kanboo.www.dto.board.CommentReportDTO;

import java.util.Map;

public interface BoardCommentReportService {
    CommentReportDTO updateCommentReport(Map<String, Object> map);

    int deleteCommentReport(Map<String, Object> map);
}
