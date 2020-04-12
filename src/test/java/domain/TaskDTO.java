package domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import java.util.List;

@JsonAutoDetect
@Data
public class TaskDTO {
    public Long id;
    public Long project_id;
    public Long parent;
    public Long section_id;
    public Long order;
    public String content;
    public Boolean completed;
    public List<Integer> label_ids;
    public Long priority;
    public Long comment_count;
    public String created;
    public String url;
}
