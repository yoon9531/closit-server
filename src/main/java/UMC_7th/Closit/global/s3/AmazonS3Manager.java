package UMC_7th.Closit.global.s3;

import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.global.config.S3Config;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;
    private final S3Config s3Config;

    public String uploadFile(String keyName, MultipartFile file) {
        long fileSize = file.getSize();
        long maxFileSize = 10 * 1024 * 1024; // 10MB 제한

        if (fileSize > maxFileSize) {
            throw new GeneralException(ErrorStatus.MAX_UPLOAD_SIZE_EXCEEDED);
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) { // try-with-resources 사용하여 자원 해제 보장
            amazonS3.putObject(new PutObjectRequest(s3Config.getBucket(), keyName, inputStream, metadata));
        } catch (MaxUploadSizeExceededException e) {
            throw new GeneralException(ErrorStatus.MAX_UPLOAD_SIZE_EXCEEDED);
        } catch (IOException e) {
            throw new RuntimeException("S3 파일 업로드 실패: " + e.getMessage(), e);
        }

        return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
    }



    public String generateProfileImageKeyName(String uuid) {
        return s3Config.getProfileImagePath() + '/' + uuid;
    }

    public String generatePostFrontImageKeyName(String uuid) {
        return s3Config.getPostFrontPath() + '/' + uuid;
    }

    public String generatePostBackImageKeyName(String uuid) {
        return s3Config.getPostBackPath() + '/' + uuid;
    }
}
