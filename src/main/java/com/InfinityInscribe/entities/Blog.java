package com.InfinityInscribe.entities;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "blogs")
public class Blog extends BaseEntity {
    @Id
    private String id;
    private String title;
    private String content;
    private String category;
    @DBRef
    private User author;
    @DBRef(lazy = true)
    private List<Like> likes = new ArrayList<>();
    @DBRef(lazy = true)
    private List<Comment> comments = new ArrayList();
    @Transient
    public int getLikesCount() {
        return likes.size();
    }
    @Transient
    public int getCommentsCount() {
        return comments.size();
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    public List<Like> getLikes() {
        return likes;
    }
    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
