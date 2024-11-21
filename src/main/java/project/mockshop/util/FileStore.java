package project.mockshop.util;

import org.springframework.web.multipart.MultipartFile;
import project.mockshop.entity.UploadFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileStore {
    String getFullPath(String filename);

    List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException;

    UploadFile createUploadFile(MultipartFile multipartFile);

    void storeFile(MultipartFile multipartFile, UploadFile uploadFile) throws IOException;

    default String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    default String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
