package com.busanit501.spring_project.service;

import com.busanit501.spring_project.dto.PageRequestDTO;
import com.busanit501.spring_project.dto.PageResponseDTO;
import com.busanit501.spring_project.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/root-context.xml")
public class TodoServiceTests {

    @Autowired(required = false)
    private TodoService todoService;

    @Test
    public void testInsert() {
        // 화면에서 전달 받은 더미 데이터 작성, TodoDTO
        TodoDTO todoDTO = TodoDTO.builder()
                .title("서비스 단위테스트 스프링 버전")
                .dueDate(LocalDate.now())
                .writer("이상용, 서비스 단위테스트")
                .build();
        todoService.register(todoDTO);
    }

    //    @Test
//    public void testGetAll() {
//        List<TodoDTO> dtoList = todoService.getAll();
//        dtoList.forEach(dto -> log.info(dto));
//    }
    @Test
    public void testGetByTno() {
        TodoDTO todoDTO = todoService.selectByTno(21L);
        log.info(todoDTO);
    }
    @Test
    public void testDeleteByTno() {
        todoService.remove(15L);
    }
    @Test
    public void testUpdate() {
        // 실제 디비 필요하고,
        // 변경할 더미데이터 필요, TodoDTO 필요함.
        // 잠깐, TodoVO , 디비와 직접 적인 연결하는 모델클래스
        // setter 안씀, 이유? 불변성 유지,
        // 방금 만든것 처럼, 따로 메서드를 만들어서 사용을함.
        // DTO는 setter 상관이 없음.
        // 자유롭게 프레젠테이션 로직에서, 자유롭게, 화면에 표현할것을
        // 설정 하기위해서.
        TodoDTO todoDTO = todoService.selectByTno(22L);
        todoDTO.setTitle("수정 테스트, 서비스에서 진행.");
        todoService.modify(todoDTO);
    }

    @Test
    public void testPaging() {
        // 더미 데이터 필요함, 화면에서 전달받은 페이징 정보를 담은 PageRequestDTO 가 필요함.
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(2)
                .size(10)
                .build();
        PageResponseDTO<TodoDTO> responseDTO = todoService.getList(pageRequestDTO);
        log.info("서비스 단위테스트 중, 페이징 처리된 전체 조회 responseDTO:"+responseDTO);
        responseDTO.getDtoList().stream().forEach(dto -> log.info(dto));
    }
}
