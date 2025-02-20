package UMC_7th.Closit.domain.post.converter;

import UMC_7th.Closit.domain.post.dto.BookmarkResponseDTO;
import UMC_7th.Closit.domain.post.entity.Bookmark;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class BookmarkConverter {

    public static BookmarkResponseDTO.CreateBookmarkResultDTO toBookmarkStatusDTO(Bookmark bookmark) { // 북마크 생성, 조회
        return BookmarkResponseDTO.CreateBookmarkResultDTO.builder()
                .bookmarkId(bookmark.getId())
                .clositId(bookmark.getUser().getClositId())
                .userName(bookmark.getUser().getName())
                .postId(bookmark.getPost().getId())
                .thumbnail(bookmark.getPost().getFrontImage())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }

    public static BookmarkResponseDTO.BookmarkPreviewDTO bookmarkPreviewDTO(Slice<Bookmark> bookmarks) {
        List<BookmarkResponseDTO.CreateBookmarkResultDTO> bookmarkResultDTOList = bookmarks.stream()
                .map(BookmarkConverter::toBookmarkStatusDTO).collect(Collectors.toList());

        return BookmarkResponseDTO.BookmarkPreviewDTO.builder()
                .bookmarkResultDTOList(bookmarkResultDTOList)
                .listSize(bookmarkResultDTOList.size())
                .isFirst(bookmarks.isFirst())
                .isLast(bookmarks.isLast())
                .hasNext(bookmarks.hasNext())
                .build();
    }
}
