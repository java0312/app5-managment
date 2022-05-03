package uz.company.app5managment.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.company.app5managment.entity.Role;
import uz.company.app5managment.entity.Task;
import uz.company.app5managment.entity.User;
import uz.company.app5managment.entity.enums.RoleName;
import uz.company.app5managment.payload.ApiResponse;
import uz.company.app5managment.payload.TaskDto;
import uz.company.app5managment.repository.TaskRepository;
import uz.company.app5managment.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender javaMailSender;

    public ApiResponse addTask(TaskDto taskDto) {

        Optional<User> optionalUser = userRepository.findById(taskDto.getToUserId());
        if (optionalUser.isEmpty())
            return new ApiResponse("This user not found!", false);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Role role = (Role) principal.getRoles().toArray()[0];
        boolean isDirector = role.getAuthority().equals(RoleName.DIRECTOR.name());
        boolean isManager = role.getAuthority().equals(RoleName.HR_MANAGER.name());

        if (isManager && optionalUser.get().getRoles().toArray()[0].equals(RoleName.WORKER) || isDirector) {
            Task task = new Task();
            task.setFromUser(principal);
            task.setToUser(optionalUser.get());
            task.setName(taskDto.getName());
            task.setText(taskDto.getText());
            task.setExpireDate(new Date(System.currentTimeMillis() + taskDto.getLifetime() * 24 * 60 * 60 * 1000));
            Task savedTask = taskRepository.save(task);

            boolean sending = sendEmailAboutTask(optionalUser.get().getEmail(), savedTask);

            return new ApiResponse("Task is given", true);
        }

        return new ApiResponse("You cannot give a task to this user", false);
    }

    public boolean sendEmailAboutTask(String sendingEmail, Task task) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Test@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Task is given you!\n" + task.getName());
            mailMessage.setText(
                    "Expire Date" + task.getExpireDate() + "\n" +
                            "Your task: " + task.getText() + "\n" +
                            "I did this task --> " + " http://localhost:8080/api/task/taskDone?taskName=" + task.getName()
            );
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ApiResponse taskDone(String name) {

        System.out.println();
        Optional<Task> optionalTask = taskRepository.findByName(name);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setDone(true);
            taskRepository.save(task);
            boolean b = sendEmailTaskIsDone(task.getFromUser().getEmail(), task);
            return new ApiResponse("Task is done!", true);
        }

        return new ApiResponse("Task not done!", true);
    }

    public boolean sendEmailTaskIsDone(String sendingEmail, Task task) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Test@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Task is done!");
            mailMessage.setText(
                "I did task!" + task.getName()
            );
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Task> getTasks(UUID userId) {
        List<Task> allByToUser_id = taskRepository.findAllByToUser_Id(userId);
        return allByToUser_id;
    }
}
