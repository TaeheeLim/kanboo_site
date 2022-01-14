package com.kanboo.www.domain.repository.project;

import com.kanboo.www.domain.entity.project.ChattingContent;
import com.kanboo.www.domain.repository.project.dslsupport.ChattingContentDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChattingContentRepository extends JpaRepository<ChattingContent, Long>, ChattingContentDslRepository {
    List<ChattingContent> findByChat_ProjectPrjctIdx(Long prjctIdx);
}
