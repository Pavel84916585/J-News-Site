package RBPO.RBPO.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @SequenceGenerator(name = "comment_seq",
            sequenceName = "comment_sequence",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    @Column(name = "comment_id")
    private Long id;
    @Column(name = "commentText", columnDefinition = "text")
    private String commentText;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name="author_id")
    private AppUser author;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name="article_id")
    private Article article;
}
