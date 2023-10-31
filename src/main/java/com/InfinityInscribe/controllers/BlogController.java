package com.InfinityInscribe.controllers;

import com.InfinityInscribe.entities.Blog;
import com.InfinityInscribe.models.*;
import com.InfinityInscribe.services.BlogService;
import com.InfinityInscribe.shared.exceptions.InfinityInscribeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    private BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/recent")
    public ResponseEntity<ResponseModel> getAllBlogsSortedByTime() {
        try{
            List<Blog> blogs = blogService.getAllBlogsSortedByCreationTime();
            return ResponseModel.Ok("All recent blogs fetched",blogs);
        }
        catch (InfinityInscribeException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<ResponseModel> getAllPopularBlogs() {
        try{
            List<Blog> blogs = blogService.getPopularBlogs();
            return ResponseModel.Ok("All recent blogs fetched",blogs);
        }
        catch (InfinityInscribeException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/like")
    public ResponseEntity<ResponseModel> likeBlog(@RequestBody LikeRequest like) {
        try{
            Blog blog = blogService.updateLike(like.getId());
            return ResponseModel.Ok("Blog updated",blog);
        }
        catch (InfinityInscribeException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<ResponseModel> createBlog(@RequestBody CreateBlog createBlog)  {
        try{
            var blog = blogService.createBlog(createBlog);
            return ResponseModel.Ok("All recent blogs fetched",blog);
        }
        catch (InfinityInscribeException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBlog/{id}")
    public ResponseEntity<ResponseModel> getBlogById(@PathVariable String id) {
        try{
            var blog = blogService.getBlogById(id);
            return ResponseModel.Ok("All recent blogs fetched",blog);
        }
        catch (InfinityInscribeException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getNyBlog/{id}")
    public ResponseEntity<ResponseModel> getBlogsByUser(@PathVariable String id) {
        try{
            var blog = blogService.getBlogByUser(id);
            return ResponseModel.Ok("All recent blogs fetched",blog);
        }
        catch (InfinityInscribeException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<ResponseModel> updateBlog(@RequestBody UpdateBlogRequest request) {
        try{
            var blog = blogService.updateBlog(request);
            return ResponseModel.Ok("Blog updated successfully",blog);
        }
        catch (InfinityInscribeException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModel> deleteBlog(@PathVariable String id) {
        blogService.deleteBlog(id);
        return ResponseModel.Ok("Success","Blog deleted");
    }
}

