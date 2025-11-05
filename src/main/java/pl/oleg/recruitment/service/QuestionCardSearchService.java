package pl.oleg.recruitment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oleg.recruitment.domain.document.QuestionCardDocument;
import pl.oleg.recruitment.repository.QuestionCardSearchRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionCardSearchService {

    private final QuestionCardSearchRepository repository;

    public List<QuestionCardDocument> searchByQuestion(String keyword) {
        return repository.findByQuestionContaining(keyword);
    }

    public QuestionCardDocument save(QuestionCardDocument document) {
        return repository.save(document);
    }
}
