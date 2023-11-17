package RBPO.RBPO.repositories;

import RBPO.RBPO.entity.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> findByText(String Text);
}
