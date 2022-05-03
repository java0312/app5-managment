package uz.company.app5managment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.company.app5managment.entity.Wages;

import java.util.List;
import java.util.UUID;

@Repository
public interface WagesRepository extends JpaRepository<Wages, UUID> {

    List<Wages> findAllByUser_Id(UUID user_id);
}
