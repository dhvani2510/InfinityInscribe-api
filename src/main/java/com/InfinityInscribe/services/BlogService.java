package com.InfinityInscribe.services;

import com.InfinityInscribe.entities.Blog;
import com.InfinityInscribe.entities.Comment;
import com.InfinityInscribe.entities.Like;
import com.InfinityInscribe.entities.User;
import com.InfinityInscribe.models.CommentRequest;
import com.InfinityInscribe.models.CreateBlog;
import com.InfinityInscribe.repositories.BlogRepository;
import com.InfinityInscribe.repositories.CommentRepository;
import com.InfinityInscribe.repositories.LikeRepository;
import com.InfinityInscribe.shared.exceptions.InfinityInscribeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.InfinityInscribe.shared.helpers.StringHelper.StringIsNullOrEmpty;

@Service
public class BlogService {
    private BlogRepository blogRepository;
    private LikeRepository likeRepository;
    private static final Logger logger= LoggerFactory.getLogger(UserService.class);
    private CommentRepository commentRepository;

    @Autowired
    public BlogService(BlogRepository blogRepository, LikeRepository likeRepository, CommentRepository commentRepository) {
        this.blogRepository = blogRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    public List<Blog> getAllBlogsSortedByCreationTime() throws InfinityInscribeException {
//        Sort sortByCreationTimeDesc = Sort.by(Sort.Direction.DESC, "");
        return blogRepository.findAllSortedByLikesAndCommentsCount();
    }

    public Blog updateLike(String id) throws InfinityInscribeException {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(()-> new InfinityInscribeException("Error updating like"));
        if (blog != null) {
            // Add a like to the blog
            var auth= SecurityContextHolder.getContext().getAuthentication();
            if(!auth.isAuthenticated()){ // toggle this
                var details= auth.getDetails();
                logger.error("User {} is not authenticated", details);
                throw  new InfinityInscribeException("user is not authenticated");
            }
            var user= (User)auth.getPrincipal();
            boolean userHasLiked = blog.getLikes().stream()
                    .anyMatch(likes -> likes.getUserId().equals(user.getId()));

            if(!userHasLiked)  {
                Like like = new Like();
                like.setCreatedAt(LocalDate.now());
                like.setUserId(user.getId());
                like = likeRepository.save(like);
                blog.getLikes().add(like);
                var blog_new = blogRepository.save(blog);
            }
            else {
                logger.warn("User {} has already liked blog {}", user.getUsername(), blog.getId());
            }
        }
        return blog;
    }

    public Blog updateComment(CommentRequest comment) throws InfinityInscribeException {
        Blog blog = blogRepository.findById(comment.getId())
                .orElseThrow(()-> new InfinityInscribeException("Error updating like"));
        if (blog != null) {
            // Add a like to the blog
            var auth= SecurityContextHolder.getContext().getAuthentication();
            if(!auth.isAuthenticated()){ // toggle this
                var details= auth.getDetails();
                logger.error("User {} is not authenticated", details);
                throw  new InfinityInscribeException("user is not authenticated");
            }
            var user= (User)auth.getPrincipal();
            boolean userHasLiked = blog.getLikes().stream()
                    .anyMatch(likes -> likes.getUserId().equals(user.getId()));

          if(StringIsNullOrEmpty(comment.getComment()))
              throw new InfinityInscribeException("Comment cannot be empty");
            Comment c = new Comment();
            c.setContent(comment.getComment());
            c.setCreatedAt(LocalDate.now());
            c.setUserId(user.getId());
            commentRepository.save(c);
            blog.getComments().add(c);
            blog = blogRepository.save(blog);
        }
        return blog;
    }

    public List<Blog> getPopularBlogs() throws InfinityInscribeException {
        try {
            List<Blog> popularBlogs = blogRepository.findAll();

            // Sort the blogs in-memory
            popularBlogs.sort((blog1, blog2) -> {
                // First, compare likes in descending order
                int likesComparison = Integer.compare(blog2.getLikesCount(), blog1.getLikesCount());
                if (likesComparison != 0) {
                    return likesComparison;
                }

                // If likes are the same, compare comments in descending order
                int commentsComparison = Integer.compare(blog2.getCommentsCount(), blog1.getCommentsCount());
                if (commentsComparison != 0) {
                    return commentsComparison;
                }

                // If both likes and comments are the same, compare by id in descending order (recentness)
                return blog2.getId().compareTo(blog1.getId());
            });

            // Limit to the top 5 popular blogs in your code
            if (popularBlogs.size() > 5) {
                popularBlogs = popularBlogs.subList(0, 5);
            }

            return popularBlogs;
        }
        catch(Exception e) {
            throw new InfinityInscribeException("Error occured in fetching data");
        }
    }

    public Blog createBlog(CreateBlog createBlog) throws InfinityInscribeException {
        var auth= SecurityContextHolder.getContext().getAuthentication();
        if(!auth.isAuthenticated()){ // toggle this
            var details= auth.getDetails();
            logger.error("User {} is not authenticated", details);
            throw  new InfinityInscribeException("user is not authenticated");
        }
        var user= (User)auth.getPrincipal();
        Blog newBlog = new Blog();
        newBlog.setTitle(createBlog.getTitle());
        newBlog.setContent(createBlog.getDescription());
        newBlog.setCategory(createBlog.getCategory());
        newBlog.setAuthor(user);
        newBlog.setCreatedAt(LocalDateTime.now());
        return blogRepository.save(newBlog);
    }

    public List<Blog> getBlogsByUser(String id) throws InfinityInscribeException {
        var auth= SecurityContextHolder.getContext().getAuthentication();
        if(!auth.isAuthenticated()){ // toggle this
            var details= auth.getDetails();
            logger.error("User {} is not authenticated", details);
            throw  new InfinityInscribeException("user is not authenticated");
        }
        var user= (User)auth.getPrincipal();
        return  blogRepository.findByAuthor(user);
    }
}

