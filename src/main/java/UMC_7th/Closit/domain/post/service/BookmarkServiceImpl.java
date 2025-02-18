package UMC_7th.Closit.domain.post.service;

import UMC_7th.Closit.domain.post.converter.BookmarkConverter;
import UMC_7th.Closit.domain.post.dto.BookmarkRequestDTO;
import UMC_7th.Closit.domain.post.dto.BookmarkResponseDTO;
import UMC_7th.Closit.domain.post.entity.Bookmark;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.BookmarkRepository;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final SecurityUtil securityUtil;

    @Override
    public BookmarkResponseDTO.CreateBookmarkResultDTO addBookmark(BookmarkRequestDTO.CreateBookmarkDTO request) {
        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Optional <Bookmark> existingBookmark = bookmarkRepository.findByUserAndPost(user, post);

        if (existingBookmark.isPresent()) {
            return BookmarkConverter.toBookmarkStatusDTO(existingBookmark.get());
        }

        Bookmark bookmark = Bookmark.createBookmark(user, post);
        bookmarkRepository.save(bookmark);

        return BookmarkConverter.toBookmarkStatusDTO(bookmark);
    }

    @Override
    public Slice<Bookmark> getUserBookmarks(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Slice<Bookmark> bookmarkList = bookmarkRepository.findByUser(user, pageable);

        return bookmarkList;
    }

    @Override
    public void removeBookmark(Long postId) {
        User user = securityUtil.getCurrentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);
    }
}
