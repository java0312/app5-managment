package uz.company.app5managment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.company.app5managment.entity.Role;
import uz.company.app5managment.entity.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);

}
