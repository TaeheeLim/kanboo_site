package com.kanboo.www.controller.access;


import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.dto.member.ProjectMemberDTO;
import com.kanboo.www.dto.project.ChattingContentDTO;
import com.kanboo.www.dto.project.SocketDTO;
import com.kanboo.www.security.JwtSecurityService;
import com.kanboo.www.service.inter.member.MemberService;
import com.kanboo.www.service.inter.member.ProjectMemberService;
import com.kanboo.www.service.inter.project.ChattingContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SocketController {

    private final ChattingContentService chattingContentService;
    private final ProjectMemberService projectMemberService;
    private final SimpMessageSendingOperations sendingOperations;
    private final JwtSecurityService jwtSecurityService;
    private final MemberService memberService;

    @MessageMapping("/receive")
    @SendTo("/send")
    public SocketDTO socketHandler(SocketDTO socketDTO) throws Exception{
        return new SocketDTO(
                socketDTO.getMemNick()
                , socketDTO.getMemIdx()
                , socketDTO.getPrjctIdx()
                , socketDTO.getText()
                , socketDTO.getDate()
                , socketDTO.getAlarm()
                , socketDTO.getAlarmCategory()
                , socketDTO.getCalCategory()
                , socketDTO.getTextAreaText()
        );
    }

    @MessageMapping("/alarm")
    @SendTo("/send")
    public SocketDTO alarmHandler(SocketDTO socketDTO) throws Exception{
        return new SocketDTO(
                socketDTO.getMemNick()
                , socketDTO.getMemIdx()
                , socketDTO.getPrjctIdx()
                , socketDTO.getText()
                , socketDTO.getDate()
                , socketDTO.getAlarm()
                , socketDTO.getAlarmCategory()
                , socketDTO.getCalCategory()
                , socketDTO.getTextAreaText()
        );
    }

    @MessageMapping("/textArea")
    @SendTo("/send")
    public SocketDTO textAreaHandler(SocketDTO socketDTO){
        return new SocketDTO(
                socketDTO.getMemNick()
                , socketDTO.getMemIdx()
                , socketDTO.getPrjctIdx()
                , socketDTO.getText()
                , socketDTO.getDate()
                , socketDTO.getAlarm()
                , socketDTO.getAlarmCategory()
                , socketDTO.getCalCategory()
                , socketDTO.getTextAreaText()
        );
    }

    @PostMapping("/socket/insertChatLog")
    public ChattingContentDTO chatHandler(ChattingContentDTO chattingContentDTO){
        return chattingContentService.insertChatLog(chattingContentDTO);
    }

    @GetMapping("/socket/selectAllChatLog")
    public Map<String, Object> getAllChat(String token, Long prjctIdx){
        Map<String, Object> map = new HashMap<>();

        String returnToken = jwtSecurityService.getToken(token);
        MemberDTO memberDTO = memberService.getUserInfo(returnToken);
        memberDTO.setMemToken("");
        map.put("member", memberDTO);
        map.put("chat",chattingContentService.getAllChat(prjctIdx));
        return map;
    }

    @PostMapping("/socket/getAllRoom")
    public List<ProjectMemberDTO> getAllRoomFindByMemIdx(Long memIdx){
        return projectMemberService.getAllRoomFindByMemIdx(memIdx);
    }

}
