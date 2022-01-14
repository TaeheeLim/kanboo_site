package com.kanboo.www.service.impl.board;

import com.kanboo.www.domain.entity.board.Board;
import com.kanboo.www.domain.entity.board.BoardReport;
import com.kanboo.www.domain.entity.member.Member;
import com.kanboo.www.domain.repository.board.BoardReportRepository;
import com.kanboo.www.domain.repository.board.BoardRepository;
import com.kanboo.www.domain.repository.member.MemberRepository;
import com.kanboo.www.dto.board.BoardReportDTO;
import com.kanboo.www.service.inter.board.BoardReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardReportServiceImpl implements BoardReportService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardReportRepository boardReportRepository;

    @Override
    public BoardReportDTO updateReport(Map<String, Object> map) {
        Member member = memberRepository.findByMemIdx(Long.parseLong(map.get("memIdx") + ""));
        Board board = boardRepository.findByBoardIdx(Long.parseLong(map.get("boardIdx") + ""));


        BoardReport boardReport = BoardReport.builder()
                .boardReportResn(map.get("reportReason") + "")
                .board(board)
                .member(member)
                .build();
        BoardReport savedReport = boardReportRepository.save(boardReport);
        return savedReport.entityToDto();
    }

    @Override
    @Transactional
    public int deleteReport(Map<String, Object> map) {
        try {
            return boardReportRepository
                    .deleteByMember_MemIdxAndAndBoard_BoardIdx(
                            Long.parseLong(map.get("memIdx") + ""),
                            Long.parseLong(map.get("boardIdx") + "")
                    );
        } catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }


}
