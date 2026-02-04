package com.xuanxue.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体
 */
@TableName("users")
@Data
public class User {

    @TableId
    private Long id;
    
    private String username;
    
    private String password;
    
    private String phone;
    
    private String email;
    
    private String avatar;
    
    /**
     * VIP过期时间
     */
    private LocalDateTime vipExpireTime;
    
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status = 1;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * 判断是否是VIP用户
     */
    public boolean isVip() {
        return vipExpireTime != null && vipExpireTime.isAfter(LocalDateTime.now());
    }
}
