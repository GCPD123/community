package life.majiang.community.service;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.modle.Question;
import life.majiang.community.modle.QuestionExample;
import life.majiang.community.modle.User;
import life.majiang.community.modle.UserExample;
import org.apache.ibatis.session.RowBounds;
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
        //拿到偏移量 偏移量是直接输入到数据库到参数
        Integer offset = size * (page - 1);
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        PaginationDTO paginationDTO = new PaginationDTO();
        //分页也需要id约束 原来生成的不带rowbounds也就是分页查询功能 要加上插件


//        Integer totalCount = questionMapper.count();

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


        //使用mybatis手动添加的分页插件不然没有rowbounds 需要offset和limit
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
//            User user = userMapper.findById(question.getCreator());
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
        //指定用户的问题条数查询

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        //该用户下的所有问题
        Integer totalCount = (int) questionMapper.countByExample(example);
//        Integer totalCount = questionMapper.countByUserId(userId);

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
        QuestionExample example1 = new QuestionExample();
        example1.createCriteria().andCreatorEqualTo(userId);
        //同样的分页查询出来的数据返回给页面上
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example1, new RowBounds(offset,size));
        //这是之前自己定义的方法 后面使用逆向了
//        List<Question> list = questionMapper.listByUserId(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
//            User user = userMapper.findById(question.getCreator());
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

    //获取相关问题和用户信息
    public QuestionDTO getById(Integer id) {
        //获得question相关的东西 然后还要有其他信息
        Question question = questionMapper.selectByPrimaryKey(id);
        //异常判断 自定义异常 将其中的异常信息传到页面
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
//        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        //依然封装
        BeanUtils.copyProperties(question, questionDTO);
        //还要获取user对象 然后封装进去
        User user = userMapper.selectByPrimaryKey(question.getCreator());
//        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        //id不为空表示是修改
        if (question.getId() != null) {

            question.setGmtModified(question.getGmtCreate());
            //该更新方法是检测项是否为空 只有不为空才更新 否则不更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDesc(question.getDesc());
            updateQuestion.setTag(question.getTag());
            //指定条件
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            //i=1表示更新成功 否则不成功 为了防止在修改的同时该问题已经被删除 所以还要加入判断
            if(i != 1){
                //枚举直接通过类名访问
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        } else {
            //表示新增
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }

    }
}
