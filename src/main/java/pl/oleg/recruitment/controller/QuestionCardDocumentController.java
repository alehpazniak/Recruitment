package pl.oleg.recruitment.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.oleg.recruitment.domain.document.QuestionCardDocument;
import pl.oleg.recruitment.service.QuestionCardDocumentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/question-cards")
public class QuestionCardDocumentController {

    private final QuestionCardDocumentService questionCardDocumentService;

    @PostMapping()
    public ResponseEntity<QuestionCardDocument> createQuestionCardDocument(@RequestBody QuestionCardDocument document) {
        questionCardDocumentService.saveQuestionCardDocument(document);
        return ResponseEntity.ok(document);
    }

    @GetMapping
    public List<QuestionCardDocument> getAllCards() {
        return questionCardDocumentService.findAll();
    }

    @GetMapping("/active")
    public List<QuestionCardDocument> getActiveCards(@PathParam("isActive") boolean isActive) {
        return questionCardDocumentService.findByIsActiveTrue(isActive);
    }

    @GetMapping("/stage/{stageId}")
    public List<QuestionCardDocument> getByStageId(@PathVariable Long stageId) {
        return questionCardDocumentService.findByStageId(stageId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<QuestionCardDocument>> searchQuestionCardDocuments(@RequestParam String keyword,
                                                                                  @RequestParam Long stageId,
                                                                                  @RequestParam(defaultValue = "true") boolean isActive) {
        return ResponseEntity.ok(questionCardDocumentService.searchQuestionCardDocuments(keyword, stageId, isActive));
    }

}
