package jpa.demo.service;

import jpa.demo.primary.domain.Note;
import jpa.demo.primary.repo.NoteRepository;
import jpa.demo.secondary.domain.NoteSecondary;
import jpa.demo.secondary.repo.NoteSecondaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository primaryRepo;
    private final NoteSecondaryRepository secondaryRepo;

    public NoteService(NoteRepository primaryRepo, NoteSecondaryRepository secondaryRepo) {
        this.primaryRepo = primaryRepo;
        this.secondaryRepo = secondaryRepo;
    }

    @Transactional(transactionManager = "primaryTransactionManager")
    public Note saveToPrimary(String content) {
        Note note = new Note();
        note.setContent(content);
        return primaryRepo.save(note);
    }

    @Transactional(transactionManager = "secondaryTransactionManager")
    public NoteSecondary saveToSecondary(String content) {
        NoteSecondary note = new NoteSecondary();
        note.setContent(content);
        return secondaryRepo.save(note);
    }

    @Transactional(readOnly = true, transactionManager = "primaryTransactionManager")
    public List<Note> findAllPrimary() {
        return primaryRepo.findAll();
    }

    @Transactional(readOnly = true, transactionManager = "secondaryTransactionManager")
    public List<NoteSecondary> findAllSecondary() {
        return secondaryRepo.findAll();
    }
}
