package life.majiang.community.mapper;


import life.majiang.community.modle.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Xue
 * @date 2020/3/16 - 11:38 上午
 */
@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,desc,gmt_create,gmt_modified,creator,tag) values (#{title},#{desc},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    //分页查询需要偏移量和每页显示数量 偏移量需要计算 数量自己指定
    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param("offset") Integer offset, @Param("size") Integer size);

    //查询总数
    @Select("select count(1) from question")
    Integer count();

//同样是和查询分页一样 只需要id约束
    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(1) from question where creator = #{userId}" )
    Integer countByUserId(@Param("userId") Integer userId);
}
