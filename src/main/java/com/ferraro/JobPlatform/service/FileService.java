package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.exceptions.FileHandlingException;
import com.ferraro.JobPlatform.model.document.JobAppliance;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    @Value("${app.basePath}")
    private String PATH;


    private final Long MAX_SIZE = 1048576L; //10MB

    /*La logica di validazione del formato del file
    * viene effettuata tramite Tika */
    public boolean isValid(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getSize() > MAX_SIZE) {
            return false;
        }
        MediaType mediaType;
        try {
            TikaInputStream tika = TikaInputStream.get(file.getInputStream());
            Detector detector = new DefaultDetector();
            mediaType = detector.detect(tika, new Metadata());
            tika.close();
        } catch (IOException ex) {
            throw new FileHandlingException();
        }
        return mediaType.equals(MediaType.application("pdf"));
    }

    /*Il file viene salvato con un nome randomico
    * e ritorna il nome del file in modo da poterlo
    * assegnare all'appliance come referencing */
    public String savePdf(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + ".pdf";
        File destination = new File(PATH, fileName);
        try {
            file.transferTo(destination);
        } catch (IllegalStateException | IOException ex) {
            throw new FileHandlingException();
        }
        return fileName;
    }

    public boolean delete(String cvPath) {
        File file = new File(PATH, cvPath);
        try {
            return file.delete();
        } catch (SecurityException e) {
            throw new FileHandlingException();
        }
    }

    //Per cancellare tutti i file in caso venga cancellato l'annuncio
    public boolean fileCleanUp(List<JobAppliance> appliances) {
        for (JobAppliance appliance : appliances) {
            delete(appliance.getCvPath());
        }
        return true;
    }

    public byte[] getFile(String cvPath) {
        try {
            Path filePath = Paths.get(PATH, cvPath);
            return Files.readAllBytes(filePath);
        }
        catch(IOException | RuntimeException e){
            throw new FileHandlingException();
        }
    }
}
