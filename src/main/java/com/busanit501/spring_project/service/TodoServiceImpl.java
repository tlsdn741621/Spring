package com.busanit501.spring_project.service;

import com.busanit501.spring_project.domain.TodoVO;
import com.busanit501.spring_project.dto.PageRequestDTO;
import com.busanit501.spring_project.dto.PageResponseDTO;
import com.busanit501.spring_project.dto.TodoDTO;
import com.busanit501.spring_project.mapper.TodoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor // final 필드를 생성자 주입.
public class TodoServiceImpl implements TodoService {
    // 의존성 주입, 외주 주기. 다른 객체 불러오기(포함), 의지(의존)하기
    private final TodoMapper todoMapper;
    private final ModelMapper modelMapper;


    @Override
    public void register(TodoDTO todoDTO) {
        log.info("컨트롤러부터 넘어온 데이터 확인, todoDTO : " + todoDTO);
        // 타입 변경 DTO -> VO ,
        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);
        log.info("변환 된 데이터 확인 todoVO : " + todoVO);
        // 실제 작업, 외주주기, DAO 부탁, 데이터 입력 해줘.
        todoMapper.insert(todoVO);


    }

    // 페이징 후 전체 조회
    @Override
    public PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO) {
        // 필요한 준비물 목록
        // 1) 디비로 부터 페이징 처리된 데이터 목록
        // 2) 디비로 부터 전체 갯수
        // 3) pageRequestDTO, 페이징 정보, 사이즈 정보, skip 정보가 있어서,

        // 1)디비로 부터 받은 타입 dto -> vo 로 변환이 필요함.
        List<TodoVO> voList = todoMapper.selectList(pageRequestDTO);
        List<TodoDTO> dtoList = voList.stream().map(vo -> modelMapper.map(vo, TodoDTO.class))
                .collect(Collectors.toList());
        // 2)
        int total = todoMapper.getCount(pageRequestDTO);
        // 준비물을 , 박스에 담아서, 서비스 -> 컨트롤러 전달.
        PageResponseDTO<TodoDTO> pageResponseDTO = PageResponseDTO.<TodoDTO>withAll()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();

        return pageResponseDTO;
    }

    // 페이징 전 전체 조회
//    @Override
//    public List<TodoDTO> getAll() {
//        // 디비로 부터 전달 받은 TodoVO -> TodoDTO 로 변환 작업,
//        // 병렬 처리로 진행하기.
//        List<TodoDTO> dtoList = todoMapper.selectAll().stream()
//                .map(vo -> modelMapper.map(vo, TodoDTO.class))
//                .collect(Collectors.toList());
//        return dtoList;
//    }

    @Override
    public TodoDTO selectByTno(Long tno) {
        TodoVO todoVO = todoMapper.selectByTno(tno);
        TodoDTO todoDTO = modelMapper.map(todoVO, TodoDTO.class);
        return todoDTO;
    }

    @Override
    public void remove(Long tno) {
        todoMapper.delete(tno);
    }

    @Override
    public void modify(TodoDTO todoDTO) {
        TodoVO todoVO= modelMapper.map(todoDTO, TodoVO.class);
        todoMapper.update(todoVO);
    }
}
