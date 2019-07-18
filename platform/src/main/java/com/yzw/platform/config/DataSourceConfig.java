package com.yzw.platform.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = {"com.yzw.platform.dao"}) // 扫描DAO
public class DataSourceConfig {

    public static final String DS1 = "dataSource1";

    public static final String DS2 = "dataSource2";

    public static final String DYNAMIC_DS = "dynamicDS";

    @Bean(name = DS1)
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DruidDataSource dataSource1() {
        return new DruidDataSource();
    }

    @Bean(name = DS2)
    @ConfigurationProperties(prefix = "spring.datasource.sqlserver")
    public DruidDataSource dataSource2() {
        return new DruidDataSource();
    }

    @Bean(name = DYNAMIC_DS)
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 将 dataSource1 数据源作为默认指定的数据源
        dynamicDataSource.setDefaultTargetDataSource(dataSource1());
        Map<Object, Object> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put("dataSource1", dataSource1());
        dataSourceMap.put("dataSource2", dataSource2());
        // 将 dataSource1 和 dataSource2 数据源作为指定的数据源
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        // 配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource作为数据源则不能实现切换
        sessionFactory.setDataSource(dynamicDataSource());
        sessionFactory.setTypeAliasesPackage("com.yzw.platform.entity");    // 扫描Model
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*/*Mapper.xml"));    // 扫描映射文件
        return sessionFactory;
    }

    /**
     * 自定义事务
     * MyBatis自动参与到spring事务管理中，无需额外配置，只要org.mybatis.spring.SqlSessionFactoryBean
     * 引用的数据源与DataSourceTransactionManager引用的数据源一致即可，否则事务管理会不起作用。
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        // 配置事务管理, 使用事务时在方法头部添加@Transactional注解即可
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}