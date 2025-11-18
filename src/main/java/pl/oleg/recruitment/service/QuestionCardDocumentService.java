package pl.oleg.recruitment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import pl.oleg.recruitment.domain.document.QuestionCardDocument;
import pl.oleg.recruitment.repository.QuestionCardDocumentRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionCardDocumentService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final QuestionCardDocumentRepository questionCardDocumentRepository;

    public void saveQuestionCardDocument(QuestionCardDocument document) {
        questionCardDocumentRepository.save(document);
    }

    public List<QuestionCardDocument> searchQuestionCardDocuments(String keyword, Long stageId, boolean isActive) {
        NativeQuery searchQuery = NativeQuery.builder()
            .withQuery(q -> q
                .bool(b -> b
                    .must(m -> m.match(mt -> mt.field("question").query(keyword)))
                    .filter(f -> f.term(t -> t.field("stageId").value(stageId)))
                    .filter(f -> f.term(t -> t.field("isActive").value(isActive)))
                )
            )
            .build();

        SearchHits<QuestionCardDocument> hits = elasticsearchOperations.search(searchQuery, QuestionCardDocument.class);
        return hits.stream()
            .map(SearchHit::getContent)
            .toList();
    }

    public List<QuestionCardDocument> findByIsActiveTrue(boolean isActive) {
        NativeQuery searchQuery = NativeQuery.builder()
            .withQuery(b -> b
                .term(t -> t.field("isActive").value(isActive)))
            .build();
        SearchHits<QuestionCardDocument> hits = elasticsearchOperations.search((searchQuery),
            QuestionCardDocument.class);
        return hits.stream()
            .map(SearchHit::getContent)
            .toList();
    }

    public List<QuestionCardDocument> findAll() {
        Iterable<QuestionCardDocument> iterable = questionCardDocumentRepository.findAll();
        List<QuestionCardDocument> result = new ArrayList<>();
        iterable.forEach(result::add);
        return result;
    }

    public List<QuestionCardDocument> findByStageId(Long stageId) {
        return questionCardDocumentRepository.findByStageId(stageId);
    }
}
