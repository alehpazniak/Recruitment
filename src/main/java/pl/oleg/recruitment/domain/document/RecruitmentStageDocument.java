package pl.oleg.recruitment.domain.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "recruitment_stages")
public class RecruitmentStageDocument {

    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long recruitmentProcessId;

    @Field(type = FieldType.Text)
    private String stageType;

    @Field(type = FieldType.Integer)
    private Integer stageOrder;

    @Field(type = FieldType.Text)
    private String status;

    @Field(type = FieldType.Date)
    private LocalDateTime scheduledAt;

    @Field(type = FieldType.Date)
    private LocalDateTime startedAt;

    @Field(type = FieldType.Date)
    private LocalDateTime completedAt;

    @Field(type = FieldType.Text)
    private String notes;
}


