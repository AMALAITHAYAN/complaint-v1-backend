package com.fotocapture.dms_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "group_batch_permission",
        uniqueConstraints = {
                @UniqueConstraint(name="uq_group_batch", columnNames = {"group_id","batch_id"})
        }
)
public class GroupBatchPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // link to group
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false,
            foreignKey = @ForeignKey(name="fk_gbp_group"))
    private AccessGroup group;

    // link to batch
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false,
            foreignKey = @ForeignKey(name="fk_gbp_batch"))
    private Batch batch;

    // permissions
    @Column(nullable=false)
    private boolean canScan;

    @Column(nullable=false)
    private boolean canIndex;

    @Column(nullable=false)
    private boolean canQuality;

    public GroupBatchPermission() { }

    public GroupBatchPermission(AccessGroup group, Batch batch,
                                boolean canScan, boolean canIndex, boolean canQuality) {
        this.group = group;
        this.batch = batch;
        this.canScan = canScan;
        this.canIndex = canIndex;
        this.canQuality = canQuality;
    }

    // --- getters/setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AccessGroup getGroup() { return group; }
    public void setGroup(AccessGroup group) { this.group = group; }

    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }

    public boolean isCanScan() { return canScan; }
    public void setCanScan(boolean canScan) { this.canScan = canScan; }

    public boolean isCanIndex() { return canIndex; }
    public void setCanIndex(boolean canIndex) { this.canIndex = canIndex; }

    public boolean isCanQuality() { return canQuality; }
    public void setCanQuality(boolean canQuality) { this.canQuality = canQuality; }
}
