package com.kanboo.www.domain.repository.board;

import com.kanboo.www.domain.entity.board.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCommentReportRepository extends JpaRepository<CommentReport, Long> {
    int deleteByMember_MemIdxAndComment_AnswerIdx(long memIdx, long commentIdx);

    @Query(nativeQuery = true, value = "select count(answer_report_idx)" +
                                " from answer_report ar" +
                                " right join answer a on(ar.answer_idx = a.answer_idx)" +
                                " where a.board_idx = :idx")
    Long getReportCnt(Long idx);
}
