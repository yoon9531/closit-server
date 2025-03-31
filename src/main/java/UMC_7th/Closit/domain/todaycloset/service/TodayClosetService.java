package UMC_7th.Closit.domain.todaycloset.service;

import UMC_7th.Closit.domain.todaycloset.dto.TodayClosetRequestDTO;
import UMC_7th.Closit.domain.todaycloset.dto.TodayClosetResponseDTO;
import org.springframework.data.domain.Pageable;

public interface TodayClosetService {
    TodayClosetResponseDTO.CreateResponseDTO createTodayCloset(TodayClosetRequestDTO.CreateRequestDTO request);
    TodayClosetResponseDTO.CreateResponseDTO createTodayClosetBySelectedPost(TodayClosetRequestDTO.CreateRequestDTO request);
    void deleteTodayCloset(Long todayClosetId);
}


