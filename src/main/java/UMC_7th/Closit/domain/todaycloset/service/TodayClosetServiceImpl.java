package UMC_7th.Closit.domain.todaycloset.service;

import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.domain.todaycloset.converter.TodayClosetConverter;
import UMC_7th.Closit.domain.todaycloset.dto.TodayClosetRequestDTO;
import UMC_7th.Closit.domain.todaycloset.dto.TodayClosetResponseDTO;
import UMC_7th.Closit.domain.todaycloset.entity.TodayCloset;
import UMC_7th.Closit.domain.todaycloset.repository.TodayClosetRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodayClosetServiceImpl implements TodayClosetService {

    private final TodayClosetRepository todayClosetRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public TodayClosetResponseDTO.CreateResponseDTO createTodayCloset(TodayClosetRequestDTO.CreateRequestDTO request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        TodayCloset todayCloset = TodayClosetConverter.toTodayCloset(post);
        TodayCloset savedTodayCloset = todayClosetRepository.save(todayCloset);

        return TodayClosetConverter.toCreateResponseDTO(savedTodayCloset);
    }

    @Override
    @Transactional
    public TodayClosetResponseDTO.CreateResponseDTO createTodayClosetBySelectedPost(TodayClosetRequestDTO.CreateRequestDTO request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        //중복방지
        boolean exists = todayClosetRepository.existsByPost(post);
        if (exists) {
            throw new GeneralException(ErrorStatus.DUPLICATE_TODAY_CLOSET);
        }

        TodayCloset todayCloset = TodayClosetConverter.toTodayCloset(post);
        TodayCloset saved = todayClosetRepository.save(todayCloset);

        return TodayClosetConverter.toCreateResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteTodayCloset(Long todayClosetId) {
        TodayCloset todayCloset = todayClosetRepository.findById(todayClosetId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TODAY_CLOSET_NOT_FOUND));

        todayClosetRepository.delete(todayCloset);
    }
}
