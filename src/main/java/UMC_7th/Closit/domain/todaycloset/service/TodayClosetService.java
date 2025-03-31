package UMC_7th.Closit.domain.todaycloset.service;

import UMC_7th.Closit.domain.todaycloset.dto.TodayClosetRequestDTO;
import UMC_7th.Closit.domain.todaycloset.dto.TodayClosetResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TodayClosetService {
    TodayClosetResponseDTO.CreateResponseDTO createTodayCloset(TodayClosetRequestDTO.CreateRequestDTO request);
    void deleteTodayCloset(Long todayClosetId);
    TodayClosetResponseDTO.CreateResponseDTO createTodayClosetByPostId(Long postId);
}


