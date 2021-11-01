package org.example.lesson_1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@PropertySource("classpath:myApp.properties")
public class MyConfig2 {

    @Bean
    @Scope("singleton")
    public Pet catBean() {
        return new Cat2();
    }

    @Bean
    public Person2 personBean() {
        return new Person2(catBean());
    }
}
