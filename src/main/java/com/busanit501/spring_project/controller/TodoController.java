package com.busanit501.spring_project.controller;

import com.busanit501.spring_project.dto.PageRequestDTO;
import com.busanit501.spring_project.dto.TodoDTO;
import com.busanit501.spring_project.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
// 대표 url,/todo,
// 밑에 메서드 하위  url 주소를 지정함.
// 최종 url, /todo/지정한 주소
@RequestMapping("/todo")
public class TodoController {
    private final TodoService todoService;

    // 최종 url : /todo/list
    @RequestMapping("/list")
    // void 라고하면, 메서드 명 : list
    // /WEB-INF/views/todo/list.jsp , 가리킴.
    // 자동 연결, 뷰 리졸버라는 친구의 업무.
    // 메소드명이 아니라, url 주소로 , 화면 연결을함.
    // 웹화면에서, url: http://localhost:8080/todo/list?page=3&size=10
    // page, size 정보를 자동 형변환 함, @Valid PageRequestDTO pageRequestDTO,
    public void list2(@Valid PageRequestDTO pageRequestDTO,
                      BindingResult bindingResult,
                      Model model) {
        log.info("TodoController에서 작업, list 호출 ");
        // 페이징 처리 전
//        List<TodoDTO> dtoList = todoService.getAll();
//        dtoList.forEach(vo -> log.info(vo));
//        // 서비스로 부터 외주 맡겨서, 디비 정보를 받아와서, 화면에 전달, 탑재하기.
//        model.addAttribute("dtoList", todoService.getAll());
        // 페이징 처리 후
        // 유효성체크.
        if (bindingResult.hasErrors()) {
            // 기본값으로 할당하기.
            // size  최소 10, 최대 100, 요청 :1000, 잘못된 요청이면, 기본으로 10으로 변경하기.
            pageRequestDTO = PageRequestDTO.builder().build();
        }
        model.addAttribute("responseDTO", todoService.getList(pageRequestDTO));
    }

    // 최종 url : /todo/register
    // 메소드 : get
    // void 라고하면, 메서드 명 : register
    // /WEB-INF/views/todo/register.jsp , 가리킴.
    // 자동 연결, 뷰 리졸버라는 친구의 업무.
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public void register() {
        log.info("TodoController에서 작업, register 호출 ");
    }

    // 로직처리.
    // 최종 url : /todo/register ,
    // 메소드 : post
    // 화면에서, TodoDTO 형식의 데이터를 전달을 받으면,
    // 각각 받는게 아니라, TodoDTO 모델 클래스로 한번에 받기 예시.
    @PostMapping("/register")
    // 작성 순서 유지
    // 순서1, @Valid TodoDTO todoDTO : 유효성 검사 적용
    //  순서2, BindingResult bindingResult : 통과 못한 이유 원인 남겨져있다.
    public String registerPost(@Valid TodoDTO todoDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        log.info("TodoController에서 작업, register , post , 로직처리");
        log.info("todoDTO:" + todoDTO);
        // 만약, 유효성 체크를 통과 못한다면,
        // bindingResult 여기에 무언가 담겨져 있다.
        if (bindingResult.hasErrors()) {
            log.info("TodoController에서 작업, register , post d에서. 유효성 오류가 발생했다");
            // 서버 -> 앞 화면에 , 오류가 발생한 이유를 전달.
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/todo/register";

        }


        // 실제 디비 반영하는 코드,
        todoService.register(todoDTO);

        return "redirect:/todo/list";
    }

    @GetMapping({"/read", "/modify"})
    // 목록 -> 상세보기 : tno 번호와, page, size 정보도 같이 전달함.
    // 한번에 PageRequestDTO 담기,
    // 스프링은 자동으로, PageRequestDTO 내용을 Model 알아서 화면에 전달해줌.
    public void read(Long tno, PageRequestDTO pageRequestDTO, Model model) {
        // 서버에서, 디비로 부터 tno 번호로 하나의 todo 정보를 조회
        // 정방향, 찍고, 역방향으로 돌아온 상태.
        TodoDTO todoDTO = todoService.selectByTno(tno);
        log.info(todoDTO);
        log.info("페이징 정보 받기 pageRequestDTO getLink(): " + pageRequestDTO.getLink());
        // 서버 -> 웹 화면 데이터 전달. 키 : dto , 값 : 객체
        // 화면에서, 사용시, 키 :dto로 사용하면됨.
        model.addAttribute("dto", todoDTO);
    }

    @PostMapping("/remove")
    public String remove(Long tno,
                         PageRequestDTO pageRequestDTO
            , RedirectAttributes redirectAttributes) {
        log.info("삭제 작업 중.,");
        log.info("tno:" + tno);
        // 컨트롤러 기능이 없어서, 서비스에게 외주 주기.
        todoService.remove(tno);

        // 화면에 전달,
        // 쿼리 스트링으로 , 서버 - 화면으로 전달한다.
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("size", pageRequestDTO.getSize());

        // 삭제 후, (로직처리) PRG 패턴, 쿼리스트링으로, 검색 조건 달아주기.
        // pageRequestDTO 의 링크를 이용하기.
        return "redirect:/todo/list?"+pageRequestDTO.getLink();
    }

    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid TodoDTO todoDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("수정 적용 부분에서, 유효성 통과 못할 경우");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            // 현재 수정 화면에서, 수정할 내용을 입력 후, 유효성 체크 통과를 못했다면,
            // 다시 수정 화면으로 이동해야함. 어는 tno 번호 에서 작업하는지 알려줘야함.
            // 쿼리 스트링으로 ?tno=21 , 달고 화면에 전달함.
            redirectAttributes.addAttribute("tno", todoDTO.getTno());
            // 최종 url : /todo/modify?tno=21
            return "redirect:/todo/modify";
        }
        log.info("수정 로직처리 post 작업중 넘어온 데이터 확인 todoDTO: " + todoDTO);
        todoService.modify(todoDTO);

        //  수정후, 현재 페이징 정보를 유지하기, 전달하기.
        // 화면에 전달,
        // 쿼리 스트링으로 , 서버 - 화면으로 전달한다.
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("size", pageRequestDTO.getSize());
        // 수정 후, -> 상세 조회로 이동, : 필요한 준비물, 현재 tno 번호가 필요함.
        redirectAttributes.addAttribute("tno", todoDTO.getTno());

        //PRG 패턴,
        // 수정 후, -> 상세 조회로 이동,
        return "redirect:/todo/read";

    }

}
