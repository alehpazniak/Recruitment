package pl.oleg.recruitment.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pl.oleg.recruitment.domain.document.QuestionCardDocument;

import java.util.List;

public interface QuestionCardDocumentRepository extends ElasticsearchRepository<QuestionCardDocument, String> {

    List<QuestionCardDocument> findByStageId(Long stageId);

}
