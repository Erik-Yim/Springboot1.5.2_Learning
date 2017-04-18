package cn.itcast.zt.service;

import cn.itcast.zt.dao.UserDao;
import cn.itcast.zt.exception.CacheException;
import cn.itcast.zt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by zhangtian on 2017/4/14.
 */
@Cacheable(cacheNames = "users")
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @CacheEvict(key="'user'")
    public int save(User user) throws Exception {
        return userDao.save(user);
    }

    @CachePut(key = "'user_'+#user.getUuid()")
    public User update(User user) throws CacheException{
        User user1 = userDao.findByUuid(user.getUuid());
        if (null == user1){
            throw new CacheException("Not Find");
        }
        user1.setAge(user.getAge());
        user1.setName(user.getName());
        userDao.update(user1);
        return user1;
    }

    @Cacheable(key="'user_'+#uuid")
    public User findByUuid(String uuid){
        System.err.println("没有走缓存！"+uuid);
        return userDao.findByUuid(uuid);
    }

    @CacheEvict(key = "'user_'+#uuid")//这是清除缓存
    public void delete(String uuid){
        userDao.delete(uuid);
    }
}