package UMC_7th.Closit.domain.todaycloset.converter;

import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.todaycloset.dto.TodayClosetResponseDTO;
import UMC_7th.Closit.domain.todaycloset.entity.TodayCloset;
import org.springframework.data.domain.Slice;
import java.util.List;
import java.util.stream.Collectors;

public class TodayClosetConverter {

    public static TodayCloset toTodayCloset(Post post) {
        return TodayCloset.builder()
                .post(post)
                .build();
    }

    public static TodayClosetResponseDTO.CreateResponseDTO toCreateResponseDTO(TodayCloset todayCloset) {
        return TodayClosetResponseDTO.CreateResponseDTO.builder()
                .todayClosetId(todayCloset.getId())
                .createdAt(todayCloset.getCreatedAt())
                .build();
    }

    public static TodayClosetResponseDTO.TodayClosetPreviewDTO toPreviewDTO(TodayCloset todayCloset) {
        return TodayClosetResponseDTO.TodayClosetPreviewDTO.builder()
                .todayClosetId(todayCloset.getId())
                .postId(todayCloset.getPost().getId())
                .frontImage(todayCloset.getPost().getFrontImage())
                .backImage(todayCloset.getPost().getBackImage())
                .viewCount(todayCloset.getPost().getView())
                .build();
    }

    public static TodayClosetResponseDTO.TodayClosetListDTO toListDTO(Slice<TodayCloset> todayClosets) {
        List<TodayClosetResponseDTO.TodayClosetPreviewDTO> todayClosetPreviewList = todayClosets.stream()
                .map(TodayClosetConverter::toPreviewDTO)
                .collect(Collectors.toList());

        return TodayClosetResponseDTO.TodayClosetListDTO.builder()
                .todayClosets(todayClosetPreviewList)
                .listSize(todayClosetPreviewList.size())
                .isFirst(todayClosets.isFirst())
                .isLast(todayClosets.isLast())
                .hasNext(todayClosets.hasNext())
                .build();
    }
}
