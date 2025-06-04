package UMC_7th.Closit.domain.post.repository;

import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.entity.PostItemTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostItemTagRepository extends JpaRepository<PostItemTag, Long>  {
    List<PostItemTag> findByPostAndTagType(Post post, String tagType);
}
