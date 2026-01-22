package UMC_7th.Closit.domain.user.converter;

import UMC_7th.Closit.domain.highlight.converter.HighlightConverter;
import UMC_7th.Closit.domain.highlight.dto.HighlightResponseDTO;
import UMC_7th.Closit.domain.highlight.entity.Highlight;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.user.dto.UserResponseDTO;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.entity.Block;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    public static UserResponseDTO.UserDTO toUserDTO(User user) {
        return UserResponseDTO.UserDTO.builder()
                .clositId(user.getClositId())
                .name(user.getName())
                .email(user.getEmail())
                .birth(user.getBirth())
                .profileImage(user.getProfileImage())
                .build();
    }

    public static UserResponseDTO.UserInfoDTO toUserInfoDTO(User user) {

        return UserResponseDTO.UserInfoDTO.builder()
                .role(user.getRole())
                .clositId(user.getClositId())
                .name(user.getName())
                .email(user.getEmail())
                .birth(user.getBirth())
                .profileImage(user.getProfileImage())
                .build();
    }

    public static UserResponseDTO.UpdateUserInfoDTO toUpdateUserInfoDTO(User user, long followerCount, long followingCount) {

        return UserResponseDTO.UpdateUserInfoDTO.builder()
                .role(user.getRole())
                .clositId(user.getClositId())
                .name(user.getName())
                .email(user.getEmail())
                .birth(user.getBirth())
                .profileImage(user.getProfileImage())
                .createdAt(user.getCreatedAt())
                .followers(followerCount)
                .following(followingCount)
                .build();
    }

    public static UserResponseDTO.CreatePresignedUrlDTO createPresignedUrlDTO(String imageUrl) {
        return UserResponseDTO.CreatePresignedUrlDTO.builder()
                .imageUrl(imageUrl)
                .build();
    }

    public static UserResponseDTO.UserHighlightSliceDTO toUserHighlightSliceDTO(@NotNull Slice<Highlight> highlightSlice) {
        Slice<HighlightResponseDTO.HighlightDTO> highlightDTOs = highlightSlice.map(HighlightConverter::toHighlightDTO);

        return UserResponseDTO.UserHighlightSliceDTO.builder()
                .highlights(highlightDTOs.getContent())
                .hasNext(highlightSlice.hasNext())
                .pageNumber(highlightSlice.getNumber())
                .size(highlightSlice.getSize())
                .build();
    }

    public static UserResponseDTO.UserFollowerSliceDTO toUserFollowerSliceDTO(Slice<User> followerSlice) {
        return UserResponseDTO.UserFollowerSliceDTO.from(followerSlice);
    }

    public static UserResponseDTO.UserFollowingSliceDTO toUserFollowingSliceDTO(Slice<User> followingSlice) {
        return UserResponseDTO.UserFollowingSliceDTO.from(followingSlice);
    }

    public static UserResponseDTO.UserRecentPostDTO userRecentPostDTO(Post post) { // 특정 사용자의 최근 게시물 조회
        return UserResponseDTO.UserRecentPostDTO.builder()
                .clositId(post.getUser().getClositId())
                .userName(post.getUser().getName())
                .postId(post.getId())
                .thumbnail(post.getFrontImage())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static UserResponseDTO.UserRecentPostListDTO userRecentPostListDTO(Slice<Post> postList) {
        List<UserResponseDTO.UserRecentPostDTO> userRecentPostDTOList = postList.stream()
                .map(UserConverter::userRecentPostDTO).collect(Collectors.toList());

        return UserResponseDTO.UserRecentPostListDTO.builder()
                .userRecentPostDTOList(userRecentPostDTOList)
                .listSize(userRecentPostDTOList.size())
                .isFirst(postList.isFirst())
                .isLast(postList.isLast())
                .hasNext(postList.hasNext())
                .build();
    }

    public static UserResponseDTO.UserBlockResponseDTO toUserBlockResponseDTO (Block savedBlock) {
        return UserResponseDTO.UserBlockResponseDTO.builder()
                .blockerClositId(savedBlock.getBlockerId())
                .blockedClositId(savedBlock.getBlockedId())
                .build();
    }

    public static UserResponseDTO.UserBlockListDTO toUserBlockListDTO(Slice<String> blockedUserSlice) {
        List<UserResponseDTO.UserBlockDTO> blockedUserList =
                blockedUserSlice.getContent().stream()
                        .map(clositId -> UserResponseDTO.UserBlockDTO.builder()
                                .clositId(clositId)
                                .build())
                        .collect(Collectors.toList());

        return UserResponseDTO.UserBlockListDTO.builder()
                .blockedUsers(blockedUserList)
                .hasNext(blockedUserSlice.hasNext())
                .pageNumber(blockedUserSlice.getNumber())
                .size(blockedUserSlice.getSize())
                .build();
    }

    public static UserResponseDTO.UserSearchListDTO toUserSearchListDTO(Slice<User> userSlice) {
        List<UserResponseDTO.UserSearchDTO> userSearchDTOs = userSlice.getContent().stream()
                .map(user -> UserResponseDTO.UserSearchDTO.builder()
                        .clositId(user.getClositId())
                        .name(user.getName())
                        .profileImage(user.getProfileImage())
                        .build())
                .collect(Collectors.toList());

        return UserResponseDTO.UserSearchListDTO.builder()
                .searchedUsers(userSearchDTOs)
                .hasNext(userSlice.hasNext())
                .pageNumber(userSlice.getNumber())
                .size(userSlice.getSize())
                .build();
    }
}
