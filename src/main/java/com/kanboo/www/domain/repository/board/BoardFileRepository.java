package com.kanboo.www.domain.repository.board;

import com.kanboo.www.domain.entity.board.BoardFile;
import com.kanboo.www.domain.repository.board.boardFileQueryDSL.BoardFileDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardFile, Long>, BoardFileDSLRepository {
}
