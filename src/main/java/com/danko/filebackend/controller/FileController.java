package com.danko.filebackend.controller;


import com.danko.filebackend.model.dto.FileData;
import com.danko.filebackend.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public String uploadImage(FileData fileData,
                              @RequestParam("file") MultipartFile file) throws IOException {
        if(file.getOriginalFilename() == null){
            log.error("File name is null");
            return "File name is null";
        }
        return fileService.saveFile(file, fileData);
    }
}


