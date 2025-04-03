package org.example.schedule.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class LoginUserFilter implements Filter {

    /**
     * LoginUserFilter 목표
     * 회원가입, 로그인, 일정 목록 조회, 일정 상세 조회를 제외하고 로그인된 유저인지 인증/인가 진행
     */

    private static final String[][] WHITE_LIST = {
            {"/api/users", "POST"},
            {"/api/users/login", "POST"},
            {"/api/schedules", "GET"},
            {"/api/schedules/*", "GET"}
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            // 다양한 기능을 사용하기 위해 다운 캐스팅
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            String requestURI = httpRequest.getRequestURI();
            String method = httpRequest.getMethod();

            // 다양한 기능을 사용하기 위해 다운 캐스팅
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

            if (!isWhiteList(requestURI, method)) {
                // 세션이 존재하면 가져온다. 세션이 없으면 session = null
                HttpSession session = httpRequest.getSession(false);

                // 로그인하지 않은 사용자인 경우
                if (session == null || session.getAttribute("LOGIN_USER") == null) {
                    //ExceptionHandler는 필터에서 발생한 예외는 잡지 못하므로, 예외 응답을 직접 작성
                    httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    httpResponse.setContentType("application/json");
                    httpResponse.setCharacterEncoding("UTF-8");

                    String json = """
                            {
                              "code": 2008,
                              "status": 400,
                              "message": "일정 수정 및 삭제에 대한 권한이 없습니다.",
                              "result": null,
                              "timestamp": "%s"
                            }
                            """.formatted(LocalDateTime.now());

                    httpResponse.getWriter().write(json);
                    return;
                }

                // 로그인 성공 로직
            }
        }
        finally {
            //해당 요청에 대한 필터 체인의 처리가 모두 끝난 후, 다시 doFilter() 호출 위치로 돌아왔을 때, finally 블록 실행
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isWhiteList(String requestURI,String method) {
        for (String[] rule : WHITE_LIST) {
            String uriPattern = rule[0];
            String httpMethod = rule[1];
            if (PatternMatchUtils.simpleMatch(uriPattern, requestURI) && method.equalsIgnoreCase(httpMethod)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}