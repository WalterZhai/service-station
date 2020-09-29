package com.cimctht.servicestation.common.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryPlatform",
        transactionManagerRef = "transactionManagerPlatform",
        basePackages = {"com.cimctht.servicestation.dao.platform"}) //设置Repository所在位置
public class PlatformJPAConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private HibernateProperties hibernateProperties;

    @Autowired
    @Qualifier("platform")
    private DataSource platformDataSource;

    @Bean(name = "entityManagerPlatform")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryPlatform(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryPlatform")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPlatform(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(platformDataSource)
                .properties(jpaProperties.getProperties())// 设置jpa配置
                .properties(getVendorProperties())
                .packages("com.cimctht.servicestation.*.entity") //设置实体类所在位置
                .persistenceUnit("platformPersistenceUnit")
                .build();
    }

    private Map getVendorProperties() {
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
    }

    @Bean(name = "transactionManagerPlatform")
    public PlatformTransactionManager transactionManagerPlatform(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryPlatform(builder).getObject());
    }

}
