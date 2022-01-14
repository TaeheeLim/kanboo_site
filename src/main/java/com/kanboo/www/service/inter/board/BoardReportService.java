package com.kanboo.www.service.inter.board;

import com.kanboo.www.dto.board.BoardReportDTO;

import java.util.Map;

public interface BoardReportService {
    BoardReportDTO updateReport(Map<String, Object> map);

    int deleteReport(Map<String, Object> map);
}
