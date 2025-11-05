package pl.oleg.recruitment.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pl.oleg.recruitment.domain.document.QuestionCardDocument;

import java.util.List;

public interface QuestionCardSearchRepository extends ElasticsearchRepository<QuestionCardDocument, String> {

    List<QuestionCardDocument> findByQuestionContaining(String text);
}
