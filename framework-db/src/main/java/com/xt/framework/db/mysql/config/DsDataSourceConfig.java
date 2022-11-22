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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

*/
/**
 * @author tao.xiong
 * @Description 多数据源-ds
 * @Date 2022/11/22 15:14
 *//*

@Slf4j
@Configuration
@MapperScan(basePackages = DsDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "dsSqlSessionFactory")
public class DsDataSourceConfig {
    static final String PACKAGE = "com.xt.framework.db.mysql.mapper.ds";
    static final String MAPPER_LOCATION = "classpath*:mapper/ds/*.xml";

    @Bean(name = "dsDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.ds")
    public DataSource dsDataSource() {
        log.info("ds配置成功");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dsTransactionManager")
    public DataSourceTransactionManager dsTransactionManager() {
        return new DataSourceTransactionManager((dsDataSource()));
    }

    @Bean(name = "dsSqlSessionFactory")
    public SqlSessionFactory dsSqlSessionFactory(@Qualifier("dsDataSource") DataSource dsDatasource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dsDatasource);
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(DsDataSourceConfig.MAPPER_LOCATION)
        );
        return sessionFactory.getObject();
    }
}
*/
