package com.kanboo.www.domain.repository.board.boardFileQueryDSL;

import com.kanboo.www.domain.entity.board.BoardFile;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardFileDSLRepository {
    BoardFile getPresentFile(long boardIdx);

    long deleteFile(long boardIdx);
}
