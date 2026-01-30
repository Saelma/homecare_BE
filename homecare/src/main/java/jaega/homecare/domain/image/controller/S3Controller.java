package jaega.homecare.domain.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "AWS S3", description = "AWS S3 이미지 업로드 API")
@RequestMapping("/api/s3")
public interface S3Controller {

    @Operation(summary = "AWS S3 이미지 업로드 API", description = "업로드 한 파일을 AWS S3에 이미지를 업로드 합니다.")
    @ApiResponse(responseCode = "200", description = "AWS 이미지 업로드 성공")
    @PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, Long userId);

    @Operation(summary = "유저 이미지 목록 조회", description = "특정 유저가 업로드한 이미지 URL 목록을 가져옵니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<String>> getUserImages(@PathVariable Long userId);
}
