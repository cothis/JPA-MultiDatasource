package jpa.demo.web;

import jpa.demo.primary.domain.Note;
import jpa.demo.secondary.domain.NoteSecondary;
import jpa.demo.service.NoteService;
import jpa.demo.web.dto.CombinedSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/primary")
    public ResponseEntity<Note> createPrimary(@RequestParam String content) {
        return ResponseEntity.ok(noteService.saveToPrimary(content));
    }

    @PostMapping("/secondary")
    public ResponseEntity<NoteSecondary> createSecondary(@RequestParam String content) {
        return ResponseEntity.ok(noteService.saveToSecondary(content));
    }

    @PostMapping("/both")
    public ResponseEntity<CombinedSaveResponse> createBoth(
            @RequestParam("primary") String primaryContent,
            @RequestParam("secondary") String secondaryContent
    ) {
        return ResponseEntity.ok(noteService.savePrimaryThenSecondary(primaryContent, secondaryContent));
    }

    @GetMapping("/primary")
    public ResponseEntity<List<Note>> listPrimary() {
        return ResponseEntity.ok(noteService.findAllPrimary());
    }

    @GetMapping("/secondary")
    public ResponseEntity<List<NoteSecondary>> listSecondary() {
        return ResponseEntity.ok(noteService.findAllSecondary());
    }
}
