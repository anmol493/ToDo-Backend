package com.boiis.todo.Dao;

import com.boiis.todo.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TaskDao extends MongoRepository<Task, String> {

    @Query("{'user' : ?0}")
    public List<Task> findTaskByUser(String user);
}
