package pl.oleg.recruitment.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pl.oleg.recruitment.domain.document.RecruitmentStageDocument;

public interface RecruitmentStageDocumentRepository extends ElasticsearchRepository<RecruitmentStageDocument, String> {

}
