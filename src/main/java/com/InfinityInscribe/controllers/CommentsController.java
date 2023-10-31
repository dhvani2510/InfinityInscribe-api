package com.InfinityInscribe.controllers;

import com.InfinityInscribe.entities.Blog;
import com.InfinityInscribe.models.CommentRequest;
import com.InfinityInscribe.models.ResponseModel;
import com.InfinityInscribe.services.BlogService;
import com.InfinityInscribe.shared.exceptions.InfinityInscribeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comment")
public class CommentsController {

    private BlogService blogService;

    @Autowired
    public CommentsController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseModel> updateComment(@RequestBody CommentRequest comment)  {
        try{
            Blog blog = blogService.updateComment(comment);
            return ResponseModel.Ok("Blog updated",blog);
        }
        catch (InfinityInscribeException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModel> deleteComment(@PathVariable String id) {
        blogService.deleteComment(id);
        return ResponseModel.Ok("Blog updated","Comment deletedSuccessfully");
    }
}
