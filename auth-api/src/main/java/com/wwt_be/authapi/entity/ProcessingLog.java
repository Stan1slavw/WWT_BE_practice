package com.wwt_be.authapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="processing_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessingLog {
    @Id
    @GeneratedValue private UUID id;
    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User user;
    @Column(name="input_text", nullable=false)
    private String inputText;
    @Column(name="output_text", nullable=false)
    private String outputText;
    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;
}
