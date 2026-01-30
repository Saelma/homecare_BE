package jaega.homecare.domain.image.controller;

import jaega.homecare.domain.image.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3ControllerImpl implements S3Controller{

    private final S3Service s3Service;

    @Override
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, Long userId) {
        try {
            String fileUrl = s3Service.uploadFile(file, userId);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<String>> getUserImages(@PathVariable("userId") Long userId) {
        List<String> imageUrls = s3Service.getUserImages(userId);
        return ResponseEntity.ok(imageUrls);
    }
}
