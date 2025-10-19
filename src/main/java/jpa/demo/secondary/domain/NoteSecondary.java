package jpa.demo.secondary.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "note_secondary")
@Getter
@Setter
public class NoteSecondary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 원본(primary) Note의 ID를 기록하여 연관성을 남깁니다.
    @Column(name = "primary_note_id")
    private Long primaryNoteId;

    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
