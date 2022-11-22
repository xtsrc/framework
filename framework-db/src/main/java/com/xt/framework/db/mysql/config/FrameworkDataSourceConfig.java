/*
package com.xt.framework.db.mysql.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

*/
/**
 * @author tao.xiong
 * @Description 多数据源-framework
 * @Date 2022/11/22 15:14
 *//*

@Slf4j
@Configuration
@MapperScan(basePackages = FrameworkDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "frameworkSqlSessionFactory")
public class FrameworkDataSourceConfig {
    static final String PACKAGE = "com.xt.framework.db.mysql.mapper.framework";
    static final String MAPPER_LOCATION = "classpath*:mapper/framework/*.xml";

    @Primary
    @Bean(name = "frameworkDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.framework")
    public DataSource frameworkDataSource() {
        log.info("framework配置成功");
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "frameworkTransactionManager")
    public DataSourceTransactionManager frameworkTransactionManager() {
        return new DataSourceTransactionManager((frameworkDataSource()));
    }

    @Primary
    @Bean(name = "frameworkSqlSessionFactory")
    public SqlSessionFactory frameworkSqlSessionFactory(@Qualifier("frameworkDataSource") DataSource frameworkDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(frameworkDataSource);
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(FrameworkDataSourceConfig.MAPPER_LOCATION)
        );
        return sessionFactory.getObject();
    }
}
*/
