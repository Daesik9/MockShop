package project.mockshop.util;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.mockshop.entity.UploadFile;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("prod")
public class ProdFileStore implements FileStore {
    private final S3Template s3Template;
    @Value("${aws_s3_bucketName}")
    private String bucketName;

    public String getFullPath(String filename) {
        return s3Template.createSignedGetURL(bucketName, filename, Duration.ofSeconds(3)).getPath();
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

}
