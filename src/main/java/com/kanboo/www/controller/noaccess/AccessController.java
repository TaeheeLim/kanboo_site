package com.kanboo.www.controller.noaccess;

import com.kanboo.www.domain.entity.member.Ban;
import com.kanboo.www.domain.entity.member.Member;
import com.kanboo.www.domain.repository.member.BanRepository;
import com.kanboo.www.domain.repository.member.MemberRepository;
import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.security.JwtSecurityService;
import com.kanboo.www.service.inter.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/access")
public class AccessController {

    private final MemberService memberService;
    private final JwtSecurityService jwtSecurityService;
    private final Logger logger = LoggerFactory.getLogger(AccessController.class);
    private final MemberRepository memberRepository;
    private final BanRepository banRepository;



    @PostMapping("/login")
    public Map<String, Object> loginHandler(MemberDTO memberDTO) {
        MemberDTO member = memberService.loginHandler(memberDTO);
        Map<String, Object> result = new HashMap<>();
        if(member == null) {
            return null;
        }
        String token = jwtSecurityService.createToken(member.getMemTag(), (60L * 60 * 100000));
        if(member.getRole().getRoleIdx() == 1) {
            result.put("role", "admin");
        } else {
            result.put("role", "member");
        }

        Ban ban = banRepository.findByMember_MemIdx(member.getMemIdx());

        if(ban == null || ban.getBanEndDate().isBefore(LocalDate.now())) {
            result.put("isStop", false);
        } else {
            result.put("isStop", true);
        }
        result.put("token", token);
        return result;
    }


    @PostMapping("/sign")
    public String signHandler(MemberDTO memberDTO) {
        MemberDTO member = memberService.signHandler(memberDTO);
        return member.getMemToken();
    }

    @PostMapping("/idCheck")
    public boolean idDuplicateCheck(String memId) {
        return memberService.isExistId(memId) > 0;
    }

    @PostMapping("/findId")
    public String findIdHandler(MemberDTO memberDTO) {
        return memberService.findIdHandler(memberDTO).getMemId();
    }

    @PostMapping("/resetPw")
    public boolean resetPwHandler(MemberDTO memberDTO){
        return memberService.resetPwHandler(memberDTO);
    }

    @PostMapping("/userInfo")
    public MemberDTO getUserInfo(@RequestBody Map<String,String> map) {
        String memTag = jwtSecurityService.getToken(map.get("token"));
        if(memTag != null) {
            return memberService.getUserInfo(memTag);
        }
        return null;
    }

    @PostMapping("/userModify")
    public Boolean modifyUser(@RequestBody MemberDTO memberDTO) {
        if(memberDTO == null) {
            return false;
        }
        return memberService.userModify(memberDTO);
    }

    @PostMapping("/userImg")
    public void modifyImg(@RequestBody Map<String, Object> map) {
        memberService.updateMemberImg(map);
    }

    @PostMapping("/getMemNick")
    public Map<String, Object> getMemInfo(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        String token = map.get("token");
        String memTag = jwtSecurityService.getToken(token);
        Member member = memberRepository.findByMemTag(memTag);
        result.put("nick", member.getMemNick());
        result.put("idx", member.getMemIdx());
        return result;
    }

}


