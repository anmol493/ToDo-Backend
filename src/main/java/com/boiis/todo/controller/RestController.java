package com.boiis.todo.controller;

import com.boiis.todo.helper.JsonFormatter;
import com.boiis.todo.helper.JwtToken;
import com.boiis.todo.model.Task;
import com.boiis.todo.model.User;
import com.boiis.todo.service.TaskService;
import com.boiis.todo.service.UserService;
import com.boiis.todo.validator.TodoException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
@CrossOrigin("http://localhost:3000")
public class RestController {

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private JsonFormatter jsonFormatter;

    private String getSuccessResponse(){
        return jsonFormatter.createMapToJson(new HashMap<>(Map.of("status","success")));
    }

    private String getFailureResponse(){
        return jsonFormatter.createMapToJson(new HashMap<>(Map.of("status","fail")));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks(@RequestHeader Map<String,String> headers)
    {
        try {
            Map<String,String> jsonObject = checkAuthentication(headers.get("token"));
            return new ResponseEntity<>(taskService.getAllTasks(jsonObject.get("userId")), HttpStatus.OK);
        }
        catch(TodoException e)
        {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/tasks")
    public ResponseEntity<String> addTask(@RequestBody Task task, @RequestHeader Map<String,String> headers) {
        try{
            Map<String,String> jsonObject = checkAuthentication(headers.get("token"));
            task.setUser(jsonObject.get("userId"));
            taskService.postTask(task);
            return new ResponseEntity<>(getSuccessResponse(), HttpStatus.OK);}
        catch(TodoException e){
            return new ResponseEntity<>(getFailureResponse(), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<String> taskCompleted(@PathVariable(value = "taskId") String id, @RequestHeader Map<String,String> headers)
    {
        try {
            checkAuthentication(headers.get("token"));
            taskService.deleteTask(id);
            return new ResponseEntity<>(getSuccessResponse(), HttpStatus.OK);
        } catch (TodoException e) {
            return new ResponseEntity<>(getFailureResponse(), HttpStatus.UNAUTHORIZED);
        }

    }

    @PutMapping("/tasks")
    public ResponseEntity<String> updateTask(@RequestBody Task task, @RequestHeader Map<String,String> headers)
    {
        try {
            checkAuthentication(headers.get("token"));
            taskService.updateTask(task);
            return new ResponseEntity<>(getSuccessResponse(), HttpStatus.OK);
        } catch (TodoException e) {
            return new ResponseEntity<>(getFailureResponse(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<String> addUser(@RequestBody User user){
        if(userService.addUser(user))
            return new ResponseEntity<>(getSuccessResponse() , HttpStatus.OK);
        else return new ResponseEntity<>(getFailureResponse(),HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/login")
    public ResponseEntity<String> verifyUser(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password)
    {
        if(userService.auth(email, password))
        {
            String token = jwtToken.generateJwtToken(email);
            return new ResponseEntity<>(jsonFormatter.createMapToJson(new HashMap<>(Map.of("status" , "success","token", token))), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(getFailureResponse() , HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestParam(name = "token") String token)
    {
        return jwtToken.isTokenExpired(token) ? new ResponseEntity<>(getFailureResponse(), HttpStatus.UNAUTHORIZED) : new ResponseEntity<>(jsonFormatter.createMapToJson(new HashMap<>(Map.of("status" , "success","userId", jwtToken.getUserIdFromToken(token)))),HttpStatus.OK);
    }

    private Map<String,String> checkAuthentication(String token) throws TodoException {
        RestTemplate restTemplate = new RestTemplate();
        String auth = restTemplate.getForObject(String.format("http://localhost:8080/authenticate?token=%s",token),String.class);
        Map<String,String> jsonObject = new Gson().fromJson(auth, HashMap.class);
        if(jsonObject.get("status").equals("fail"))
            throw new TodoException("Server Error");
        return jsonObject;
    }

}
