package RBPO.RBPO.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "article", uniqueConstraints = @UniqueConstraint(columnNames={"article_id"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //возможно изза этой строки у нас проблема с айдишниками
    @Column(name = "article_id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "text", columnDefinition = "text")
    private String text;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private AppUser author;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER)
    private List<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "article")
    private List<Image> images;    //поменять по base64

    @Column(name = "previewImageId")
    private Long previewImageId;
    public void addImageToProductArticle(Image image) {
        image.setArticle(this);
        images.add(image);
    }





    public void setCategory(Category category) {
        this.category = category;
    }
}
