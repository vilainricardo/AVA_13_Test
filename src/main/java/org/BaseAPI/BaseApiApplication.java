package org.BaseAPI;

import org.BaseAPI.Module.User;
import org.BaseAPI.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BaseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApiApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository repo) {
        return args -> {
            repo.save(new User(1L, "Ricardo"));
            System.out.println("Usuário salvo com sucesso!");
        };
    }
}
