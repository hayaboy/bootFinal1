package org.zerock.b01.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.service.ReplyService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
public class ReplyController {

    @Autowired
    private ReplyService replyService;
//    @GetMapping(value = "/testGet")
//    public void testGet(){
//        log.info("testGet");
//    }

//    @GetMapping(value = "/testGet2")
//    public int testGet2(int num1){
//        log.info("testGet2");
//        return num1;
//    }


    //@PostMapping(value = "/test")
//    public void test(){
//        log.info("test");
//    }


//    @PostMapping(value = "/test2")
//    public void test2(int num1){
//        log.info(num1);
//    }

//    @PostMapping(value = "/test3")
//    public void test3(@RequestBody  Member m1){
//        log.info(m1);
//    }

//    @PostMapping(value = "/test4")
//    public  Map<String,Member>  test4(@RequestBody  Member m1){
//        Map<String,Member> map=new HashMap<>();
//        log.info(m1);
//        map.put("m1", m1);
//        return map;
//    }

//    @PostMapping(value = "/test41", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public  Map<String,Member>  test41(@RequestBody  Member m1){
//        Map<String,Member> map=new HashMap<>();
//        log.info(m1);
//        map.put("m1", m1);
//        return map;
//    }

//    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")
//    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE) // 해당 메소드를 받아서 소비(consumes)하는 데이터가 어떤 종류인지 명시, JSON 타입의 데이터 처리하는 메소드
//    public Map<String,Long> register(@RequestBody ReplyDTO replyDTO){
//        Map<String,Long> map=new HashMap<>();
//        map.put("replyDTO", replyDTO.getBno());
//
//        //@RequestBody는 JSON 문자열을 ReplyDTO로 변환
//        log.info("replyDTO : " + replyDTO);
//
//        return map;
//    }

    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE) // 해당 메소드를 받아서 소비(consumes)하는 데이터가 어떤 종류인지 명시, JSON 타입의 데이터 처리하는 메소드
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO, BindingResult bindingResult) throws BindException {
        log.info("replyDTO : " + replyDTO);

        if(bindingResult.hasErrors()){
            throw  new BindException(bindingResult);
        }


        Map<String,Long> resultMap=new HashMap<>();
//        resultMap=Map.of("rno", 111L);

        //resultMap.put("rno", 111L);

        //@RequestBody는 JSON 문자열을 ReplyDTO로 변환

        Long rno=replyService.register(replyDTO);
        resultMap.put("rno", rno);

        return resultMap;
    }


    @ApiOperation(value = "Replies of Board", notes = "GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno,
                                             PageRequestDTO pageRequestDTO){

        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }



    @ApiOperation(value = "Read Reply", notes = "GET 방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO( @PathVariable("rno") Long rno ){

        ReplyDTO replyDTO = replyService.read(rno);

        return replyDTO;
    }


    @ApiOperation(value = "Delete Reply", notes = "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String,Long> remove( @PathVariable("rno") Long rno ){

        replyService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }


    @ApiOperation(value = "Modify Reply", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE )
    public Map<String,Long> remove( @PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO ){

        replyDTO.setRno(rno); //번호를 일치시킴

        replyService.modify(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }



}
