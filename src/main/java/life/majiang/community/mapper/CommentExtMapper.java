package life.majiang.community.mapper;

import life.majiang.community.modle.Comment;
import life.majiang.community.modle.CommentExample;
import life.majiang.community.modle.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incComment(Comment comment);
}