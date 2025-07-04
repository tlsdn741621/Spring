<!--베이스 레이아웃 접근 주소 ,
http://localhost:8080/resources/test.html-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap demo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!--        <h1>Header</h1>-->
        <!--        네비게이션바 추가 시작-->
        <div class="row">
            <div class="col">
                <nav class="navbar navbar-expand-lg bg-body-tertiary">
                    <div class="container-fluid">
                        <a class="navbar-brand" href="#">Navbar</a>
                        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                                data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
                                aria-label="Toggle navigation">
                            <span class="navbar-toggler-icon"></span>
                        </button>
                        <div class="collapse navbar-collapse" id="navbarNav">
                            <ul class="navbar-nav">
                                <li class="nav-item">
                                    <a class="nav-link active" aria-current="page" href="/todo/list">목록가기</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="/todo/register">글쓰기</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="#">Pricing</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link disabled" aria-disabled="true">Disabled</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>

            </div>
        </div>
        <!--        네비게이션바 추가 끝-->

        <!--        class="row content"-->
        <div class="row content">
            <!--        col-->
            <div class="col">
                <!--        카드 시작 부분-->
                <div class="card">
                    <div class="card-header">
                        tno로 번호로 하나조회, 상세조회
                    </div>
                    <div class="card-body">
                        <div class="input-group mb-3">
                            <span class="input-group-text">Tno</span>
                            <input type="text" name="tno" class="form-control" readonly
                                   value=<c:out value="${dto.tno}"></c:out> >
                        </div>
                        <div class="input-group mb-3">
                            <span class="input-group-text">Title</span>
                            <input type="text" name="title" class="form-control" readonly
                                   value='<c:out value="${dto.title}"></c:out>'>
                        </div>

                        <div class="input-group mb-3">
                            <span class="input-group-text">DueDate</span>
                            <input type="date" name="dueDate" class="form-control" readonly
                                   value=<c:out value="${dto.dueDate}"></c:out>>
                        </div>

                        <div class="input-group mb-3">
                            <span class="input-group-text">Writer</span>
                            <input type="text" name="writer" class="form-control" readonly
                                   value=<c:out value="${dto.writer}"></c:out>>
                        </div>
                        <div class="input-group mb-3">
                            <label class="form-check-label">Finished &nbsp</label>
                            <input type="checkbox" name="finished" class="form-check-input" readonly
                            ${dto.finished ? "checked" : ""}>
                        </div>
                        <div class="my-4">
                            <div class="float-end">
                                <button type="button" class="btn btn-primary">수정하기</button>
                                <button type="button" class="btn btn-secondary">목록가기</button>
                            </div>
                        </div>
                        <script>
                            // 자바스크립트 , 좀더 편하게 사용하는 도구들 많음.
                            // 대표적으로 jQuery, 많이 사용함. -> 리액트 , Vue.js ,
                            // 순수 자바스크립트, 바닐라 자바스크립라고 표현함,
                            // 바닐라로, 특정 요소를 선택하고, 로직 처리함.
                            document.querySelector(".btn-primary").addEventListener("click",
                                function (e) {
                                    // read , 읽기전용. 변결 불가.
                                    // 수정폼으로 가기. 데이터 변경 가능.
                                    // 양쪽을 백티 기호로 감싸기, 문자열 처리하기가 쉽다.
                                    // 상세보기 화면 -> 수정 폼 화면으로 이동시 -> 쿼리 스트링으로 페이징정보를 달고서 간다.
                                    // 어디로 가죠? /todo/modify/ get 화면 처리로 가야함.
                                    self.location = `/todo/modify?tno=${dto.tno}&${pageRequestDTO.link}`
                                }, false)

                            document.querySelector(".btn-secondary").addEventListener("click",
                                function (e) {
                                    // read , 읽기전용. 변결 불가.
                                    // 수정폼으로 가기. 데이터 변경 가능.
                                    // 추가, 상세보기 화면 -> 다시 목록 이동시, 페이징 정보를 달고 가기.
                                    // 서버로부터 자동으로 넘겨 받은 객체, pageRequestDTO에서 link 꺼내서 사용하기.
                                    self.location = "/todo/list?${pageRequestDTO.link}"
                                }, false)


                        </script>
                        <%--                        </form>--%>
                        <%--                        Todo 입력 폼 여기에 작성--%>

                    </div>
                </div>
            </div>
            <!--        카드 끝 부분-->
        </div>
        <!--        col-->
    </div>
    <!--        class="row content"-->
</div>
<%--    <div class="row content">--%>
<%--        <h1>Content</h1>--%>
<%--    </div>--%>
<div class="row footer">
    <!--        <h1>Footer</h1>-->
    <div class="row fixed-bottom" style="z-index: -100">
        <footer class="py-1 my-1">
            <p class="text-center text-muted">Footer</p>
        </footer>
    </div>
</div>
</div>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>