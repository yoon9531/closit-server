package UMC_7th.Closit.global.infra.s3;

import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String getPresignedUrl(String path, String fileName) { // Presigned Url 발급
        String fileExtension = getFileExtension(fileName);
        String uuid = UUID.randomUUID() + fileExtension;
        String fileUrl = path + "/" + uuid;

        GeneratePresignedUrlRequest request = generatePresignedUrl(bucket, fileUrl);
        URL url = amazonS3.generatePresignedUrl(request);

        return url.toString();
    }

    private GeneratePresignedUrlRequest generatePresignedUrl(String bucketName, String fileName) { // Presigned Url 생성
        return new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPresignedUrlExpiration());
    }

    private Date getPresignedUrlExpiration() { // 유효기간 설정
        return new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
    }

    private String getFileExtension(String fileName) { // 파일 확장자 추출
        int fileExtension = fileName.lastIndexOf(".");
        return (fileExtension == -1) ? "" : fileName.substring(fileExtension);
    }

    public void deleteFile(String profileImage) { // 파일 삭제
        if (profileImage == null || profileImage.isEmpty()) {
            throw new GeneralException(ErrorStatus.PROFILE_IMAGE_EMPTY);
        }

        try {
            String fileKey = extractS3Key(profileImage);
            amazonS3.deleteObject(bucket, fileKey);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.IMAGE_NOT_DELETE);
        }
    }

    private String extractS3Key(String fileUrl) {
        String bucketUrl = amazonS3.getUrl(bucket, "").toString();

        if (fileUrl.startsWith(bucketUrl)) {
            return fileUrl.substring(bucketUrl.length());
        }
        throw new GeneralException(ErrorStatus.INVALID_S3_FILE_URL);
    }
}
