package com.cimctht.servicestation.common.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryUnimax",
        transactionManagerRef = "transactionManagerUnimax",
        basePackages = {"com.cimctht.servicestation.*.repository"}) //设置Repository所在位置
public class UnimaxJPAConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private HibernateProperties hibernateProperties;

    @Resource(name = "unimax")
    private DataSource unimaxDataSource;



    @Primary
    @Bean(name = "entityManagerUnimax")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryUnimax(builder).getObject().createEntityManager();
    }

    @Primary
    @Bean(name = "entityManagerFactoryUnimax")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryUnimax(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(unimaxDataSource)// 设置数据源
                .properties(jpaProperties.getProperties())// 设置jpa配置
                .properties(getVendorProperties())// 设置hibernate配置
                .packages("com.cimctht.servicestation.*.entity") //设置实体类所在位置
                .persistenceUnit("unimaxPersistenceUnit")// 设置持久化单元名，用于@PersistenceContext注解获取EntityManager时指定数据源
                .build();
    }

    private Map getVendorProperties() {
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
    }

    @Primary
    @Bean(name = "transactionManagerUnimax")
    public PlatformTransactionManager transactionManagerUnimax(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryUnimax(builder).getObject());
    }

    //禁用jackson的SerializationFeature.FAIL_ON_EMPTY_BEANS这条系列化规则来避免该异常
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}
