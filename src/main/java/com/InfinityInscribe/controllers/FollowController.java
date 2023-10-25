package com.InfinityInscribe.controllers;

import com.InfinityInscribe.entities.Blog;
import com.InfinityInscribe.models.ResponseModel;
import com.InfinityInscribe.services.FollowService;
import com.InfinityInscribe.shared.exceptions.InfinityInscribeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/follow")
public class FollowController {
    private FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @GetMapping("/sendRequest/{id}")
    public ResponseEntity<ResponseModel> swndFollowRequest(@PathVariable String id)  {
        try{
            var res = followService.sendRequest(id);
            return ResponseModel.Ok("All recent blogs fetched",res);
        }
        catch (InfinityInscribeException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
