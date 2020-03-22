package life.majiang.community.mapper;

import life.majiang.community.modle.Question;
import life.majiang.community.modle.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
   int incView(Question record);
   int incComment(Question record);
}