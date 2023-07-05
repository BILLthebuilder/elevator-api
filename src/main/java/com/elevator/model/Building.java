package com.elevator.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@SQLDelete(sql = "UPDATE TBBUILDING SET status=false WHERE id=?")
@Table(name = "TBBUILDING")
public class Building {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(
            name = "custom-uuid",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    private UUID id;

    private int floors;


    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Elevator> elevators;

    @Column(name = "date_created", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date dateCreated;

    @PrePersist
    void dateCreatedAt() {
        this.dateCreated = new Date();
    }

    @Column(name = "date_updated", columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date dateUpdated;

    @PreUpdate
    void dateUpdatedAt() {
        this.dateUpdated = new Date();
    }

    @Column(name = "status", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean status;

    /**
     * Ensures status value is also updated in the
     * current session during deletion
     */
    @PreRemove
    public void deleteElevator () {
        this.status = false;
    }
}
