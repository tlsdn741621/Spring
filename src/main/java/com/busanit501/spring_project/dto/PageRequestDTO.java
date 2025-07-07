package com.busanit501.spring_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Arrays;

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

    // 페이징의 정보를 유지하기,link = page=1&size=10
    private String link;

    // 검색과 필터를 위한 정보 준비
    // types, 제목 t , 작성자 w 검색 할지
    // keyword , 검색어
    // finished 완료 여부
    // from, to, 기한
    private String[] types;
    private String keyword;
    private boolean finished;
    private LocalDate from;
    private LocalDate to;


    // 건너띄기 할 데이터의 갯수,
    // 1페이지 10개,
    // 2페이지, 11개 부터 , skip 10
    // 3페이지 21개, 부터 , skip 20
    // limit 10
    public int getSkip() {
        return (page - 1) * size;
    }

    // 업그레이드
    // 기존 페이징 정보만 있었고,
    // 추가하기, 검색 정보들 추가하기.
    public String getLink() {
        if (link == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("page="+this.page);
            builder.append("&size="+this.size);

            // 검색시, types ,t,w 다 선택하고, finished 완료여부 체크하고,
            // 검색어 있고,
            // 기한도 있고
            // 그랬을 때, url :
            //http://localhost:8080/todo/list?size=10&types=t&types=w&keyword=%EC%88%98%EC%A0%95&from=2025-07-01&to=2025-07-31

            //http://localhost:8080/todo/list
            // ?size=10&
            // finished=on
            if(finished) {
                builder.append("&finished=on");
            }
            //&types=t&types=w&
            if(types != null && types.length > 0) {
                for (int i = 0; i < types.length; i++) {
                    builder.append("&types="+types[i]);
                }
            }
//            keyword=%ED%85%8C%EC%8A%A4%ED%8A%B8&
            // 검색어가 한글이 안깨지게 인코딩 타입을 UTF-8 미리 변환 하기.
            if(keyword != null && keyword.length() > 0) {
                try{
                    builder.append("&keyword="+ URLEncoder.encode(keyword,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

//            from=2025-07-01&to=2025-07-31
            if(from != null) {
                builder.append("&from="+from.toString());
            }
            if(to != null) {
                builder.append("&to="+to.toString());
            }

            link = builder.toString();
        }
        return link;
    }
    // type , t, w 이면 true 검사기.
    public boolean checkType(String type){
        if(types==null || types.length==0){
            return false;
        }
        //types -> {"t","w"}, type : t, w
        return Arrays.stream(types).anyMatch(type::equals);
    }

}
