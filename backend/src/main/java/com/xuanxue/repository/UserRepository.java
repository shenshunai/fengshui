package com.xuanxue.repository;

import com.xuanxue.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问接口
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone);
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 判断手机号是否已存在
     */
    boolean existsByPhone(String phone);
    
    /**
     * 判断用户名是否已存在
     */
    boolean existsByUsername(String username);
}
