package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        Hibernate5Module module = new Hibernate5Module();
        module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, false);
        mapper.registerModule(module);
        return mapper;
    }
}
