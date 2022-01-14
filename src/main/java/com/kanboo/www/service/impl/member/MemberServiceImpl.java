package com.kanboo.www.service.impl.member;

import com.kanboo.www.domain.entity.member.Member;
import com.kanboo.www.domain.repository.member.MemberRepository;
import com.kanboo.www.dto.global.RoleDto;
import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.security.JwtSecurityService;
import com.kanboo.www.service.inter.member.MemberService;
import com.kanboo.www.util.CreateKTag;
import com.kanboo.www.util.CreateTempPw;
import com.kanboo.www.util.CryptoUtil;
import com.kanboo.www.util.SendSMS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtSecurityService jwtSecurityService;

    @Override
    public MemberDTO loginHandler(MemberDTO memberDTO) {

        Member member = null;

        try {
            member = memberRepository.findByMemIdAndMemPass(
                    memberDTO.getMemId(),
                    CryptoUtil.encryptSha512(memberDTO.getMemPass())
            );
            MemberDTO dto = member.entityToDto();
            dto.setRole(member.getRole().entityToDto());
            return dto;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public MemberDTO signHandler(MemberDTO memberDTO) {

        String kTag, toKen = "";

        do {
            kTag = CreateKTag.create();
        } while (isExistKTag(kTag) > 0);

        do {
            toKen = UUID.randomUUID().toString();
        } while (isExistToken(toKen) > 0);

        String password = memberDTO.getMemPass();

        try {
            memberDTO.setMemPass(CryptoUtil.encryptSha512(password));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        memberDTO.setRole(new RoleDto(1L, "ROLE_MEMBER"));

        Member beforeMember = Member.builder()
                .memIdx(memberDTO.getMemIdx())
                .memId(memberDTO.getMemId())
                .memNick(memberDTO.getMemNick())
                .memCelNum(memberDTO.getMemCelNum())
                .memToken(toKen)
                .memTag(kTag)
                .memImg("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAVCAYAAABG1c6oAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAADRSURBVHgBrZQLDYMwEIb/LhMADnAwCasUJOAAHMzCLEwBm4M5AAfg4HYXWNYM+qRfck3T9L70cS3ggIg0R88x0YL0a6TAiS3ZaWNlNfnRMcI+QNjv5SqLkOBnVkqV/4Mn22T4KfYGbcI3/LwQylouPmrEwAmdQ9YhBVrK57lKprWvXTnKI5SD1/hdwCjBtzsicmXfJ2etwaAz5EkVhRW1Ka5csoHiGTbSAzJTWpjCOx3nJi5Fy3IH5KGUp9cgH40IL8jHVbY8wfJzJDCfuXkgIx+zEByVvJWBBgAAAABJRU5ErkJggg==")
                .memPass(memberDTO.getMemPass())
                .role(new RoleDto(2L, "ROLE_MEMBER").dtoToEntity())
                .build();

        Member member = memberRepository.save(beforeMember);

        return member.entityToDto();
    }

    @Override
    public int isExistKTag(String kTag) {
        return memberRepository.countByMemTag(kTag);
    }

    @Override
    public int isExistToken(String token) {
        return memberRepository.countByMemToken(token);
    }

    @Override
    public int isExistId(String memId) {
        return memberRepository.countByMemId(memId);
    }

    @Override
    public MemberDTO findIdHandler(MemberDTO memberDTO) {
        return memberRepository.findByMemToken(memberDTO.getMemToken()).entityToDto();
    }

    @Override
    @Transactional
    public boolean resetPwHandler(MemberDTO memberDTO) {
        Member member = memberRepository.findByMemToken(memberDTO.getMemToken());
        String newPw = null;
        String memNum = null;

        if (member != null) {
            newPw = CreateTempPw.create();
            memNum = "010"+member.getMemCelNum().replaceAll("-","");
            SendSMS.send(memNum, newPw);
            member.changeMemPass(newPw);

            try {
                member.changeMemPass(CryptoUtil.encryptSha512(newPw));
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public MemberDTO getUserInfo(String memTag) {
        Member member = memberRepository.findByMemTag(memTag);
        return MemberDTO.builder()
                .memIdx(member.getMemIdx())
                .memId(member.getMemId())
                .memNick(member.getMemNick())
                .memTag(member.getMemTag())
                .memCelNum(member.getMemCelNum())
                .memImg(member.getMemImg())
                .build();
    }

    @Override
    @Transactional
    public Boolean userModify(MemberDTO memberDTO) {
        try{
            memberDTO.setMemPass(CryptoUtil.encryptSha512(memberDTO.getMemPass()));
        } catch (Exception e) {
            return false;
        }
        Member member = memberRepository.findByMemIdAndMemPass(memberDTO.getMemId(), memberDTO.getMemPass());
        if(member != null) {
            member.changeMemPass(memberDTO.getMemPass());
            member.changeMemCelNum(memberDTO.getMemCelNum());
            member.changeMemNick(memberDTO.getMemNick());
            member.changeMemImg(memberDTO.getMemImg());
            return true;
        }
        return false;
    }

    @Override
    public List<MemberDTO> getAllMember(String selected, String keyword, int articleOnView) {

        List<Member> all = memberRepository.findAllMemberBanInfo(selected,keyword,articleOnView);
        List<MemberDTO> result = new ArrayList<>();
        for (Member m : all) {
            MemberDTO memberDTO = m.entityToDto();
            if(m.getBan() != null) {
                memberDTO.setBan(m.getBan().entityToDto());
            }
            result.add(memberDTO);
        }
        return result;
    }

    @Override
    public Long getMaxIndexOfMember(String selected, String key) {
        return memberRepository.getMaxIndexOfMember(selected,key);
    }

    @Override
    @Transactional
    public void updateMemberImg(Map<String, Object> map) {
        String token = map.get("token") + "";
        String memTag = jwtSecurityService.getToken(token);
        Member member = memberRepository.findByMemTag(memTag);
        if(member != null) {
            String img = map.get("img") + "";
            member.changeMemImg(img);
        }
    }
}
