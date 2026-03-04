package com.web.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:messages.yaml",
        factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "messages")
public class MessagesProperties {

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getGoodbye() {
        return goodbye;
    }

    public void setGoodbye(String goodbye) {
        this.goodbye = goodbye;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    private String welcome;
    private String goodbye;
    private Errors errors;

    public static class Errors {
        private String notFound;
        private String unauthorized;

        public String getNotFound() {
            return notFound;
        }

        public void setNotFound(String notFound) {
            this.notFound = notFound;
        }

        public String getUnauthorized() {
            return unauthorized;
        }

        public void setUnauthorized(String unauthorized) {
            this.unauthorized = unauthorized;
        }
// getters & setters
    }

    // getters & setters
}
