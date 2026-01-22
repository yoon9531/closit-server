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
import UMC_7th.Closit.security.SecurityUtil;
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
    public void deleteTodayCloset(Long todayClosetId) {
        TodayCloset todayCloset = todayClosetRepository.findById(todayClosetId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TODAY_CLOSET_NOT_FOUND));

        todayClosetRepository.delete(todayCloset);
    }

    @Override
    @Transactional
    public TodayClosetResponseDTO.CreateResponseDTO createTodayClosetByPostId(Long postId) {
        // 1. 게시글 존재 여부 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // 2. 이미 TodayCloset에 등록된 게시글인지 검사
        if (todayClosetRepository.existsByPost(post)) {
            throw new GeneralException(ErrorStatus.DUPLICATE_TODAY_CLOSET);
        }

        // 3. TodayCloset 객체 생성 및 저장
        TodayCloset todayCloset = TodayCloset.builder()
                .post(post)
                .build();

        TodayCloset saved = todayClosetRepository.save(todayCloset);

        // 4. 응답 DTO 반환
        return TodayClosetConverter.toCreateResponseDTO(saved);
    }
}
