package com.boiis.todo.service;

import com.boiis.todo.Dao.UserDao;
import com.boiis.todo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public boolean addUser(User user){
        if(userDao.findById(user.getEmail()).isPresent())
            return false;
        userDao.save(user);
        return true;
    }

    public boolean auth(String email, String password){
        return userDao.findById(email).get().getPassword().equals(password);

    }
}
