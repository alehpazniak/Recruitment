package pl.oleg.recruitment.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "question_cards")
public class QuestionCardDocument {

    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long stageId;

    @Field(type = FieldType.Text)
    private String question;

    @Field(type = FieldType.Text)
    private String expectedAnswer;

    @Field(type = FieldType.Integer)
    private Integer orderIndex;

    @Field(type = FieldType.Boolean)
    private Boolean isActive;

    @Field(type = FieldType.Text)
    private String evaluationCriteria;
}
