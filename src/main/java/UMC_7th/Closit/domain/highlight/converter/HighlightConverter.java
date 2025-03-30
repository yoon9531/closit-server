package UMC_7th.Closit.domain.highlight.converter;

import UMC_7th.Closit.domain.highlight.dto.HighlightRequestDTO;
import UMC_7th.Closit.domain.highlight.dto.HighlightResponseDTO;
import UMC_7th.Closit.domain.highlight.entity.Highlight;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.user.entity.User;

import java.time.LocalDateTime;

public class HighlightConverter {

    public static HighlightResponseDTO.CreateHighlightResultDTO toCreateHighlightResultDTO(Highlight highlight) {
        return HighlightResponseDTO.CreateHighlightResultDTO.builder()
                .highlightId(highlight.getId())
                .clositId(highlight.getPost().getUser().getClositId())
                .postId(highlight.getPost().getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static HighlightResponseDTO.HighlightDTO toHighlightDTO(Highlight highlight) {
        return HighlightResponseDTO.HighlightDTO.builder()
                .clositId(highlight.getPost().getUser().getClositId())
                .userName(highlight.getPost().getUser().getName())
                .postId(highlight.getPost().getId())
                .thumbnail(highlight.getPost().getFrontImage())
                .createdAt(highlight.getCreatedAt())
                .updatedAt(highlight.getUpdatedAt())
                .build();
    }

    public static HighlightResponseDTO.HighlightDetailDTO toHighlightDetailDTO(Highlight highlight) {
        return HighlightResponseDTO.HighlightDetailDTO.builder()
                .highlightId(highlight.getId())
                .clositId(highlight.getPost().getUser().getClositId())
                .createdAt(highlight.getCreatedAt())
                .updatedAt(highlight.getUpdatedAt())
                .post(toPostInfoDTO(highlight.getPost()))
                .build();
    }

    private static HighlightResponseDTO.PostInfoDTO toPostInfoDTO(Post post) {
        return HighlightResponseDTO.PostInfoDTO.builder()
                .id(post.getId())
                .backImage(post.getBackImage())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static Highlight toHighlight(HighlightRequestDTO.CreateHighlightDTO request, User user, Post post) {
        return Highlight.builder()
                .post(post)
                .build();
    }
}
