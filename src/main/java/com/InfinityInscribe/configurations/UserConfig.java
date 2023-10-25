package com.InfinityInscribe.configurations;

import com.InfinityInscribe.entities.Blog;
import com.InfinityInscribe.entities.User;
import com.InfinityInscribe.repositories.BlogRepository;
import com.InfinityInscribe.repositories.UserRepository;
import org.apache.catalina.Store;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

//Initializer
@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository,BlogRepository blogRepository, PasswordEncoder passwordEncoder){

        return args -> {
            var hashedPassword= passwordEncoder.encode("Abc123.");

            User admin=new User("dhvani2510@gmail.com",hashedPassword);

            admin.setJoiningDate("2023-10-01");
            admin.setUsername("dsheth");
            admin.setFirstName("dhvani");
            admin.setLastName("sheth");
            var existingIves= userRepository.findByEmail(admin.getEmail());

            if(existingIves.isEmpty()){
                userRepository.saveAll(List.of(admin));
                Blog testBlog = new Blog();
                testBlog.setTitle("Test Blog Title");
                testBlog.setContent("This is a test blog content.");
                testBlog.setAuthor(admin);

                blogRepository.save(testBlog);
            }
        };
    };
}
