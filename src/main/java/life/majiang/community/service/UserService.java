package life.majiang.community.service;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.modle.User;
import life.majiang.community.modle.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xue
 * @date 2020/3/18 - 9:19 下午
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public void createOrUpdate(User user) {
        //accountid是github给的 唯一的
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(example);
//        User dbuser = userMapper.findAccountId(user.getAccountId());
       if(users.size() == 0){
           //插入 数据库 相当于第一次登陆
           user.setGmtCreate(System.currentTimeMillis());
           user.setGmtModified(user.getGmtCreate());
           userMapper.insert(user);
       }else {
           User dbuser = users.get(0);
           //更新它的token方便再次登陆可以查找 更新所有用户信息

           User updateUser = new User();
           updateUser.setGmtModified(System.currentTimeMillis());
           updateUser.setAvatarUrl(user.getAvatarUrl());
           updateUser.setName(user.getName());
           updateUser.setToken(user.getToken());

           UserExample example1 = new UserExample();
           example1.createCriteria().andIdEqualTo(dbuser.getId());
           userMapper.updateByExampleSelective(updateUser, example1);
//           userMapper.update(dbuser);
       }
    }
}
