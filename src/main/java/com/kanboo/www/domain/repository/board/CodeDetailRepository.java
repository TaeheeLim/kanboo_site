package com.kanboo.www.domain.repository.board;

import com.kanboo.www.domain.entity.global.CodeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeDetailRepository extends JpaRepository<CodeDetail, String> {
    CodeDetail findByCodeDetailIdx(String idx);
}
