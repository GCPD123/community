package life.majiang.community.service;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.modle.Question;
import life.majiang.community.modle.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xue
 * @date 2020/3/16 - 6:54 下午
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        Integer totalPage;

        PaginationDTO paginationDTO = new PaginationDTO();
        //分页也需要id约束
        Integer totalCount = questionMapper.count();

        //总页数和总条数的关系

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }


        //容错判断
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        //这里面封装的是分页相关信息 页面等 数据在另外地方组装
        paginationDTO.setPagination(totalPage, page);

        //拿到偏移量 偏移量是直接输入到数据库到参数
        Integer offset = size * (page - 1);
//        数据库到分页查询 查询出来的就是一条条数据
        List<Question> list = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();

        for (Question question : list) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将对象原封不动当拷贝到后者里面
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);

        }
        //将问题对象也组装里面同时有用户信息
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;

    }

    //该方法和上面一模一样 因为都是要获得查询分页对象 这次就是加上一个id约束
    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        Integer totalPage;

        PaginationDTO paginationDTO = new PaginationDTO();
        //分页也需要id约束
        Integer totalCount = questionMapper.countByUserId(userId);

        //总页数和总条数的关系

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }


        //容错判断
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        //将所有相关元素传递 并组装成分页对象的一部分
        paginationDTO.setPagination(totalPage, page);
        //拿到偏移量 偏移量是直接输入到数据库到参数
        Integer offset = size * (page - 1);
//        数据库到分页查询 查询出来的就是一条条数据
        List<Question> list = questionMapper.listByUserId(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();

        for (Question question : list) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将对象原封不动当拷贝到后者里面
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);

        }
        //将问题对象也组装里面同时有用户信息
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }
}
