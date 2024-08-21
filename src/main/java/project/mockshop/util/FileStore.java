package project.mockshop.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.mockshop.entity.UploadFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
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
        multipartFile.transferTo(new File(getFullPath(uploadFile.getStoreFileName())));
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
