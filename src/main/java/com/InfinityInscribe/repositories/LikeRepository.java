package com.InfinityInscribe.repositories;

import com.InfinityInscribe.entities.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends MongoRepository<Like,String> {

}
