package com.cn.lx.dao;

import com.cn.lx.entity.EcommerceUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author StevenLu
 * @date 2021/8/5 下午10:37
 */
public interface EcommerceUserDao extends JpaRepository<EcommerceUser,Long> {

    /**
     * <h2>根据用户名查询 EcommerceUser 对象</h2>
     * select * from t_ecommerce_user where username = ?
     * @param userName
     * @return
     */
    EcommerceUser findByUsername(String userName);

    /**
     * <h2>根据用户名和密码查询实体对象</h2>
     * select * from t_ecommerce_user where username = ? and password = ?
     * @param username
     * @param password
     * @return
     */
    EcommerceUser findByUsernameAndPassword(String username, String password);


}
