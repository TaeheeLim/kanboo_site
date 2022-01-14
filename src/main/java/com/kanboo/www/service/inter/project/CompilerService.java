package com.kanboo.www.service.inter.project;

import com.kanboo.www.dto.project.CompilerDTO;
import com.kanboo.www.dto.project.ProjectDTO;

import java.util.List;
import java.util.Map;

public interface CompilerService {

    Map<String, String> runDemo(String code);

    List<CompilerDTO> getList(ProjectDTO projectDTO);

    Map<String, String> runMemberProject(ProjectDTO projectDTO);

    List<CompilerDTO> getHtmlList(ProjectDTO projectDTO);
}
