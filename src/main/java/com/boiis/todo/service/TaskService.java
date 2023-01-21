package com.boiis.todo.service;

import com.boiis.todo.Dao.TaskDao;
import com.boiis.todo.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskDao taskDao;

    public List<Task> getAllTasks(String userId){
        return taskDao.findTaskByUser(userId);
    }

    public boolean postTask(Task task)
    {
        taskDao.save(task);
        return true;
    }

    public boolean deleteTask(String id)
    {
        taskDao.deleteById(id);
        return true;
    }

    public boolean updateTask(Task task)
    {
        taskDao.save(task);
        return true;
    }
}
