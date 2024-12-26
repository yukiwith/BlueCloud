package com.example.bolgwithcontents.service.impl;

import com.example.bolgwithcontents.service.LikeService;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private RedissonClient redissonClient;

    // 点赞
    @Override
    public void likePost(String userId, String blogId) {
        String key = "post:likecount:" + blogId;
        RScoredSortedSet<String> likeSet = redissonClient.getScoredSortedSet(key);
        likeSet.add(Instant.now().getEpochSecond(),userId);
    }

    // 取消点赞
    @Override
    public void cancelLikePost(String userId, String blogId) {
        String key = "post:likecount:" + blogId;
        RScoredSortedSet<String> likeSet = redissonClient.getScoredSortedSet(key);
        likeSet.remove(userId);
    }

    // 获取点赞数量
    @Override
    public int countLikes(String blogId) {
        String key = "post:likecount:" + blogId;
        RScoredSortedSet<String> likeSet = redissonClient.getScoredSortedSet(key);
        return likeSet.size();
    }
}
