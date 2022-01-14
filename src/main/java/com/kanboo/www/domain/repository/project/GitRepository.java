package com.kanboo.www.domain.repository.project;

import com.kanboo.www.domain.entity.project.Git;
import com.kanboo.www.domain.repository.project.dslsupport.GitDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GitRepository extends JpaRepository<Git, Long>, GitDslRepository {
    Git findByProjectPrjctIdx(Long prjctIdx);
}
