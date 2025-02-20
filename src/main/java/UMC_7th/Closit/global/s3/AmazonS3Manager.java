package UMC_7th.Closit.global.s3;

import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.global.config.AmazonConfig;
import UMC_7th.Closit.global.s3.entity.Uuid;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    public String uploadFile(String keyName, MultipartFile file) {
        long fileSize = file.getSize();
        long maxFileSize = 10 * 1024 * 1024; // 10MB 제한

        log.info("File size: {} bytes", fileSize);
        log.info("File extension: {}", file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
        log.info("Original file name: {}", file.getOriginalFilename());

        if (fileSize > maxFileSize) {
            log.error("❌ Max upload size exceeded: {}", fileSize);
            throw new GeneralException(ErrorStatus.MAX_UPLOAD_SIZE_EXCEEDED);
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) { // ✅ try-with-resources 사용하여 자원 해제 보장
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), keyName, inputStream, metadata));
        } catch (MaxUploadSizeExceededException e) {
            log.error("❌ Max upload size exceeded: {}", e.getMessage(), e);
            throw new GeneralException(ErrorStatus.MAX_UPLOAD_SIZE_EXCEEDED);
        } catch (IOException e) {
            log.error("❌ Failed to upload file to S3: {}", e.getMessage(), e);
            throw new RuntimeException("S3 파일 업로드 실패: " + e.getMessage(), e);
        }

        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }

    public void deleteFile (String profileImage) {
        if (profileImage == null || profileImage.isEmpty()) {
            log.error("profileImage is null or empty");
            return;
        }

        try {
            String fileKey = extractS3Key(profileImage);
            amazonS3.deleteObject(amazonConfig.getBucket(), fileKey);
            log.info("file deleted successfully");
        } catch (Exception e) {
            log.error("error at AmazonS3Manager deleteFile : {}", (Object) e.getStackTrace());
        }


        amazonS3.deleteObject(amazonConfig.getBucket(), profileImage);
    }

    private String extractS3Key(String fileUrl) {
        String bucketUrl = amazonS3.getUrl(amazonConfig.getBucket(), "").toString();

        log.info("bucketUrl: {}", bucketUrl);

        if (fileUrl.startsWith(bucketUrl)) {
            log.info("fileUrl: {}", fileUrl);
            return fileUrl.substring(bucketUrl.length());
        }

        log.error("❌ Invalid S3 file URL: {}", fileUrl);
        throw new GeneralException(ErrorStatus.INVALID_S3_FILE_URL);
    }


    public String generateProfileImageKeyName(String uuid) {
        return amazonConfig.getProfileImagePath() + '/' + uuid;
    }

    public String generatePostFrontImageKeyName(String uuid) {
        return amazonConfig.getPostFrontPath() + '/' + uuid;
    }

    public String generatePostBackImageKeyName(String uuid) {
        return amazonConfig.getPostBackPath() + '/' + uuid;
    }


}
