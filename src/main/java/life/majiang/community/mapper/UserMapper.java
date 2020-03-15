package life.majiang.community.mapper;

import life.majiang.community.modle.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Xue
 * @date 2020/3/15 - 12:09 下午
 */
@Mapper
public interface UserMapper {

    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findToken(@Param("token") String token);//#{} 会到形参里面去找 如果不是类 需要加@param指定名称
}
