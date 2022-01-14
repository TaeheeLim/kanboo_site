package com.kanboo.www.service.impl.board;

import com.kanboo.www.domain.entity.board.Board;
import com.kanboo.www.domain.entity.board.ProjectBoard;
import com.kanboo.www.domain.entity.project.Project;
import com.kanboo.www.domain.repository.board.ProjectBoardRepository;
import com.kanboo.www.dto.board.ProjectBoardDTO;
import com.kanboo.www.service.inter.board.ProjectBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectBoardServiceImpl implements ProjectBoardService {
    private final ProjectBoardRepository projectBoardRepository;

    @Override
    public ProjectBoardDTO insertProjectBoard(long boardIdx, long projectIdx) {
        Board board = Board.builder()
                .boardIdx(boardIdx)
                .build();

        Project project = Project.builder()
                .prjctIdx(projectIdx)
                .build();

        ProjectBoard projectBoard = ProjectBoard.builder()
                .board(board)
                .project(project)
                .build();

        return projectBoardRepository.save(projectBoard).entityToDto();
    }

    @Override
    @Transactional
    public int deleteProjectBoard(long boardIdx, long projectIdx) {
        return projectBoardRepository.deleteByBoard_BoardIdxAndProject_PrjctIdx(boardIdx, projectIdx);
    }
}
