package com.hummingbird.backend.food.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Setter
@Getter
public class UpdateFoodDto {
    private String name;
    private int price;
    private String content;
    private String origFileName;
    private String fileName;
    private String filePath;
}
