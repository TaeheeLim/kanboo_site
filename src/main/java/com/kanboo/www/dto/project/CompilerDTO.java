package com.kanboo.www.dto.project;

import com.kanboo.www.domain.entity.project.Compiler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilerDTO {

    private Long comIdx;
    private ProjectDTO project;
    private Long parentComIdx;
    private String comSe;
    private String comNm;

    public Compiler dtoToEntity() {
        return Compiler.builder()
                .comIdx(comIdx)
                .project(project.dtoToEntity())
                .parentComIdx(parentComIdx)
                .comSe(comSe)
                .comNm(comNm)
                .build();
    }
}
