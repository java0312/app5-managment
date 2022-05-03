package uz.company.app5managment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.company.app5managment.entity.Role;
import uz.company.app5managment.entity.Turnstile;
import uz.company.app5managment.entity.User;
import uz.company.app5managment.entity.enums.RoleName;
import uz.company.app5managment.payload.ApiResponse;
import uz.company.app5managment.payload.LoginDto;
import uz.company.app5managment.payload.RegisterDto;
import uz.company.app5managment.repository.RoleRepository;
import uz.company.app5managment.repository.TurnstileRepository;
import uz.company.app5managment.repository.UserRepository;
import uz.company.app5managment.security.JwtProvider;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TurnstileRepository turnstileRepository;

    /*
     * http://localhost:8080/api/auth/register
     * */
    public ApiResponse registerUser(RegisterDto registerDto) {

        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail)
            return new ApiResponse("this user already registred!", false);


        User user = new User();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().toString().equals("anonymousUser"))
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.DIRECTOR)));
        else {
            User principal = (User) authentication.getPrincipal();

            for (Role role : principal.getRoles()) {
                if (role.equals(RoleName.DIRECTOR))
                    user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.HR_MANAGER)));
                if (role.equals(RoleName.HR_MANAGER))
                    user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.WORKER)));
            }

        }

        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        user.setEmailCode(UUID.randomUUID().toString());
        User savedUser = userRepository.save(user);

        boolean sendEmail = sendEmail(user.getEmail(), user.getEmailCode());
        if (!sendEmail)
            return new ApiResponse("Error in sending message to account", false);

        return new ApiResponse("You successfully registered", true);
    }

    /*
     * Sending email
     * */
    public boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Test@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Confirm account!");
            mailMessage.setText(
                    "You confirm with these link\n" +
                            "http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail
            );
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("user confirmed", true);
        }

        return new ApiResponse("user already confirmed", false);
    }


    public ApiResponse login(LoginDto loginDto) {

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()
            ));

            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(user.getEmail(), user.getRoles());


            Turnstile turnstile = new Turnstile();
            turnstile.setUser(user);
            turnstile.setEnter(true);
            turnstileRepository.save(turnstile);


            return new ApiResponse("Token", true, token);
            /*
             * to take a token
             * */
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Email or password is wrong!", false);
        }

    }

    public void logout() {

        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Turnstile turnstile = new Turnstile();
        turnstile.setUser(principal);
        turnstile.setEnter(false);
        turnstileRepository.save(turnstile);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UsernameNotFoundException(username + " not found!");
    }
}
