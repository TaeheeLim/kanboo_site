package com.kanboo.www.dto.project;

import com.kanboo.www.domain.entity.project.Kanban;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KanbanDTO {

    private Long kbIdx;
    private ProjectDTO project;

    public Kanban dtoToEntity() {
        return Kanban.builder()
                .kbIdx(kbIdx)
                .project(project.dtoToEntity())
                .build();
    }
}
