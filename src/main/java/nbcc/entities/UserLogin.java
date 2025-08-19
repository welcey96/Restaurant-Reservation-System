package nbcc.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class UserLogin {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(name = "FK_LOGIN_USER"))
    private UserDetail userDetail;

    @Column(nullable = false)
    private LocalDateTime lastUsedAt;

    private LocalDateTime loggedOutAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UserLogin() {
    }

    public UserLogin(UserDetail userDetail) {
        this.userDetail = userDetail;
        this.id = UUID.randomUUID().toString();
        this.lastUsedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(LocalDateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public LocalDateTime getLoggedOutAt() {
        return loggedOutAt;
    }

    public void setLoggedOutAt(LocalDateTime loggedOutAt) {
        this.loggedOutAt = loggedOutAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
