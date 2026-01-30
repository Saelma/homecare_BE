package jaega.homecare.domain.image.repository;

import jaega.homecare.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByUserId(Long userId);
}