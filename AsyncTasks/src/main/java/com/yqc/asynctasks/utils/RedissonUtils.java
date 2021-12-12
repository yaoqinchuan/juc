package com.yqc.asynctasks.utils;

import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.map.event.EntryExpiredListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RedissonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonUtils.class);

    @Autowired
    private RedissonClient redissonClient;

    public RLock getLock(@NotEmpty String lockName) {
        return redissonClient.getLock(lockName);
    }

    public void lock(@NotEmpty String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        lock.lock();
    }

    public void lock(@NotEmpty String lockName, long expireTime, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(lockName);
        lock.lock(expireTime, timeUnit);
    }

    public Boolean tryLock(@NotEmpty String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        if (lock.tryLock()) {
            return Boolean.TRUE;
        }
        LOGGER.info("try lock {} failed.", lockName);
        return false;
    }

    public Boolean tryLock(@NotEmpty String lockName, long expireTime, TimeUnit timeUnit) throws InterruptedException {
        RLock lock = redissonClient.getLock(lockName);
        if (lock.tryLock(expireTime, timeUnit)) {
            return Boolean.TRUE;
        }
        LOGGER.info("try lock {} failed.", lockName);
        return false;
    }

    public void releaseLock(@NotEmpty String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        lock.unlock();
    }

    public Optional<RRateLimiter> getRateLimiter(@NotEmpty String rateName, long count, long time, RateIntervalUnit rateIntervalUnit) {
        RRateLimiter rater = redissonClient.getRateLimiter(rateName);
        // PER_CLIENT为单个客户端限流, OVERALL为所有客户端一起限流
        if (rater.trySetRate(RateType.PER_CLIENT, count, time, rateIntervalUnit)) {
            return Optional.of(rater);
        }
        return Optional.empty();
    }

    public <T> List<T> addAndGetList(@NotEmpty String listName, T element) {
        RList<T> list = redissonClient.getList(listName);
        list.add(element);
        return list;
    }

    public <T> List<T> removeElementFromList(@NotEmpty String listName, T element) {
        RList<T> list = redissonClient.getList(listName);
        list.remove(element);
        return list;
    }

    public <T> void deleteList(@NotEmpty String listName) {
        RList<T> list = redissonClient.getList(listName);
        list.delete();
    }

    public <T> List<T> getList(@NotEmpty String listName) {
        return redissonClient.getList(listName);
    }

    public <T> Queue<T> addToQueue(@NotEmpty String queueName, T element) {
        RQueue<T> queue = redissonClient.getQueue(queueName);
        queue.add(element);
        return queue;
    }

    public <T> T popQueue(@NotEmpty String queueName) {
        RQueue<T> queue = redissonClient.getQueue(queueName);
        return queue.remove();
    }

    public <T> T pollQueue(@NotEmpty String queueName) {
        RQueue<T> queue = redissonClient.getQueue(queueName);
        return queue.poll();
    }

    public <T> RScoredSortedSet<T> initScoreSet(@NotEmpty String scoreSetName, Date expiredDate) {
        RScoredSortedSet<T> scoreSet = redissonClient.getScoredSortedSet(scoreSetName);
        scoreSet.expireAt(expiredDate);
        return scoreSet;
    }

    public <T> RScoredSortedSet<T> addScoreSet(@NotEmpty String scoreSetName, T element,
                                               double score) {
        RScoredSortedSet<T> scoreSet = redissonClient.getScoredSortedSet(scoreSetName);
        scoreSet.addListener((ExpiredObjectListener) name -> LOGGER.info("timeout event {} occurred.", name));
        scoreSet.add(score, element);
        return scoreSet;
    }

    public <T> List<T> getSSRankedList(@NotEmpty String scoreSetName) {
        RScoredSortedSet<T> scoreSet = redissonClient.getScoredSortedSet(scoreSetName);
        if (scoreSet.isEmpty()) return null;
        return scoreSet.stream().collect(Collectors.toList());
    }

    public <T> T getSSFirstElement(@NotEmpty String scoreSetName) {
        RScoredSortedSet<T> scoreSet = redissonClient.getScoredSortedSet(scoreSetName);
        if (scoreSet.isEmpty()) return null;
        return scoreSet.first();
    }

    public <T> T getSSLastElement(@NotEmpty String scoreSetName) {
        RScoredSortedSet<T> scoreSet = redissonClient.getScoredSortedSet(scoreSetName);
        if (scoreSet.isEmpty()) return null;
        return scoreSet.last();
    }

    public <T> T pollSSFirstElement(@NotEmpty String scoreSetName) {
        RScoredSortedSet<T> scoreSet = redissonClient.getScoredSortedSet(scoreSetName);
        if (scoreSet.isEmpty()) return null;
        return scoreSet.pollFirst();
    }

    public <T> T pollSSLastElement(@NotEmpty String scoreSetName) {
        RScoredSortedSet<T> scoreSet = redissonClient.getScoredSortedSet(scoreSetName);
        if (scoreSet.isEmpty()) return null;
        return scoreSet.pollLast();
    }

    public <T> void removeSSLastElement(@NotEmpty String scoreSetName, T element) {
        RScoredSortedSet<T> scoreSet = redissonClient.getScoredSortedSet(scoreSetName);
        if (scoreSet.isEmpty()) return;
        scoreSet.remove(element);
    }

    public <T> void deleteSS(@NotEmpty String scoreSetName) {
        RScoredSortedSet<T> scoreSet = redissonClient.getScoredSortedSet(scoreSetName);
        if (!scoreSet.isExists()) return;
        scoreSet.delete();
    }

    public long deleteSSByPattern(@NotEmpty String pattern) {
        return redissonClient.getKeys().deleteByPattern(pattern);
    }

    public <T, E> void putMap(@NotEmpty String mapName, T key, E value) {
        // 底层不是使用的redis的过期机制，存在服务宕机但是redis数据不会超时的问题，
        // RLocalCachedMap存在刷新不及时的问题，因为使用的本地缓存，减少网络时间，速度会快很多，45倍
        RMapCache<T, E> map = redissonClient.getMapCache(mapName);
        if (!map.isExists()) {
            map.addListener((EntryExpiredListener<T, E>) entryEvent ->
                    LOGGER.info("{} is expired, old value is {}, current value is {}", entryEvent.getKey(),
                            entryEvent.getOldValue(), entryEvent.getValue()));
        }
        map.put(key, value);
    }

    public <T, E> void putIfAbsentMap(@NotEmpty String mapName, T key, E value) {
        RMapCache<T, E> map = redissonClient.getMapCache(mapName);
        if (!map.isExists()) {
            map.addListener((EntryExpiredListener<T, E>) entryEvent ->
                    LOGGER.info("{} is expired, old value is {}, current value is {}", entryEvent.getKey(),
                            entryEvent.getOldValue(), entryEvent.getValue()));
        }
        map.putIfAbsent(key, value);
    }

    public <T, E> void removeMpElement(@NotEmpty String mapName, T key) {
        RMapCache<T, E> map = redissonClient.getMapCache(mapName);
        map.remove(key);
    }

    public <T, E> E getMpElement(@NotEmpty String mapName, T key) {
        RMapCache<T, E> map = redissonClient.getMapCache(mapName);
        return map.get(key);
    }

    public <T, E> void deleteMap(@NotEmpty String mapName) {
        RMapCache<T, E> map = redissonClient.getMapCache(mapName);
        if (!map.isExists()) return;
        map.delete();
    }

    public <T> void publishMessageToTopic(@NotEmpty String topicName, T message) {
        RTopic topic = redissonClient.getTopic(topicName);
        topic.publish(message);
        LOGGER.info("message {} is published to topic {} success.", message, topicName);
    }

    @SuppressWarnings(value = "all")
    public <T> void consumerMessageFromTopic(@NotEmpty String topicName, Class<T> clazz, Function function) {
        RTopic topic = redissonClient.getTopic(topicName);
        topic.addListener(clazz, new MessageListener<T>() {
            @Override
            public void onMessage(CharSequence charSequence, T t) {
                function.apply(t);
                LOGGER.info("message {} is received from topic", t);
            }
        });
    }

    public RTransaction createTransaction() {
        TransactionOptions transactionOpts = TransactionOptions.defaults()
                .syncSlavesTimeout(5, TimeUnit.SECONDS)
                .responseTimeout(3, TimeUnit.SECONDS)
                .retryInterval(2, TimeUnit.SECONDS).retryAttempts(3).timeout(5, TimeUnit.SECONDS);
       return redissonClient.createTransaction(transactionOpts);

    }

    public void commitTransaction(RTransaction transaction) {
        try {
            transaction.commit();
        } catch ( Exception exception) {
            LOGGER.error("error occurs when commit transaction.");
            transaction.rollback();
        }
    }

    public RCountDownLatch getCountDownLatch(@NotEmpty String countDownLatchName, Integer count) {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(countDownLatchName);
        if (!countDownLatch.isExists()){
            if(!countDownLatch.trySetCount(count.longValue())) {
                return null;
            }
        }
        return countDownLatch;
    }
}
