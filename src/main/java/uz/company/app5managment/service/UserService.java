package uz.company.app5managment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.company.app5managment.entity.Role;
import uz.company.app5managment.entity.User;
import uz.company.app5managment.entity.enums.RoleName;
import uz.company.app5managment.repository.RoleRepository;
import uz.company.app5managment.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public List<User> getUsers() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Role role = (Role) user.getRoles().toArray()[0];
        if (role.getRoleName().equals(RoleName.DIRECTOR)){
            return userRepository.findAll();
        }
        if (role.getRoleName().equals(RoleName.HR_MANAGER)){
            return userRepository.findByRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.WORKER)));
        }

        return null;
    }


    public User getUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null);
    }
}
