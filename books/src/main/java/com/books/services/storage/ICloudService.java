package com.books.services.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICloudService {

    String uploadFile(MultipartFile file, String objectName) throws IOException;

    void deleteFile(String fileUrl);
}
