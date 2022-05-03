package uz.company.app5managment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.company.app5managment.entity.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findByName(String name);

    List<Task> findAllByToUser_Id(UUID toUser_id);
}
