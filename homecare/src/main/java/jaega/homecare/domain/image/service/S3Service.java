package jaega.homecare.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jaega.homecare.domain.image.entity.Image;
import jaega.homecare.domain.image.repository.ImageRepository;
import jaega.homecare.domain.users.entity.User;
import jaega.homecare.domain.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloudfront.domain:}")
    private String cloudFrontDomain;

    @Transactional
    public String uploadFile(MultipartFile file, Long userId) throws IOException {
        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + originalName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // S3에 파일 전송
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead)); // 누구나 읽을 수 있게 설정

        // 1. 저장된 기본 S3 URL 가져오기
        String storedUrl = amazonS3.getUrl(bucket, fileName).toString();

        // 2. 만약 CloudFront 도메인이 설정되어 있다면 URL 교체
        if (!cloudFrontDomain.isEmpty()) {
            storedUrl = cloudFrontDomain + "/" + fileName;
        }

        // 3. 유저 정보 찾기 (인증이 없으므로 파라미터로 받은 ID 활용)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 4. DB에 이미지 정보 저장
        Image image = Image.builder()
                .originName(originalName)
                .storedUrl(storedUrl)
                .user(user)
                .build();
        imageRepository.save(image);

        return storedUrl;
    }

    public List<String> getUserImages(Long userId) {
        return imageRepository.findAllByUserId(userId).stream()
                .map(Image::getStoredUrl) // 저장된 CloudFront URL만 추출
                .collect(Collectors.toList());
    }
}