package com.busanit501.spring_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
// 용도 : 화면 -> 서버 쪽으로 전달하는 용도.
public class PageRequestDTO {
    // 페이징 하기위한 준비물 준비하기.
    // 낱개로 따로따로 이동하기보다는, 한번에 담아서 전달하기.
    @Builder.Default //빌더 패턴을 사용할 때, 필드에 기본값을 설정
    @Min(value = 1)
    @Positive
    private int page = 1;

    @Builder.Default
    @Min(value = 10)
    @Max(value = 100)
    @Positive
    private int size = 10;

    // 건너띄기 할 데이터의 갯수,
    // 1페이지 10개,
    // 2페이지, 11개 부터 , skip 10
    // 3페이지 21개, 부터 , skip 20
    public int getSkip() {
        return (page - 1) * size;
    }

}
