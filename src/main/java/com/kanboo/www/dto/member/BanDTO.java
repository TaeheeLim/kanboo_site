package com.kanboo.www.dto.member;

import com.kanboo.www.domain.entity.member.Ban;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BanDTO {

    private Long banIdx;
    private MemberDTO member;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate banStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate banEndDate;

    public Ban dtoToEntity() {
        return Ban.builder()
                .banIdx(banIdx)
                .member(member.dtoToEntity())
                .banStartDate(banStartDate)
                .banEndDate(banEndDate)
                .build();
    }
}
