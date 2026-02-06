package com.xuanxue.repository;

import com.xuanxue.entity.BaziResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 八字结果数据访问接口
 */
@Repository
public interface BaziResultRepository extends JpaRepository<BaziResult, Long> {
    
    /**
     * 根据用户ID查找八字结果列表
     */
    List<BaziResult> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * 根据用户ID查找最新的八字结果
     */
    Optional<BaziResult> findFirstByUserIdOrderByCreatedAtDesc(Long userId);
}
