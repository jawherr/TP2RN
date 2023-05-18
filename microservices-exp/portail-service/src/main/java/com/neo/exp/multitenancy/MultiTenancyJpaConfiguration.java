package com.neo.exp.multitenancy;

import io.quarkus.hibernate.orm.runtime.HibernateOrmRecorder;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ext.ParamConverter;

@Configuration
@Transactional
@ParamConverter.Lazy
public class MultiTenancyJpaConfiguration {

    @Inject
    private HibernateOrmRecorder properties;

    @Inject
    private RestClientBuilder rs;


    /*@Primary
    @Bean(name = "dataSourcesMtApp")
    public Map<String, DataSource> dataSourcesMtApp() {
        System.out.println("****************************************");
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Dsource[]> response =
                rs.exchange(
                        "http://localhost:2021/base/getAll", HttpMethod.GET, new HttpEntity<Object>(headers),
                        Dsource[].class);

        Dsource[] dsList = response.getBody();

        Map<String, DataSource> result = new HashMap<>();
        for (Dsource source :dsList) {
            DataSourceBuilder<?> factory = DataSourceBuilder.create().url(source.getUrl())
                    .username(source.getUsername()).password(source.getPassword())
                    .driverClassName(source.getDriverClassName());

            HikariDataSource ds = (HikariDataSource)factory.build();
            ds.setKeepaliveTime(40000);
            ds.setMinimumIdle(1);
            ds.setMaxLifetime(45000);
            ds.setIdleTimeout(35000);
            result.put(source.getTenantId(), ds);
        }

        return result;
    }

    @Bean
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        return new DataSourceBasedMultiTenantConnectionProviderImpl();
    }

    @Bean
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolverImpl();
    }

    @Bean(name="entityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
            AbstractDataSourceBasedMultiTenantConnectionProviderImpl multiTenantConnectionProvider,
            CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

        Map<String, Object> hibernateProps = new LinkedHashMap<>();
        hibernateProps.putAll(this.jpaProperties.getProperties());
        hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
        hibernateProps.put("hibernate.hbm2ddl.auto", "update");
        hibernateProps.put("hibernate.dialact", "org.hibernate.dialect.MySQL8Dialect");
        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setPackagesToScan("com.ClientService.ClientService");
        result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        result.setJpaPropertyMap(hibernateProps);
        return result;
    }

    @Bean
    @Primary
    public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return entityManagerFactoryBean.getObject();
    }
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }*/
}
