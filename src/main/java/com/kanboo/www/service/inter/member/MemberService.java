package com.kanboo.www.service.inter.member;

import com.kanboo.www.dto.member.MemberDTO;

import java.util.List;
import java.util.Map;

public interface MemberService {

    MemberDTO loginHandler(MemberDTO memberDTO);

    MemberDTO signHandler(MemberDTO memberDTO);

    int isExistKTag(String kTag);

    int isExistToken(String token);

    int isExistId(String memId);

    MemberDTO findIdHandler(MemberDTO memberDTO);

    boolean resetPwHandler(MemberDTO memberDTO);

    MemberDTO getUserInfo(String memTag);

    Boolean userModify(MemberDTO memberDTO);

    List<MemberDTO> getAllMember(String selected, String keyword, int articleOnView);

    Long getMaxIndexOfMember(String selected, String key);

    void updateMemberImg(Map<String, Object> map);
}
