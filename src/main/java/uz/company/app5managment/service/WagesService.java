package uz.company.app5managment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.company.app5managment.entity.User;
import uz.company.app5managment.entity.Wages;
import uz.company.app5managment.entity.WagesDto;
import uz.company.app5managment.payload.ApiResponse;
import uz.company.app5managment.repository.UserRepository;
import uz.company.app5managment.repository.WagesRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WagesService {

    @Autowired
    WagesRepository wagesRepository;

    @Autowired
    UserRepository userRepository;


    public ApiResponse addWages(WagesDto wagesDto) {

        Optional<User> optionalUser = userRepository.findById(wagesDto.getUserId());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();

            Wages wages = new Wages();
            wages.setGivenDate(new Date());
            wages.setPrice(wagesDto.getPrice());
            wages.setUser(user);
            wagesRepository.save(wages);

            return new ApiResponse("Wages added!", true);
        }

        return new ApiResponse("User not found!", false);
    }

    public List<Wages> getWagesByUserId(UUID userId) {
        List<Wages> allByUser_id = wagesRepository.findAllByUser_Id(userId);
        return allByUser_id;
    }
}
