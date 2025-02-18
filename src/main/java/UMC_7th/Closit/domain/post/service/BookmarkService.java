package UMC_7th.Closit.domain.post.service;

import UMC_7th.Closit.domain.post.dto.BookmarkRequestDTO;
import UMC_7th.Closit.domain.post.dto.BookmarkResponseDTO;
import UMC_7th.Closit.domain.post.entity.Bookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookmarkService {
    BookmarkResponseDTO.CreateBookmarkResultDTO addBookmark(BookmarkRequestDTO.CreateBookmarkDTO request);
    Slice<Bookmark> getUserBookmarks(Long userId, Pageable pageable);
    void removeBookmark(Long postId);
}
