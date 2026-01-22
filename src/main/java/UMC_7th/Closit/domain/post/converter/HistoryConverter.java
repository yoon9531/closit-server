package UMC_7th.Closit.domain.post.converter;

import UMC_7th.Closit.domain.post.dto.HistoryResponseDTO;
import UMC_7th.Closit.domain.post.entity.Post;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryConverter {

    public static HistoryResponseDTO.DateHistoryThumbnailDTO dateHistoryThumbnailDTO (Post post) { // 히스토리 썸네일 조회
        return  HistoryResponseDTO.DateHistoryThumbnailDTO.builder()
                .postId(post.getId())
                .thumbnail(post.getFrontImage())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static HistoryResponseDTO.DataHistoryThumbnailListDTO dataHistoryThumbnailListDTO (Slice<Post> postList) {
        List<HistoryResponseDTO.DateHistoryThumbnailDTO> dateHistoryThumbnailDTOList = postList.stream()
                .map(HistoryConverter::dateHistoryThumbnailDTO).collect(Collectors.toList());

        return HistoryResponseDTO.DataHistoryThumbnailListDTO.builder()
                .dateHistoryThumbnailDTOList(dateHistoryThumbnailDTOList)
                .listSize(dateHistoryThumbnailDTOList.size())
                .isFirst(postList.isFirst())
                .isLast(postList.isLast())
                .hasNext(postList.hasNext())
                .build();
    }

    public static HistoryResponseDTO.DateHistoryPreviewDTO dateHistoryPreviewDTO(Post post) { // 히스토리 게시글 상세 조회
        return HistoryResponseDTO.DateHistoryPreviewDTO.builder()
                .postId(post.getId())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static HistoryResponseDTO.DateHistoryPreviewListDTO dateHistoryPreviewListDTO(LocalDate localDate, List<Post> postList) {
        List<HistoryResponseDTO.DateHistoryPreviewDTO> historyPreviewDTOList = postList.stream()
                .map(HistoryConverter::dateHistoryPreviewDTO).collect(Collectors.toList());

        return HistoryResponseDTO.DateHistoryPreviewListDTO.builder()
                .postList(historyPreviewDTOList)
                .date(localDate)
                .build();
    }

    public static HistoryResponseDTO.ColorHistoryThumbnailDTO colorHistoryThumbnailDTO(Post post) { // 히스토리 포인트 색상 썸네일 조회
        return HistoryResponseDTO.ColorHistoryThumbnailDTO.builder()
                .postId(post.getId())
                .thumbnail(post.getPointColor())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static HistoryResponseDTO.ColorHistoryThumbnailListDTO colorHistoryThumbnailListDTO(Slice<Post> postList) {
        List<HistoryResponseDTO.ColorHistoryThumbnailDTO> colorHistoryThumbnailDTOList = postList.stream()
                .map(HistoryConverter::colorHistoryThumbnailDTO).collect(Collectors.toList());

        return HistoryResponseDTO.ColorHistoryThumbnailListDTO.builder()
                .colorHistoryThumbnailDTOList(colorHistoryThumbnailDTOList)
                .listSize(colorHistoryThumbnailDTOList.size())
                .isFirst(postList.isFirst())
                .isLast(postList.isLast())
                .hasNext(postList.hasNext())
                .build();
    }
}
