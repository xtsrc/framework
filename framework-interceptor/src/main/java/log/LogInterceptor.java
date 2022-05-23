package log;

import com.xt.framwork.common.core.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author tao.xiong
 * @Description 日志拦截器
 * @Date 2022/2/25 11:30
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果有上层调用就用上层的ID
        String traceId = request.getHeader(Constants.TRACE_ID);
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }
        MDC.put(Constants.TRACE_ID, traceId);
        request.setAttribute(Constants.BEGIN_TIME, System.currentTimeMillis());
        log.info("请求开始：url:{},args:{}", request.getRequestURI(), request.getQueryString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //调用结束后删除
        MDC.remove(Constants.TRACE_ID);
        long beginTime = (Long) request.getAttribute(Constants.BEGIN_TIME);
        log.info("请求结束：url:{},耗时:{}ms", request.getRequestURI(), System.currentTimeMillis() - beginTime);
    }
}
