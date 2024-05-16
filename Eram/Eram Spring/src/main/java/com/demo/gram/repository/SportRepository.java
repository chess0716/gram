package com.demo.gram.repository;


import com.demo.gram.entity.Sport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SportRepository extends MongoRepository<Sport, String> {

}