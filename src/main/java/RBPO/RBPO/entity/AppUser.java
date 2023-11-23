package RBPO.RBPO.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames={"email"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "passwordHash")
    private String passwordHash;
    @Column(name = "email")
    private String email;
    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<Article> articles;
    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<Comment> comments;

}
