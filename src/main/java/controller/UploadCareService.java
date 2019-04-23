package controller;

import com.uploadcare.api.Client;
import com.uploadcare.upload.FileUploader;
import com.uploadcare.upload.UploadFailureException;
import com.uploadcare.upload.Uploader;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

@Service
public class UploadCareService {

    private Client uploadCareClient;

    public UploadCareService() {
        final String PUBLIC_KEY = "3f9f04a238122a220a22";
        final String PRIVATE_KEY = "9ffb406949a0b7d4ad7f";
        uploadCareClient = new Client(PUBLIC_KEY, PRIVATE_KEY);
    }


    public String saveImageToCloud(String imagePath) throws UploadFailureException {
        File localFile = new File(imagePath);
        Uploader uploader = new FileUploader(uploadCareClient, localFile);
        com.uploadcare.api.File uploadedFile = uploader.upload();
        return uploadedFile.getFileId();
    }

    public String downloadImageFromCloud(String imageId) throws IOException {
        com.uploadcare.api.File requestedByIdFile = uploadCareClient.getFile(imageId);
        File directory = new File(
                Paths.get(System.getProperty("user.home"),
                        "Restaurant-images"
                ).toString());
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = requestedByIdFile.getOriginalFilename();
        String imagePath =
                Paths.get(System.getProperty("user.home"),
                        "Restaurant-images",
                        requestedByIdFile.getFileId() + fileName.substring(fileName.lastIndexOf("."))
                ).toString();
        File image = new File(imagePath);
        if (!image.exists()) {
            URL imageUrl = new URL("https://ucarecdn.com/" + requestedByIdFile.getFileId() + "/" + requestedByIdFile.getOriginalFilename());
            System.out.println(imageUrl.toString());
            FileUtils.copyURLToFile(imageUrl, image);
        }
        return imagePath;
    }
}
