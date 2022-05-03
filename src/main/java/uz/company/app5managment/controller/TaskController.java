package uz.company.app5managment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.company.app5managment.entity.Task;
import uz.company.app5managment.entity.User;
import uz.company.app5managment.payload.ApiResponse;
import uz.company.app5managment.payload.TaskDto;
import uz.company.app5managment.service.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping
    public HttpEntity<?> addTask(@RequestBody TaskDto taskDto){
        ApiResponse apiResponse = taskService.addTask(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/taskDone")
    public HttpEntity<?> taskDone(@RequestParam String name){
        ApiResponse apiResponse = taskService.taskDone(name);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/byUserId/{userId}")
    public HttpEntity<?> getTasks(@PathVariable UUID userId){
        List<Task> tasks = taskService.getTasks(userId);
        return ResponseEntity.ok(tasks);
    }

}
