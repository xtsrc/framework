package com.xt.framwork.common.core.util;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author tao.xiong
 * @Description 压缩工具
 * @Date 2022/10/26 16:25
 */

public class ZipUtil {

    private static final int BUFFER_SIZE = 8192;

    public static byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(out)) {
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize(bytes.length);
            zip.putNextEntry(entry);
            zip.write(bytes);
            zip.closeEntry();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Zip compress error", e);
        }
    }

    public static byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(bytes))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            while (zip.getNextEntry() != null) {
                int n;
                while ((n = zip.read(buffer)) > -1) {
                    out.write(buffer, 0, n);
                }
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Zip decompress error", e);
        }
    }

    public static String compress(String text) {
        if (StringUtils.isBlank(text)) {
            throw new NullPointerException("String is null");
        }
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(out)) {
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize(bytes.length);
            zip.putNextEntry(entry);
            zip.write(bytes);
            zip.closeEntry();
            return out.toString("ISO-8859-1");
        } catch (IOException e) {
            throw new RuntimeException("Zip compress error", e);
        }
    }

    public static String decompress(String text) {
        if (StringUtils.isBlank(text)) {
            throw new NullPointerException("String is null");
        }
        byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(bytes))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            while (zip.getNextEntry() != null) {
                int n;
                while ((n = zip.read(buffer)) > -1) {
                    out.write(buffer, 0, n);
                }
            }
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException("Zip decompress error", e);
        }
    }

    public static void main(String[] args) {
        String text = "\"E:\\dev tool\\jdk-11.0.7\\bin\\java.exe\" -agentlib:jdwp=transport=dt_socket,address=127.0.0.1:7902,suspend=y,server=n -XX:TieredStopAtLevel=1 -noverify -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dspring.jmx.enabled=true -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true -javaagent:C:\\Users\\16229\\AppData\\Local\\JetBrains\\IntelliJIdea2021.3\\captureAgent\\debugger-agent.jar -Dfile.encoding=UTF-8 @C:\\Users\\16229\\AppData\\Local\\Temp\\idea_arg_file687492012 com.xt.framework.db.FrameworkDbApplication\n" +
                "Connected to the target VM, address: '127.0.0.1:7902', transport: 'socket'\n" +
                "\n" +
                "  .   ____          _            __ _ _\n" +
                " /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\\n" +
                "( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\\n" +
                " \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )\n" +
                "  '  |____| .__|_| |_|_| |_\\__, | / / / /\n" +
                " =========|_|==============|___/=/_/_/_/\n" +
                " :: Spring Boot ::        (v2.3.4.RELEASE)\n" +
                "\n" +
                "2022-10-26 16:08:03.460-[]-INFO [ 655]-c.x.f.d.FrameworkDbApplication : The following profiles are active: local\n" +
                " _ _   |_  _ _|_. ___ _ |    _ \n" +
                "| | |\\/|_)(_| | |_\\  |_)||_|_\\ \n" +
                "     /               |         \n" +
                "                        3.4.1 \n" +
                "2022-10-26 16:08:13.396-[]-INFO [  33]-c.x.f.d.r.c.RedissonConfig : 【Redisson 配置】：org.springframework.boot.autoconfigure.data.redis.RedisProperties@330f3046\n" +
                "2022-10-26 16:08:17.353-[]-INFO [  61]-c.x.f.d.FrameworkDbApplication : Started FrameworkDbApplication in 17.938 seconds (JVM running for 19.919)\n" +
                "INFO: Sentinel log output type is: file\n" +
                "INFO: Sentinel log charset is: utf-8\n" +
                "INFO: Sentinel log base directory is: C:\\Users\\16229\\logs\\csp\\\n" +
                "INFO: Sentinel log name use pid is: false\n" +
                "WARNING: An illegal reflective access operation has occurred\n" +
                "WARNING: Illegal reflective access by org.springframework.cglib.core.ReflectUtils (file:/E:/dev%20tool/apache-maven-3.6.3/conf/respository/org/springframework/spring-core/5.2.9.RELEASE/spring-core-5.2.9.RELEASE.jar) to method java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain)\n" +
                "WARNING: Please consider reporting this to the maintainers of org.springframework.cglib.core.ReflectUtils\n" +
                "WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations\n" +
                "WARNING: All illegal access operations will be denied in a future release\n" +
                "2022-10-26 16:08:53.756-[]-DEBUG[ 137]-c.x.f.d.m.m.L.insert : ==>  Preparing: INSERT INTO t_log ( trace_id, uri, query_string, method, ip, return_data, start_time, end_time, create_by, update_by, create_time, update_time ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )\n" +
                "2022-10-26 16:08:54.353-[]-DEBUG[ 137]-c.x.f.d.m.m.L.insert : ==> Parameters: e59153d9-94e5-44ab-9066-34559a137d37(String), /oss/qrc(String), text=test5(String), GET(String), 0:0:0:0:0:0:0:1(String), \"http://192.168.1.5:9001/xt-bucket//localqrcode/qrc-5296854528184d309fa2d9676db00fe8.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=tao.xiong%2F20221026%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20221026T080833Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=7dd80da5e516a77f50b766dc75fe5a1af8ad89eb39cc980b95a7efb8048260aa\"(String), 1666771713358(Long), 1666771718484(Long), {\"namePrefix=\":\"windows\",\"mainBoardSerialNumber=\":\"171216551200451\",\"MACAddress=\":\"00-50-56-C0-00-08\",\"CPUIdentification=\":\"BFEBFBFF000906E9\"}(String), {\"namePrefix=\":\"windows\",\"mainBoardSerialNumber=\":\"171216551200451\",\"MACAddress=\":\"00-50-56-C0-00-08\",\"CPUIdentification=\":\"BFEBFBFF000906E9\"}(String), 2022-10-26T16:08:53.705783200(LocalDateTime), 2022-10-26T16:08:53.705783200(LocalDateTime)\n" +
                "Disconnected from the target VM, address: '127.0.0.1:7902', transport: 'socket'\n" +
                "2022-10-26 16:09:32.901-[]-ERROR[  71]-c.x.f.i.g.a.GlobalResponseAdvice : 未知异常！原因是:\n" +
                "org.springframework.dao.DataIntegrityViolationException: \n" +
                "### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column 'return_data' at row 1\n" +
                "### The error may exist in com/xt/framework/db/mysql/mapper/LogMapper.java (best guess)\n" +
                "### The error may involve com.xt.framework.db.mysql.mapper.LogMapper.insert-Inline\n" +
                "### The error occurred while setting parameters\n" +
                "### SQL: INSERT INTO t_log  ( trace_id, uri, query_string, method,  ip,    return_data, start_time, end_time, create_by, update_by, create_time, update_time )  VALUES  ( ?, ?, ?, ?,  ?,    ?, ?, ?, ?, ?, ?, ? )\n" +
                "### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column 'return_data' at row 1\n" +
                "; Data truncation: Data too long for column 'return_data' at row 1; nested exception is com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column 'return_data' at row 1\n" +
                "\tat org.springframework.jdbc.support.SQLStateSQLExceptionTranslator.doTranslate(SQLStateSQLExceptionTranslator.java:104)\n" +
                "\tat org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:72)\n" +
                "\tat org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:81)\n" +
                "\tat org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:81)\n" +
                "\tat org.mybatis.spring.MyBatisExceptionTranslator.translateExceptionIfPossible(MyBatisExceptionTranslator.java:91)\n" +
                "\tat org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:441)\n" +
                "\tat com.sun.proxy.$Proxy155.insert(Unknown Source)\n" +
                "\tat org.mybatis.spring.SqlSessionTemplate.insert(SqlSessionTemplate.java:272)\n" +
                "\tat com.baomidou.mybatisplus.core.override.MybatisMapperMethod.execute(MybatisMapperMethod.java:60)\n" +
                "\tat com.baomidou.mybatisplus.core.override.MybatisMapperProxy$PlainMethodInvoker.invoke(MybatisMapperProxy.java:148)\n" +
                "\tat com.baomidou.mybatisplus.core.override.MybatisMapperProxy.invoke(MybatisMapperProxy.java:89)\n" +
                "\tat com.sun.proxy.$Proxy156.insert(Unknown Source)\n" +
                "\tat com.baomidou.mybatisplus.extension.service.IService.save(IService.java:63)\n" +
                "\tat com.baomidou.mybatisplus.extension.service.IService$$FastClassBySpringCGLIB$$f8525d18.invoke(<generated>)\n" +
                "\tat org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\n" +
                "\tat org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:687)\n" +
                "\tat com.xt.framework.db.mysql.service.impl.LogServiceImpl$$EnhancerBySpringCGLIB$$ef9d28d5.save(<generated>)\n" +
                "\tat com.xt.framework.db.mysql.service.provider.LogServiceApiImpl.save(LogServiceApiImpl.java:31)\n" +
                "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
                "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n" +
                "\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n" +
                "\tat java.base/java.lang.reflect.Method.invoke(Method.java:566)\n" +
                "\tat org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)\n" +
                "\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)\n" +
                "\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:105)\n" +
                "\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:878)\n" +
                "\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:792)\n" +
                "\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\n" +
                "\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040)\n" +
                "\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943)\n" +
                "\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\n" +
                "\tat org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)\n" +
                "\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:652)\n" +
                "\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\n" +
                "\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:733)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\n" +
                "\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\n" +
                "\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.doFilterInternal(WebMvcMetricsFilter.java:93)\n" +
                "\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\n" +
                "\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)\n" +
                "\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)\n" +
                "\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:541)\n" +
                "\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143)\n" +
                "\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\n" +
                "\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)\n" +
                "\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\n" +
                "\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:374)\n" +
                "\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\n" +
                "\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:868)\n" +
                "\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1590)\n" +
                "\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\n" +
                "\tat java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)\n" +
                "\tat java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)\n" +
                "\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\n" +
                "\tat java.base/java.lang.Thread.run(Thread.java:834)\n" +
                "Caused by: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column 'return_data' at row 1\n" +
                "\tat com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:104)\n" +
                "\tat com.mysql.cj.jdbc.ClientPreparedStatement.executeInternal(ClientPreparedStatement.java:953)\n" +
                "\tat com.mysql.cj.jdbc.ClientPreparedStatement.execute(ClientPreparedStatement.java:370)\n" +
                "\tat com.alibaba.druid.filter.FilterChainImpl.preparedStatement_execute(FilterChainImpl.java:3461)\n" +
                "\tat com.alibaba.druid.wall.WallFilter.preparedStatement_execute(WallFilter.java:660)\n" +
                "\tat com.alibaba.druid.filter.FilterChainImpl.preparedStatement_execute(FilterChainImpl.java:3459)\n" +
                "\tat com.alibaba.druid.filter.FilterEventAdapter.preparedStatement_execute(FilterEventAdapter.java:440)\n" +
                "\tat com.alibaba.druid.filter.FilterChainImpl.preparedStatement_execute(FilterChainImpl.java:3459)\n" +
                "\tat com.alibaba.druid.filter.FilterAdapter.preparedStatement_execute(FilterAdapter.java:1081)\n" +
                "\tat com.alibaba.druid.filter.FilterChainImpl.preparedStatement_execute(FilterChainImpl.java:3459)\n" +
                "\tat com.alibaba.druid.proxy.jdbc.PreparedStatementProxyImpl.execute(PreparedStatementProxyImpl.java:167)\n" +
                "\tat com.alibaba.druid.pool.DruidPooledPreparedStatement.execute(DruidPooledPreparedStatement.java:497)\n" +
                "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
                "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n" +
                "\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n" +
                "\tat java.base/java.lang.reflect.Method.invoke(Method.java:566)\n" +
                "\tat org.apache.ibatis.logging.jdbc.PreparedStatementLogger.invoke(PreparedStatementLogger.java:59)\n" +
                "\tat com.sun.proxy.$Proxy210.execute(Unknown Source)\n" +
                "\tat org.apache.ibatis.executor.statement.PreparedStatementHandler.update(PreparedStatementHandler.java:47)\n" +
                "\tat org.apache.ibatis.executor.statement.RoutingStatementHandler.update(RoutingStatementHandler.java:74)\n" +
                "\tat com.baomidou.mybatisplus.core.executor.MybatisSimpleExecutor.doUpdate(MybatisSimpleExecutor.java:56)\n" +
                "\tat org.apache.ibatis.executor.BaseExecutor.update(BaseExecutor.java:117)\n" +
                "\tat com.baomidou.mybatisplus.core.executor.MybatisCachingExecutor.update(MybatisCachingExecutor.java:85)\n" +
                "\tat org.apache.ibatis.session.defaults.DefaultSqlSession.update(DefaultSqlSession.java:194)\n" +
                "\tat org.apache.ibatis.session.defaults.DefaultSqlSession.insert(DefaultSqlSession.java:181)\n" +
                "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
                "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n" +
                "\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n" +
                "\tat java.base/java.lang.reflect.Method.invoke(Method.java:566)\n" +
                "\tat org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:427)\n" +
                "\t... 66 common frames omitted\n" +
                "\n" +
                "Process finished with exit code 130\n";
        System.out.println(text.length()+"|||"+compress(text).length());
    }
}
