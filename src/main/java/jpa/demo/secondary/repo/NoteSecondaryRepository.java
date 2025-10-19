package jpa.demo.secondary.repo;

import jpa.demo.secondary.domain.NoteSecondary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteSecondaryRepository extends JpaRepository<NoteSecondary, Long> {
}
