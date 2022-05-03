package uz.company.app5managment.payload;


import lombok.Data;

import java.util.UUID;

@Data
public class TaskDto {
    private String name;
    private String text;
    private Integer lifetime; //count of days
    private UUID toUserId;
}
