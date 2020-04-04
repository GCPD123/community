package life.majiang.community.service;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.dto.QuestionQueryDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.modle.Question;
import life.majiang.community.modle.QuestionExample;
import life.majiang.community.modle.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    //自己定义的mapper 为了解决view
    //j
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(String search, String tag, Integer page, Integer size) {
        if (StringUtils.isNotBlank(search)) {
//            按照空格分开
            String[] s = StringUtils.split(search, " ");
//            每个元素之间用|分割
             search = Arrays.stream(s).collect(Collectors.joining("|"));
        }



        Integer totalPage;
        //拿到偏移量 偏移量是直接输入到数据库的参数
        Integer offset = size * (page - 1);
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setTag(tag);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
//                对这个方法进行重构 考虑到下面也要用到类似的方法 所以抽取出来一个对象传入
//        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        //指定这个类中数据的类型 t指任何类型都可以但是要指定清楚
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
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
        QuestionExample example = new QuestionExample();
        //倒叙排列所有问题 和回复一样
        example.setOrderByClause("gmt_create desc");
//        这两个是分页需要的数据
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
//        重构成一个方法
        List<Question> questions = questionExtMapper.selectBysearch(questionQueryDTO);
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
        paginationDTO.setData(questionDTOList);

        return paginationDTO;

    }

    //该方法和上面一模一样 因为都是要获得查询分页对象 这次就是加上一个id约束
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        Integer totalPage;

        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
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
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example1, new RowBounds(offset, size));
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
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    //获取相关问题和用户信息
    public QuestionDTO getById(Long id) {
        //获得question相关的东西 然后还要有其他信息
        Question question = questionMapper.selectByPrimaryKey(id);
        //异常判断 自定义异常 将其中的异常信息传到页面
        if (question == null) {
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
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            //指定条件
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            //i=1表示更新成功 否则不成功 为了防止在修改的同时该问题已经被删除 所以还要加入判断
            if (i != 1) {
                //枚举直接通过类名访问
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        } else {
            //表示新增
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);
        }

    }

    public void incView(Long id) {

        //我新创建了一个对象 该对象只有VIew有值，所以只更新这个值，其他的还是保留原来的值
        Question question = new Question();
        //该方法只要两个数据就可以执行 每次增加1
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);

    }


    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (queryDTO == null) {
            return new ArrayList<>();
        }
        //将标签通过,号隔开并替换成|，因为sql中需要 这个就是正则需要匹配的语句
        String regex = StringUtils.replace(queryDTO.getTag(), ",", "|");
        Question question = new Question();
        question.setTag(regex);
        question.setId(queryDTO.getId());
        List<Question> questions = questionExtMapper.selectRelated(question);
        //因为上面返回的是question，而该方法需要返回DTO,所以可以通过lamda转换
        List<QuestionDTO> questionDTOS = questions.stream().map(question1 -> {
            QuestionDTO questionDTO = new QuestionDTO();
            //然后将对象封装进去
            BeanUtils.copyProperties(question1, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());

        return questionDTOS;
    }
}
