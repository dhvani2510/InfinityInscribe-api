package com.InfinityInscribe.configurations;

import com.InfinityInscribe.entities.User;
import com.InfinityInscribe.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

//Initializer
@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder){

        return args -> {
            var hashedPassword= passwordEncoder.encode("Abc123.");

            User admin=new User("dhvani2510@gmail.com",hashedPassword);

            admin.setJoiningDate("2023-10-01");
            var existingIves= userRepository.findByEmail(admin.email);

            if(existingIves.isEmpty()){
                userRepository.saveAll(List.of(admin));
            }
        };
    };
}
