package jpa.demo.service;

import jpa.demo.primary.domain.Note;
import jpa.demo.primary.repo.NoteRepository;
import jpa.demo.secondary.domain.NoteSecondary;
import jpa.demo.secondary.repo.NoteSecondaryRepository;
import jpa.demo.web.dto.CombinedSaveResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository primaryRepo;
    private final NoteSecondaryRepository secondaryRepo;

    private final PlatformTransactionManager primaryTm;
    private final PlatformTransactionManager secondaryTm;

    // Explicit constructor is required because Lombok does not copy @Qualifier to constructor params.
    @Autowired
    public NoteService(NoteRepository primaryRepo,
                       NoteSecondaryRepository secondaryRepo,
                       @Qualifier("primaryTransactionManager") PlatformTransactionManager primaryTm,
                       @Qualifier("secondaryTransactionManager") PlatformTransactionManager secondaryTm) {
        this.primaryRepo = primaryRepo;
        this.secondaryRepo = secondaryRepo;
        this.primaryTm = primaryTm;
        this.secondaryTm = secondaryTm;
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

    // 하나의 서비스 메서드에서 1번→2번 순서로 각각 트랜잭션을 열어 저장합니다.
    // 주의: JTA/XA가 아니므로 두 저장은 하나의 분산 트랜잭션으로 묶이지 않습니다.
    public CombinedSaveResponse savePrimaryThenSecondary(String primaryContent, String secondaryContent) {
        // 1) primary DB에 저장 (독립 트랜잭션)
        Note primary = new TransactionTemplate(primaryTm).execute(status -> {
            Note n = new Note();
            n.setContent(primaryContent);
            return primaryRepo.save(n);
        });

        // 2) secondary DB에 저장 (독립 트랜잭션), primary ID를 기록
        NoteSecondary secondary = new TransactionTemplate(secondaryTm).execute(status -> {
            NoteSecondary n = new NoteSecondary();
            n.setContent(secondaryContent);
            if (primary != null) {
                n.setPrimaryNoteId(primary.getId());
            }
            return secondaryRepo.save(n);
        });

        Long pId = primary != null ? primary.getId() : null;
        Long sId = secondary != null ? secondary.getId() : null;
        return new CombinedSaveResponse(pId, sId, "saved to primary then secondary");
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
