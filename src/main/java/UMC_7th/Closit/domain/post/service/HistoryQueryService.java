package UMC_7th.Closit.domain.post.service;

import UMC_7th.Closit.domain.post.entity.Post;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

public interface HistoryQueryService {
    Slice<Post> getHistoryThumbnailList(Integer page); // 히스토리 썸네일 조회
    List<Post> getHistoryPreviewList(LocalDate localDate); // 히스토리 게시글 상세 조회
    Slice<Post> getHistoryColorThumbnailList(Integer page); // 히스토리 포인트 색상 썸네일 조회
}
