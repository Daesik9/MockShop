package project.mockshop.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.mockshop.entity.UploadFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile({"local", "test"})
public class LocalFileStore implements FileStore {
    @Value("${file_dir}")
    private String fileDir;

    @Override
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    @Override
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

    @Override
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

    @Override
    public void storeFile(MultipartFile multipartFile, UploadFile uploadFile) throws IOException {
        multipartFile.transferTo(new File(getFullPath(uploadFile.getStoreFileName())));
    }
}
