package com.elevator.model;


import com.elevator.Enum.ElevatorDirection;
import com.elevator.Enum.ElevatorState;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@SQLDelete(sql = "UPDATE TBELEVATOR SET status=false WHERE id=?")
@Table(name = "TBELEVATOR")
public class Elevator {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(
            name = "custom-uuid",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    private UUID id;
    private int targetFloor;
    private int currentFloor = 0;
    private ElevatorState elevatorState = ElevatorState.IDLE;
    private ElevatorDirection elevatordirection = ElevatorDirection.UP;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

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
