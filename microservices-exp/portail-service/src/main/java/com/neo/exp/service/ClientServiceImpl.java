package com.neo.exp.service;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClientServiceImpl {
    /*@Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Map<String, DataSource> dataSourcesMtApp;

    @Override
    public String register(OrgDTO orgDTO) {
        boolean flag = false;
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Dsource[]> response = restTemplate.exchange("http://localhost:2021/base/getAll", HttpMethod.GET,
                new HttpEntity<Object>(headers), Dsource[].class);

        if (response.getBody() != null) {
            Dsource[] dsList = response.getBody();
            if (dsList != null && dsList.length > 0) {
                for (Dsource d : dsList) {
                    if (d.getTenantId().equalsIgnoreCase(orgDTO.getInstanceName())) {
                        flag = true;
                    }
                }
            }
        }

        if (flag) {
            return "tenant already exists!";
        }

        String url = "http://localhost:2021/base/addsource/" + orgDTO.getInstanceName();
        try {
            restTemplate.postForEntity(url, orgDTO, String.class);
        } catch (Exception e) {
        }

        DataSourceBuilder<?> factory = DataSourceBuilder.create(MultiTenancyJpaConfiguration.class.getClassLoader())
                .url("jdbc:mysql://localhost:3306/" + orgDTO.getInstanceName() + "?useSSL=false").username("root")
                .password("Password123#@!").driverClassName("com.mysql.cj.jdbc.Driver");
        HikariDataSource ds = (HikariDataSource) factory.build();
        ds.setKeepaliveTime(40000);
        ds.setMinimumIdle(1);
        ds.setMaxLifetime(45000);
        ds.setIdleTimeout(35000);
        dataSourcesMtApp.put(orgDTO.getInstanceName(), ds);
        TenantContextHolder.setTenantId(orgDTO.getInstanceName());
        return "tenant registered successfully!";
    }

    @Cacheable(value = "products", key = "#productId")
    public Optional<Product> getProduct(String productId) {
        sleep(6);
        return Optional.ofNullable(new Product(5, "p1"));
    }

    private void sleep(int seconds) {
        try {
            SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
}
