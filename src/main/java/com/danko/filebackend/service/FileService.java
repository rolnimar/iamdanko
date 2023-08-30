package com.danko.filebackend.service;

import com.danko.filebackend.configuration.SaveProperties;
import com.danko.filebackend.model.Composer;
import com.danko.filebackend.model.Sheet;
import com.danko.filebackend.model.dto.FileData;
import com.danko.filebackend.repository.ComposerRepository;
import com.danko.filebackend.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final SaveProperties saveProperties;
    private final FileRepository fileRepository;
    private final ComposerRepository composerRepository;

    public String saveFile(MultipartFile file, FileData fileData) throws IOException {
        Composer composer = saveComposer(fileData);
        Path pathToSave = saveProperties.getSavePath();
        String fileName = file.getOriginalFilename();
        Files.write(pathToSave.resolve(fileName), file.getBytes());
        log.info("File saved to {}", pathToSave);
        String imageName = convertFirstPdfPageToImageAndSave(file);
        Sheet sheet = Sheet.builder().title(fileData.getTitle())
                .difficulty(fileData.getDifficulty())
                .composer(composer)
                .pdfName(fileName)
                .imageName(imageName)
                .build();
        fileRepository.save(sheet);
        log.info("Full pwd to saved file is {}", pathToSave.resolve(fileName));
        return "File saved to " + pathToSave;
    }

    private Composer saveComposer(FileData fileData) {
        Composer composer = Composer.builder().name(fileData.getComposer()).build();
        Composer foundComposer = composerRepository.findComposerByName(fileData.getComposer());
        if (foundComposer != null) {
            return foundComposer;
        }
        return composerRepository.save(composer);
    }

    private String convertFirstPdfPageToImageAndSave(MultipartFile pdfFile) throws IOException {
        PDDocument document = Loader.loadPDF(pdfFile.getBytes());
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
        String imageFileName = Objects.requireNonNull(pdfFile.getOriginalFilename()).replaceFirst("[.][^.]+$", ".png");

        // Save the image
        Path imagePath = Paths.get(saveProperties.getSavePath().toString(), imageFileName);
        ImageIO.write(bim, "png", imagePath.toFile());
        return imageFileName;
    }
}
