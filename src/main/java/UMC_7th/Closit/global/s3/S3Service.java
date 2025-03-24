package UMC_7th.Closit.global.s3;

import UMC_7th.Closit.global.config.S3Config;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    private final S3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String getPresignedUrl(String path, String fileName) throws UnsupportedEncodingException { // Presigned Url 발급
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        String fileUrl = path + "/" + encodedFileName;

        GeneratePresignedUrlRequest request = generatePresignedUrl(bucket, fileUrl);
        URL url = amazonS3.generatePresignedUrl(request);

        return url.toString();
    }

    private GeneratePresignedUrlRequest generatePresignedUrl(String bucketName, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                                                                        .withMethod(HttpMethod.PUT)
                                                                        .withExpiration(getPresignedUrlExpiration());

        generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

        return generatePresignedUrlRequest;
    }

    private Date getPresignedUrlExpiration() { // 유효기간 설정
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + (1000 * 60 * 2)); // 2분 유효
        return expiration;
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
