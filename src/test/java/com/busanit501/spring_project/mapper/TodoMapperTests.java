package com.busanit501.spring_project.mapper;

import com.busanit501.spring_project.domain.TodoVO;
import com.busanit501.spring_project.dto.PageRequestDTO;
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
public class TodoMapperTests {
    @Autowired(required = false)
    private TodoMapper todoMapper;

    @Test
    public void testGetTime() {
        log.info("디비로부터 시간 정보 가져오기 확인 : " + todoMapper.getTime());
    }

    @Test
    public void testInsert() {
        // 더미 데이터, 화면에서 전달 받은 데이터
        TodoVO todoVO = TodoVO.builder()
                .title("오늘 점심 뭐 먹지? 스프링 버전")
                .dueDate(LocalDate.now())
                .writer("이상용 스프링 버전")
                .build();
        todoMapper.insert(todoVO);
    }
    @Test
    public void testSelectAll() {
        List<TodoVO> voList = todoMapper.selectAll();
        voList.forEach(vo -> log.info(vo));
    }
    @Test
    public void testSelectByTno() {
        TodoVO todoVO = todoMapper.selectByTno(21L);
        log.info(todoVO);
    }

    @Test
    public void testDelete() {
        // 실제 디비의 내용으로 tno 번호
        todoMapper.delete(18L);
    }

    @Test
    public void testUpdate() {
        // 실제 디비 사용해야함.
        // 수정할 더미 데이터 필요함.
        TodoVO todoVO = todoMapper.selectByTno(22L);
        todoVO.changeTitle("수정 테스트입니다");
        todoMapper.update(todoVO);

    }

    // 페이징 처리가 된 전체 리스트,
    @Test
    public void testSelectListwithPage() {
        // 준비물 , 화면에서 전달받은 페이징 정보가 필요,
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(2) // 확인시, 이 페이지 번호만 변경하면 됨.
                .size(10)
                .build();
        List<TodoVO> todoVOList = todoMapper.selectList(pageRequestDTO);
        todoVOList.forEach(vo -> log.info(vo));
    }

    @Test
    public void testSelectCount() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1) // 확인시, 이 페이지 번호만 변경하면 됨.
                .size(10)
                .build();
        int total = todoMapper.getCount(pageRequestDTO);
        log.info("total : " + total);
    }

}
