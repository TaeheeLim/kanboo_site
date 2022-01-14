package com.kanboo.www.controller.global;

import com.kanboo.www.domain.entity.board.Comment;
import com.kanboo.www.domain.entity.member.Member;
import com.kanboo.www.domain.repository.board.CommentRepository;
import com.kanboo.www.domain.repository.board.boardQueryDSL.BoardDSLRepositoryImpl;
import com.kanboo.www.domain.repository.member.MemberRepository;
import com.kanboo.www.dto.board.*;
import com.kanboo.www.dto.global.CodeDetailDto;
import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.security.JwtSecurityService;
import com.kanboo.www.service.inter.board.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private static final Logger logger
            = LoggerFactory.getLogger(BoardDSLRepositoryImpl.class);

    private final BoardService boardService;
    private final BoardFileService boardFileService;
    private final LikeService likeService;
    private final JwtSecurityService jwtSecurityService;
    private final MemberRepository memberRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final BoardReportService boardReportService;
    private final BoardCommentReportService commentReportService;
    private final ProjectBoardService projectBoardService;

    @GetMapping("/boardTest")
    public Map<String, Object> testBoard(String selected, String key, int articleOnvView,
                                         String codeDetail, String token, long projectIdx) {
        String memTag  = jwtSecurityService.getToken(token);
        // 유저 정보 조회
        MemberDTO memberDTO = null;
        if(memTag != null) {
            memberDTO = memberRepository.findByMemTag(memTag).entityToDto();
        }
        List<BoardDTO> allList = new ArrayList<>();
        // 게시판 정보 조회
        if(Integer.parseInt(codeDetail) == 7){
            allList = boardService.getAllList(selected, key, articleOnvView, codeDetail, memberDTO);
        } else if(Integer.parseInt(codeDetail) == 8 ){
            allList = boardService.getAllList(selected, key, articleOnvView, codeDetail, memberDTO);
        } else if(Integer.parseInt(codeDetail) == 9){
            if(memTag == null) {
                return null;
            }
            allList = boardService.getAllProjectList(selected, key, articleOnvView, codeDetail, memberDTO, projectIdx);
        }

        // 맵에 정보 담기
        Map<String, Object> result = new HashMap<>();
        result.put("member", memberDTO);
        result.put("boardList", allList);
        return result;
    }

    @GetMapping("/getArticleNum")
    public long getArticleNum(String key, String selected, String codeDetails, long projectIdx, String token) {
        if(Integer.parseInt(codeDetails) == 7){
            return boardService.getArticleNum(key, selected, codeDetails, null);
        } else if(Integer.parseInt(codeDetails) == 8){
            String memTag = jwtSecurityService.getToken(token);
            if(memTag != null && memTag.length() == 6) {
                return boardService.getArticleNum(key, selected, codeDetails, memTag);
            }
        } else if(Integer.parseInt(codeDetails) == 9){
            return boardService.getProjectArticleNum(key, selected, codeDetails, projectIdx);
        }
        return 0;
    }

    @PostMapping("/BoardComment")
    public List<CommentDTO> getComments(@RequestBody Map<String, Object> map){
        String foundToken = jwtSecurityService.getToken(map.get("token") + "");
        Member member = null;
        if(foundToken != null) {
            member = memberRepository.findByMemTag(foundToken);
        }
        return boardService.getComments(
                Long.parseLong(map.get("boardIdx") + ""),
                Integer.parseInt(map.get("commentsOnView") + ""),
                member == null ? 0 : member.getMemIdx());
    }


    @GetMapping("/DeleteBoard")
    public boolean deleteBoard(long boardIdx, String token, long projectIdx, long codeDetailIdx) {
        String foundToken = jwtSecurityService.getToken(token);

        if(foundToken != null){
            if(codeDetailIdx == 7){
                return boardService.deleteBoard(boardIdx);
            } else if(codeDetailIdx == 8){
                return boardService.deleteBoard(boardIdx);
            } else {
                int result = projectBoardService.deleteProjectBoard(boardIdx, projectIdx);
                if(result != 0){
                    return boardService.deleteBoard(boardIdx);
                }
            }
        }
        return false;
    }

    @PostMapping("/insertBoard")
    public BoardDTO insertBoard(@RequestBody Map<String, Object> map){
        String token = jwtSecurityService.getToken(map.get("token") + "");
        Member member = memberRepository.findByMemTag(token);
        CodeDetailDto codeDetail = CodeDetailDto.builder()
                .codeDetailIdx(map.get("codeDetailIdx") + "")
                .build();

        BoardDTO dto = BoardDTO.builder()
                .boardCn(map.get("boardCn") + "")
                .delAt("N")
                .fileAt(map.get("fileAt") + "")
                .totalComments(0)
                .totalLikes(0)
                .codeDetail(codeDetail)
                .build();

        if(String.valueOf(map.get("codeDetailIdx")).equals("7")){
            return boardService.insertBoard(dto, member.getMemIdx());
        } else if(String.valueOf(map.get("codeDetailIdx")).equals("8")) {
            return boardService.insertBoard(dto, member.getMemIdx());
        } else if(String.valueOf(map.get("codeDetailIdx")).equals("9")){
            BoardDTO boardDTO = boardService.insertBoard(dto, member.getMemIdx());
            if(boardDTO != null){
                projectBoardService.insertProjectBoard(boardDTO.getBoardIdx(), Long.parseLong(map.get("projectIdx") + ""));
            }
            return boardDTO;
        }
        return null;
    }

    @PostMapping("/updateBoard")
    public BoardDTO updateBoard(@RequestBody Map<String, Object> map){
        String token = jwtSecurityService.getToken(map.get("token")+ "");
        if(token != null){
            return boardService.updateBoard(map);
        }
        return null;
    }

    @PostMapping("/insertFile")
    public BoardFileDTO insertFile(String fileSize, String fileName,
                                   String extensionName, long boardIdxToInsertFile,
                                   int category, @ModelAttribute MultipartFile file,
                                   String checkInsertOrUpdate, String token, long projectIdx){

        String foundToken = jwtSecurityService.getToken(token);
        Member member = memberRepository.findByMemTag(foundToken);

        BoardDTO boardDTO = BoardDTO.builder()
                .boardIdx(boardIdxToInsertFile)
                .build();
        BoardFileDTO fileDTO = BoardFileDTO.builder()
                .fileSize(fileSize)
                .fileName(fileName)
                .extensionName(extensionName)
                .board(boardDTO)
                .build();

        return boardFileService.insertFiles(fileDTO, file, category, member.getMemIdx(), checkInsertOrUpdate, projectIdx);
    }

    @PostMapping("/downloadFile")
    public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent,
                                                 @RequestBody Map<String, String> map){

        return boardFileService.downloadFile(userAgent, map);
    }

    @GetMapping("/updateLike")
    public BoardDTO updateLike(long boardIdx, String token){
        String foundToken = jwtSecurityService.getToken(token);
        Member byMemToken = memberRepository.findByMemTag(foundToken);
        Long memIdx = byMemToken.getMemIdx();
        LikesDTO likesDTO = likeService.insertLike(boardIdx, memIdx);

        if(likesDTO != null){
            return boardService.updateLikes(boardIdx);
        }

        return null;
    }

    @GetMapping("/deleteLike")
    public BoardDTO deleteLike(long boardIdx, String token){
        String foundToken = jwtSecurityService.getToken(token);
        Member byMemTag = memberRepository.findByMemTag(foundToken);
        Long memIdx = byMemTag.getMemIdx();

        int deleteResult = likeService.deleteLike(boardIdx, memIdx);

        if(deleteResult != 0){
            return boardService.decreaseLikesNum(boardIdx);
        }
        return null;
    }

    @PostMapping("/insertComment")
    public CommentDTO insertComment(@RequestBody Map<String, Object> map){
        String token = jwtSecurityService.getToken(map.get("token") + "");

        Member member = memberRepository.findByMemTag(token);

        BoardDTO boardDTO = BoardDTO.builder()
                .boardIdx(Long.parseLong(String.valueOf(map.get("boardIdx"))))
                .build();
        MemberDTO memberDTO = MemberDTO.builder()
                .memNick(member.getMemNick())
                .memIdx(member.getMemIdx())
                .build();

        CommentDTO commentDTO = CommentDTO.builder()
                .member(memberDTO)
                .board(boardDTO)
                .answerDelAt(map.get("answerDelAt") + "")
                .answerDate(LocalDateTime.now())
                .answerCn(map.get("answerCn") + "")

                .build();
        BoardDTO returnedBoard = boardService.increaseTotalComments(Long.parseLong(map.get("boardIdx") + ""));

        if(returnedBoard != null){
            return commentService.insertComment(commentDTO);
        }
        return null;
    }

    @PostMapping("/updateComment")
    public CommentDTO updateComment(@RequestBody Map<String, Object> map){
        String token = jwtSecurityService.getToken(map.get("token") + "");
        Member member = memberRepository.findByMemTag(token);

        Long id = Long.parseLong(map.get("answerIdx") + "");
        Comment comment = commentRepository.findById(id).get();

        if(member.getMemIdx() == comment.getMember().getMemIdx()){
            return commentService.updateComment(map);
        }
        return null;
    }

    @PostMapping("/deleteComment")
    public CommentDTO deleteComment(@RequestBody Map<String, Object> map){
        String token = jwtSecurityService.getToken(map.get("token") + "");
        Member member = memberRepository.findByMemTag(token);
        Comment comment = commentRepository.findById(Long.parseLong(map.get("answerIdx") + "")).get();

        if(member.getMemIdx() == comment.getMember().getMemIdx()){
            return commentService.deleteComment(map);
        }
        return null;
    }

    @PostMapping("/updateReport")
    public BoardReportDTO updateReport(@RequestBody Map<String, Object> map){
        String token = jwtSecurityService.getToken(map.get("token") + "");
        Member member = memberRepository.findByMemTag(token);

        if(member != null){
            map.put("memIdx", member.getMemIdx());
            return boardReportService.updateReport(map);
        }
        return null;
    }

    @PostMapping("/cancelReport")
    public int deleteReport(@RequestBody Map<String, Object> map){
        String token = jwtSecurityService.getToken(map.get("token") + "");
        Member member = memberRepository.findByMemTag(token);

        if(member != null){
            map.put("memIdx", member.getMemIdx());
            return boardReportService.deleteReport(map);
        }
        return 0;
    }

    @PostMapping("/updateCommentReport")
    public CommentReportDTO updateCommentReport(@RequestBody Map<String, Object> map){
        String token = jwtSecurityService.getToken(map.get("token") + "");
        Member member = memberRepository.findByMemTag(token);

        if(member != null){
            map.put("memIdx", member.getMemIdx());
            return commentReportService.updateCommentReport(map);
        }
        return null;
    }

    @PostMapping("/deleteCommentReport")
    public int deleteCommentReport(@RequestBody Map<String, Object> map){
        String token = jwtSecurityService.getToken(map.get("token") + "");
        Member member = memberRepository.findByMemTag(token);

        if(member != null){
            map.put("memIdx", member.getMemIdx());
            return commentReportService.deleteCommentReport(map);
        }
        return 0;
    }
}
