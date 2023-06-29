package com.elevator.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;

import java.util.UUID;

@Data
@Entity
@SQLDelete(sql = "UPDATE TBBUILDING SET status=false WHERE id=?")
@Table(name = "TBBUILDING")
public class Building {
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
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

    @ManyToOne
    @JoinColumn(name = "elevator_id")
    private Elevator elevator;
}
