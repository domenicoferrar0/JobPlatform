package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.exceptions.FileHandlingException;
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

@Service
public class FileService {

    @Value("${app.basePath}")
    private String PATH;

    private final static Long MAX_SIZE = 1048576L;


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

    public String savePdf(MultipartFile file, String cf) {
        String fileName = cf.concat(".pdf");
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
}
