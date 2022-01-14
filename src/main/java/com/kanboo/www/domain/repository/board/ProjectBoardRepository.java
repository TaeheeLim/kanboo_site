package com.kanboo.www.domain.repository.board;

import com.kanboo.www.domain.entity.board.ProjectBoard;
import com.kanboo.www.domain.repository.board.boardQueryDSL.ProjectBoardDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectBoardRepository extends JpaRepository<ProjectBoard, Long>, ProjectBoardDSLRepository {
    int deleteByBoard_BoardIdxAndProject_PrjctIdx(long boardIdx, long projectIdx);
    List<ProjectBoard> findByProjectPrjctIdx(Long prjctIdx);
}
