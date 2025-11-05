package pl.oleg.recruitment.domain.document;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface QuestionCardRepository extends ElasticsearchRepository<QuestionCardDocument, String> {

}
