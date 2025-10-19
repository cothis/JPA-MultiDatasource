package jpa.demo.primary.repo;

import jpa.demo.primary.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
