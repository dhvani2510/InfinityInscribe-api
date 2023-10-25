package com.InfinityInscribe.repositories;

import com.InfinityInscribe.entities.Blog;
import com.InfinityInscribe.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends MongoRepository<Blog,String> {
    @Query(value = "{}", sort = "{ 'likesCount' : -1, 'commentsCount' : -1 }")
    List<Blog> findAllSortedByLikesAndCommentsCount();

    List<Blog> findByAuthor(User user);
}
