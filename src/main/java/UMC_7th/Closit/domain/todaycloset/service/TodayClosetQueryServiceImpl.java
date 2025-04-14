package UMC_7th.Closit.domain.todaycloset.service;

import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.domain.todaycloset.dto.TodayClosetResponseDTO;
import UMC_7th.Closit.domain.todaycloset.entity.TodayCloset;
import UMC_7th.Closit.domain.todaycloset.repository.TodayClosetRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodayClosetQueryServiceImpl implements TodayClosetQueryService {

    private final TodayClosetRepository todayClosetRepository;
    private final PostRepository postRepository;
    private final SecurityUtil securityUtil;

    @Override
    public Slice<TodayCloset> getTodayClosetList(Integer page, String sort) {
        Pageable pageable;

        if (sort.equals("view")) {
            pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "view"));
        } else { // 기본: 최신순
            pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        return todayClosetRepository.findAll(pageable);
    }

    @Override
    public List<TodayClosetResponseDTO.TodayClosetCandidateDTO> getTodayClosetCandidates() {
        User currentUser = securityUtil.getCurrentUser();

        // 오늘 날짜의 시작과 끝
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);

        // 오늘 하루 동안 작성된 게시글 전체 조회
        List<Post> todaysPosts = postRepository.findAllByUserIdAndCreatedAtBetween(currentUser.getId(), start, end);

        // 이미 TodayCloset에 등록된 Post만 필터링
        List<Post> filtered = todaysPosts.stream()
                .filter(post -> !todayClosetRepository.existsByPost(post))
                .collect(Collectors.toList());

        // DTO 변환
        return filtered.stream()
                .map(post -> TodayClosetResponseDTO.TodayClosetCandidateDTO.builder()
                        .postId(post.getId())
                        .frontImage(post.getFrontImage())
                        .backImage(post.getBackImage())
                        .build())
                .collect(Collectors.toList());
    }
}
