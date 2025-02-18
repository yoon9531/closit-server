package UMC_7th.Closit.domain.post.controller;

import UMC_7th.Closit.domain.post.converter.BookmarkConverter;
import UMC_7th.Closit.domain.post.dto.BookmarkRequestDTO;
import UMC_7th.Closit.domain.post.dto.BookmarkResponseDTO;
import UMC_7th.Closit.domain.post.entity.Bookmark;
import UMC_7th.Closit.domain.post.service.BookmarkService;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final SecurityUtil securityUtil;

    @Operation(summary = "게시글 북마크 추가")
    @PostMapping
    public ApiResponse<BookmarkResponseDTO.CreateBookmarkResultDTO> addBookmark(
            @RequestBody @Valid BookmarkRequestDTO.CreateBookmarkDTO request){
        return ApiResponse.onSuccess(bookmarkService.addBookmark(request));
    }

    @Operation(summary = "사용자의 북마크 목록 조회")
    @GetMapping
    public ApiResponse<BookmarkResponseDTO.BookmarkPreviewDTO> getUserBookmarks(@RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        Pageable pageable = PageRequest.of(page, size);
        Slice<Bookmark> bookmarks = bookmarkService.getUserBookmarks(userId, pageable);

        return ApiResponse.onSuccess(BookmarkConverter.bookmarkPreviewDTO(bookmarks));
    }

    @Operation(summary = "게시글 북마크 삭제")
    @DeleteMapping("/{post_id}")
    public ApiResponse<String> deleteBookmark(@PathVariable("post_id") Long postId){
        bookmarkService.removeBookmark(postId);
        return ApiResponse.onSuccess("Deletion successful");
    }
}
