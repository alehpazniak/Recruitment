package pl.oleg.recruitment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.oleg.recruitment.domain.QuestionCard;
import pl.oleg.recruitment.domain.document.QuestionCardDocument;
import pl.oleg.recruitment.repository.QuestionCardRepository;
import pl.oleg.recruitment.repository.QuestionCardSearchRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionCardService {

    private final QuestionCardRepository questionCardRepository; // JPA
    private final QuestionCardSearchRepository questionCardSearchRepository; // Elasticsearch

    @Transactional
    public void syncAllToElasticsearch() {
        List<QuestionCard> all = questionCardRepository.findAll();

        List<QuestionCardDocument> documents = all.stream()
            .map(card -> QuestionCardDocument.builder()
                .id(card.getId().toString())
                .stageId(card.getStage().getId())
                .question(card.getQuestion())
                .expectedAnswer(card.getExpectedAnswer())
                .orderIndex(card.getOrderIndex())
                .isActive(card.getIsActive())
                .evaluationCriteria(card.getEvaluationCriteria())
                .build())
            .toList();

        questionCardSearchRepository.saveAll(documents);
    }
}
