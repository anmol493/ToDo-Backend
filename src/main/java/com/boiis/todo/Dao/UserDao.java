package com.boiis.todo.Dao;

import com.boiis.todo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserDao extends MongoRepository<User, String> {
}
