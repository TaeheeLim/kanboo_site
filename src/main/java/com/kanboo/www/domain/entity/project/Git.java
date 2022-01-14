package com.kanboo.www.domain.entity.project;

import com.kanboo.www.dto.project.GitDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "git")
@Builder
public class Git {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gitIdx;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prjct_idx")
    private Project project;

    private String gitRepo;

    public GitDTO entityToDto() {
        return GitDTO.builder()
                .project(project.entityToDto())
                .gitRepo(gitRepo)
                .build();
    }
}