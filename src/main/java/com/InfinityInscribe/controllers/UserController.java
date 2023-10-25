package com.InfinityInscribe.controllers;

import com.InfinityInscribe.entities.Blog;
import com.InfinityInscribe.entities.User;
import com.InfinityInscribe.models.ResponseModel;
import com.InfinityInscribe.services.UserService;
import com.InfinityInscribe.shared.exceptions.InfinityInscribeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/user")
public class UserController {
    private  final UserService userService;
    private static final Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired
    public  UserController(UserService userService){
        this.userService= userService;
    }


    @GetMapping("/all")
    public ResponseEntity<ResponseModel> getAllUsers()  {
        try{
            List<User> blogs = userService.getAllUsers();
            return ResponseModel.Ok("All recent blogs fetched",blogs);
        } catch (Exception e){
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/me")
    public ResponseEntity<ResponseModel> GetMyProfile()
    {
        try{
            var user= userService.getUser();
            return ResponseModel.Ok("My user profile fetched", user);
        }
        catch (InfinityInscribeException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
