package RBPO.RBPO.repositories;

import RBPO.RBPO.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByText(String Text);
}
