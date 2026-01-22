package UMC_7th.Closit.domain.follow.converter;

import UMC_7th.Closit.domain.follow.dto.FollowRequestDTO;
import UMC_7th.Closit.domain.follow.dto.FollowResponseDTO;
import UMC_7th.Closit.domain.follow.entity.Follow;
import UMC_7th.Closit.domain.user.entity.User;

import java.time.LocalDateTime;

public class FollowConverter {

    public static FollowResponseDTO.CreateFollowResultDTO toCreateFollowResultDTO(Follow follow) {
        return FollowResponseDTO.CreateFollowResultDTO.builder()
                .followId(follow.getId())
                .senderId(follow.getSender().getId())
                .receiverId(follow.getReceiver().getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static FollowResponseDTO.FollowDTO toFollowDTO(Follow follow) {
        return FollowResponseDTO.FollowDTO.builder()
                .followId(follow.getId())
                .senderId(follow.getSender().getId())
                .receiverId(follow.getReceiver().getId())
                .createdAt(follow.getCreatedAt())
                .updatedAt(follow.getUpdatedAt())
                .build();
    }

    public static Follow toFollow(FollowRequestDTO.CreateFollowDTO request, User sender, User receiver) {
        return Follow.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
