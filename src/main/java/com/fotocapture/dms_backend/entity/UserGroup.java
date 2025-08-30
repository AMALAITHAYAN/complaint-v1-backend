package com.fotocapture.dms_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "user_group",
        uniqueConstraints = {
                @UniqueConstraint(name="uq_user_group", columnNames = {"user_id","group_id"})
        }
)
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // link to user
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name="fk_usergroup_user"))
    private User user;

    // link to group
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false,
            foreignKey = @ForeignKey(name="fk_usergroup_group"))
    private AccessGroup group;

    public UserGroup() { }

    public UserGroup(User user, AccessGroup group) {
        this.user = user;
        this.group = group;
    }

    // --- getters/setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public AccessGroup getGroup() { return group; }
    public void setGroup(AccessGroup group) { this.group = group; }
}
