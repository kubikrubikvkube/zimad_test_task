package domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
@JsonAutoDetect
public class CreateNewTaskRequestDTO {
    public String content;
    public Integer project_id;
    public Integer section_id;
    public Integer parent;
    public Integer order;
    public List<Integer> label_ids;
    public Integer priority;
    public String due_string;
    public String due_date;
    public String due_datetime;
    public String due_lang;
}
