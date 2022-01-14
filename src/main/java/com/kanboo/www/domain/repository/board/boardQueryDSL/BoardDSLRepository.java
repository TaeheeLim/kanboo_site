package com.kanboo.www.domain.repository.board.boardQueryDSL;

import com.kanboo.www.domain.entity.board.Board;
import com.kanboo.www.dto.board.BoardDTO;
import com.kanboo.www.dto.board.CommentDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardDSLRepository {

    List<BoardDTO> getAllList(String selected, String key, int articleOnvView, String codeDetail, long memId);

    List<BoardDTO> getAllProjectList(String selected, String key, int articleOnvView, String codeDetails, long memId, long projectIdx);

    long getArticleNum(String keyword, String selected, String codeDetails, Long memIdx);

    long getProjectArticleNum(String keyword, String selected, String codeDetails, long projectIdx);

    List<CommentDTO> getComments(long boardIdx, int commentsOnView, long memIdx);

    List<Board> findByPrjctIdxOnFive(Long projectIdx);

    List<Board> getAllQnaList(String selected, String key ,int articleOnView, String codeDetail);
}
