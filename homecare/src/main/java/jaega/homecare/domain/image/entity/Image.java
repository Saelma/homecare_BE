package jaega.homecare.domain.image.entity;

import jaega.homecare.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originName; // 원본 파일명

    @Column(nullable = false, length = 1000)
    private String storedUrl; // S3 또는 CloudFront 전체 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Image(String originName, String storedUrl, User user) {
        this.originName = originName;
        this.storedUrl = storedUrl;
        this.user = user;
    }
}
