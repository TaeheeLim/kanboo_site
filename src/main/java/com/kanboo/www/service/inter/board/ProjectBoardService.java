package com.kanboo.www.service.inter.board;

import com.kanboo.www.dto.board.ProjectBoardDTO;

public interface ProjectBoardService {
    ProjectBoardDTO insertProjectBoard(long boardIdx, long projectIdx);

    int deleteProjectBoard(long boardIdx, long projectIdx);
}
