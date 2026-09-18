package com.bootcamp.pokemonservice.util;

import io.minio.*;
import io.minio.errors.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioUtil {

    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public String uploadMultipart(MultipartFile file, String filename) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")){
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String finalName;
        if (filename != null && !filename.isEmpty()){
            finalName = filename.contains(".") ? filename : filename + extension;
        } else {
            finalName = UUID.randomUUID().toString() + extension;
        }

        try (InputStream inputStream = file.getInputStream()){
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(finalName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("gagal saat ke minio");
        }

        return filename;
    }

    public void downloadFile(String objectName, HttpServletResponse response) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        StatObjectResponse meta = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );

        response.setContentType(meta.contentType());
        response.setHeader(
                "Content-Disposition",
                "attachment: filename=\"" + objectName +"\""
        );

        InputStream objectStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );

        response.setContentLengthLong(meta.size());
        try(InputStream input = objectStream){
            input.transferTo(response.getOutputStream());
            response.getOutputStream().flush();
        }
    }
}
