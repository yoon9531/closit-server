package UMC_7th.Closit.domain.post.converter;

import UMC_7th.Closit.domain.post.dto.CommentRequestDTO;
import UMC_7th.Closit.domain.post.dto.CommentResponseDTO;
import UMC_7th.Closit.domain.post.entity.Comment;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

    public static Comment toComment(User user, Post post, CommentRequestDTO.CreateCommentRequestDTO request) {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .build();
    }

    public static CommentResponseDTO.CreateCommentResultDTO createCommentResponseDTO(Comment comment) {
        return CommentResponseDTO.CreateCommentResultDTO.builder()
                .commentId(comment.getId())
                .clositId(comment.getUser().getClositId())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentResponseDTO.CommentPreviewDTO commentPreviewDTO(Comment comment) {
        return CommentResponseDTO.CommentPreviewDTO.builder()
                .commentId(comment.getId())
                .clositId(comment.getUser().getClositId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentResponseDTO.CommentPreviewListDTO commentPreviewListDTO(Slice<Comment> commentList) {
        List<CommentResponseDTO.CommentPreviewDTO> commentPreviewDTOList = commentList.stream()
                .map(CommentConverter::commentPreviewDTO).collect(Collectors.toList());

        return CommentResponseDTO.CommentPreviewListDTO.builder()
                .commentPreviewList(commentPreviewDTOList)
                .listSize(commentPreviewDTOList.size())
                .isFirst(commentList.isFirst())
                .isLast(commentList.isLast())
                .hasNext(commentList.hasNext())
                .build();
    }
}

