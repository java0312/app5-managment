package uz.company.app5managment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.company.app5managment.entity.Turnstile;

import java.util.List;
import java.util.UUID;

@Repository
public interface TurnstileRepository extends JpaRepository<Turnstile, UUID> {

    List<Turnstile> findAllByUser_Id(UUID user_id);
}
