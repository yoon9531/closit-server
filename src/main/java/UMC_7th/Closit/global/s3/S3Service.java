package UMC_7th.Closit.global.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String getPresignedUrl(String dirName, String fileName) { // Presigned Url 발급
        if (!dirName.isEmpty()) {
            fileName = createPath(dirName, fileName);
        }
        GeneratePresignedUrlRequest request = generatePresignedUrlRequest(bucket, fileName);
        URL url = amazonS3.generatePresignedUrl(request);

        return url.toString();
    }

    private GeneratePresignedUrlRequest generatePresignedUrlRequest(String bucketName, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                                                                        .withMethod(HttpMethod.PUT)
                                                                        .withExpiration(getPresignedUrlExpiration());

        generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

        return generatePresignedUrlRequest;
    }

    private Date getPresignedUrlExpiration() { // 유효기간 설정
        Date expiration = new Date();

        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;

        expiration.setTime(expTimeMillis);

        return expiration;
    }

    private String createFileId() {
        return UUID.randomUUID().toString();
    }

    private String createPath(String dirName, String fileName) {
        String fileId = createFileId();
        return String.format("%s/%s", dirName, fileId + fileName);
    }
}
