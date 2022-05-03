package uz.company.app5managment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.company.app5managment.entity.Role;
import uz.company.app5managment.entity.User;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);

    Optional<User> findByEmail(String email);

    List<User> findByRoles(Set<Role> roles);


}
