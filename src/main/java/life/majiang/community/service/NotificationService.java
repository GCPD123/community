package life.majiang.community.service;

import life.majiang.community.dto.NotificationDTO;
import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.enums.NotificationStatusEnum;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.modle.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Xue
 * @date 2020/3/27 - 10:26 上午
 */
@Service
public class NotificationService {
    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    UserMapper userMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        Integer totalPage;

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        //指定用户的问题条数查询

        NotificationExample example = new NotificationExample();
        //接受的用户
        example.createCriteria().andReceiverEqualTo(userId);
        //该用户下的所有问题
        Integer totalCount = (int) notificationMapper.countByExample(example);
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
        NotificationExample example1 = new NotificationExample();
        example1.createCriteria().andReceiverEqualTo(userId);
        //倒叙
        example1.setOrderByClause("gmt_create desc");
        //同样的分页查询出来的数据返回给页面上
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example1,new RowBounds(offset,size));


        if(notifications.size() == 0){
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOs.add(notificationDTO);
        }


        //封装到内容中去 传给页面
        paginationDTO.setData(notificationDTOs);

        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }

    public NotificationDTO read(Long id, User user) {
        //拿到这个通知
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        //如果这个通知不是给你的
        if(notification.getReceiver() != user.getId()){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        //标记为已读 并更新到数据库
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        //判断传入的类型是否在枚举中 是的话就直接返回 typename是回复问题或评论这句话 type是用来判断 之前存入的时候已经存入了type
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));

        return notificationDTO;

    }
}
