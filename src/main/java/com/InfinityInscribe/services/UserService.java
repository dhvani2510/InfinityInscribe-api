package com.InfinityInscribe.services;

import com.InfinityInscribe.configurations.JwtService;
import com.InfinityInscribe.entities.Blog;
import com.InfinityInscribe.entities.User;
import com.InfinityInscribe.models.AuthenticationRequest;
import com.InfinityInscribe.models.AuthenticationResponse;
import com.InfinityInscribe.models.RegisterRequest;
import com.InfinityInscribe.models.RegisterResponse;
import com.InfinityInscribe.repositories.UserRepository;
import com.InfinityInscribe.shared.exceptions.InfinityInscribeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static com.InfinityInscribe.shared.helpers.StringHelper.StringIsNullOrEmpty;

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

        if(StringIsNullOrEmpty(authenticationRequest.getEmail()))
            throw  new InfinityInscribeException("Email is empty");
        if(StringIsNullOrEmpty(authenticationRequest.getPassword()))
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

    public boolean validateUsername(String username) throws InfinityInscribeException {
        if(StringIsNullOrEmpty(username))
            throw new InfinityInscribeException("Password is empty");
        var result = userRepository.countByUsername(username);
        return !(result > 0);
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws InfinityInscribeException {

        logger.info("User is registering with email {} and name {}", registerRequest.getEmail(), registerRequest.getFirstName());
        // Check if auto mapper exist in Java
        if(StringIsNullOrEmpty(registerRequest.getEmail()))
            throw  new InfinityInscribeException("Email is empty");
        if(!emailIsValid(registerRequest.getEmail()))
            throw  new InfinityInscribeException("Email is not valid");
        if(StringIsNullOrEmpty(registerRequest.getPassword()))
            throw new InfinityInscribeException("Password is empty");

        if(StringIsNullOrEmpty(registerRequest.getUsername()))
            throw new InfinityInscribeException("Username is empty");

        if(StringIsNullOrEmpty(registerRequest.getFirstName()))
            throw new InfinityInscribeException("Name is empty");
        if(StringIsNullOrEmpty(registerRequest.getLastName()))
            throw new InfinityInscribeException("Surname is empty");

        var existinguser= userRepository.findByEmail(registerRequest.getEmail());

        if(!existinguser.isEmpty())
        {
            logger.error("Email is already in use");
            throw new InfinityInscribeException("Email is already in use");
        }
        var hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        var user= new User.UserBuilder(registerRequest.getFirstName(),registerRequest.getLastName())
                .setEmail(registerRequest.getEmail())
                .setPassword(hashedPassword)
                .setUsername(registerRequest.getUsername())
                .setBio(registerRequest.getBio())
                .build();

        userRepository.save(user);

        var response= new RegisterResponse(user.getId(),user.getEmail(),user.getFirstName(), user.getLastName(),user.getUsername(), user.getBio());
        return  response;
    }

    private boolean emailIsValid(String email) {
        var EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return EMAIL.matcher(email).matches();
    }

    public User getUser() throws InfinityInscribeException {
        logger.info("Getting user profile");
        var auth= SecurityContextHolder.getContext().getAuthentication();
        if(!auth.isAuthenticated()){ // toggle this
            var details= auth.getDetails();
            logger.error("User {} is not authenticated", details);
            throw  new InfinityInscribeException("user is not authenticated");
        }
        var user= (User)auth.getPrincipal();
//        var u= userRepository.findById(user.Id)
//                .orElseThrow(()-> new ArclightException("User not found"));
        if(user== null)
            throw  new InfinityInscribeException("User not found");

        return user;
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
