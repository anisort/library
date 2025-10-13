package com.ai.books.assistant.services.storage.gcp;

import com.ai.books.assistant.services.storage.ICloudService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class GcsService implements ICloudService {

    private final Storage storage;
    private final String bucketName;

    @Autowired
    public GcsService(Storage storage, @Value("${gcp.bucket.name}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadFile(MultipartFile file, String objectName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
    }

    @Override
    public void deleteFile(String fileUrl) {
        String prefix = "https://storage.googleapis.com/" + bucketName + "/";
        if (fileUrl != null && fileUrl.startsWith(prefix)) {
            String objectName = fileUrl.substring(prefix.length());
            BlobId blobId = BlobId.of(bucketName, objectName);
            storage.delete(blobId);
        }
    }

}
