package com.InfinityInscribe.services;

import com.InfinityInscribe.configurations.JwtService;
import com.InfinityInscribe.models.AuthenticationRequest;
import com.InfinityInscribe.models.AuthenticationResponse;
import com.InfinityInscribe.repositories.UserRepository;
import com.InfinityInscribe.shared.exceptions.InfinityInscribeException;
import com.InfinityInscribe.shared.helpers.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserService {
    private  final UserRepository userRepository;
    private  final JwtService jwtService;
    private  final AuthenticationManager authenticationManager;
    private static final Logger logger= LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;

    @Autowired
   public UserService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, PasswordEncoder passwordEncoder1) {
       this.userRepository = userRepository;
       this.jwtService = jwtService;
       this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder1;
    }


    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws InfinityInscribeException {

        var auth= SecurityContextHolder.getContext().getAuthentication();
        var details =auth.getDetails().toString();
        logger.info("User {} is logging in with email {}", details,authenticationRequest.getEmail());

        if(StringHelper.StringIsNullOrEmpty(authenticationRequest.getEmail()))
            throw  new InfinityInscribeException("Email is empty");
        if(StringHelper.StringIsNullOrEmpty(authenticationRequest.getPassword()))
            throw new InfinityInscribeException("Password is empty");

        var user= userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(()-> new InfinityInscribeException("User not found"));

        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        var extraClaims = new HashMap<String, Object>();
        var jwtToken= jwtService.GenerateToken(extraClaims, user);
        return new AuthenticationResponse.Builder()
                //.builder()
                .setToken(jwtToken)
                .build();
    }

    private  void authenticate(String email, String password) throws InfinityInscribeException {

        try{
            var result = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    email,
                    password
            ));
            logger.info("User authenticated with the right credentials");
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw  new InfinityInscribeException("Wrong email or password");
        }

    }

}
