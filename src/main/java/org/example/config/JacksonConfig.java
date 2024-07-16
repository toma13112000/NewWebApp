package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Register Hibernate5Module to handle lazy loading
        Hibernate5Module hibernateModule = new Hibernate5Module();
        hibernateModule.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, false);
        mapper.registerModule(hibernateModule);

        // Register JavaTimeModule to handle java.time.* types (not necessary for Java 8+)
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }
}
