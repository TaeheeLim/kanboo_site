package com.kanboo.www.dto.board;

import com.kanboo.www.domain.entity.board.ProjectBoard;
import com.kanboo.www.dto.project.ProjectDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBoardDTO {

    private BoardDTO board;
    private ProjectDTO project;

    public ProjectBoard dtoToEntity() {
        return ProjectBoard.builder()
                .board(board.dtoToEntity())
                .project(project.dtoToEntity())
                .build();
    }
}
