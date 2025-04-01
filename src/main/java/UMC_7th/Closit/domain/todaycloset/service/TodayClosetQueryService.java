package UMC_7th.Closit.domain.todaycloset.service;

import UMC_7th.Closit.domain.todaycloset.dto.TodayClosetResponseDTO;
import UMC_7th.Closit.domain.todaycloset.entity.TodayCloset;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface TodayClosetQueryService {
    Slice<TodayCloset> getTodayClosetList(Integer page);
    List<TodayClosetResponseDTO.TodayClosetCandidateDTO> getTodayClosetCandidates();
}


