package com.yqc.redis.manager;

import com.yqc.redis.exception.WrongParametersException;
import com.yqc.redis.pojo.Student;
import com.yqc.redis.utils.CommonUtils;
import com.yqc.redis.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StudentManager {
    Logger logger = LoggerFactory.getLogger(StudentManager.class);

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CommonUtils commonUtils;

    public void saveInRedis(Student student) {
        if (StringUtils.isEmpty(student.getId())) {
            throw new WrongParametersException("student id is empty, please check.");
        }
        redisUtils.hset(student.getId(), "name", student.getName());
        redisUtils.hset(student.getId(), "age", student.getAge());
        redisUtils.hset(student.getId(), "rank", student.getRank());
        redisUtils.hset(student.getId(), "id", student.getId());
    }

    public Student getFromRedis(String studentId) {
        if (StringUtils.isEmpty(studentId)) {
            throw new WrongParametersException("student id is empty, please check.");
        }
        Map<Object, Object> map = redisUtils.hmget(studentId);
        return commonUtils.getInstance(map, Student.class);
    }
}
