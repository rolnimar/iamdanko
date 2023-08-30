package com.danko.filebackend.model.dto;


import com.danko.filebackend.model.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileData {
    private String composer;
    private Difficulty difficulty;
    private String title;
}
