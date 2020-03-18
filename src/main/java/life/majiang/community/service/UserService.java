package life.majiang.community.service;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.modle.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
       User dbuser = userMapper.findAccountId(user.getAccountId());
       if(dbuser == null){
           //插入 数据库 相当于第一次登陆
           user.setGmtCreate(System.currentTimeMillis());
           user.setGmtModified(user.getGmtCreate());
           userMapper.insert(user);
       }else {
           //更新它的token方便再次登陆可以查找 更新所有用户信息
           dbuser.setGmtModified(System.currentTimeMillis());
           dbuser.setAvatarUrl(user.getAvatarUrl());
           dbuser.setName(user.getName());
           dbuser.setToken(user.getToken());
           userMapper.update(dbuser);
       }
    }
}
