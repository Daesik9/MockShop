package project.mockshop.util;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.mockshop.entity.UploadFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileStore {
    private final S3Template s3Template;
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public URL getFullPath(String filename) {
        return s3Template.createSignedGetURL(bucketName, filename, Duration.ofSeconds(3));
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                UploadFile uploadFile = createUploadFile(multipartFile);
                storeFile(multipartFile, uploadFile);
                storeFileResult.add(uploadFile);
            }
        }
        return storeFileResult;
    }

//    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
//        if (multipartFile.isEmpty()) {
//            return null;
//        }
//
//        String originalFilename = multipartFile.getOriginalFilename(); //image.png
//        String storeFilename = createStoreFileName(originalFilename);
//        multipartFile.transferTo(new File(getFullPath(storeFilename)));
//        return UploadFile.builder()
//                .uploadFileName(originalFilename)
//                .storeFileName(storeFilename)
//                .build();
//    }

    public UploadFile createUploadFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename(); //image.png
        String storeFilename = createStoreFileName(originalFilename);

        return UploadFile.builder()
                .uploadFileName(originalFilename)
                .storeFileName(storeFilename)
                .build();
    }

    public void storeFile(MultipartFile multipartFile, UploadFile uploadFile) throws IOException {
        s3Template.upload(bucketName, uploadFile.getStoreFileName(), multipartFile.getInputStream());
    }


    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
