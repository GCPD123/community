package life.majiang.community.mapper;


import life.majiang.community.modle.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Xue
 * @date 2020/3/16 - 11:38 上午
 */
@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,desc,gmt_create,gmt_modified,creator,tag) values (#{title},#{desc},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);
}
