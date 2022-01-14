package com.kanboo.www.dto.member.nativedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberNative {

    private Long memIdx;
    private String memId;
    private String memNick;
    private String memCelNum;
    private String memToken;
    private String memTag;
    private String memImg;
    private String memPass;
}
