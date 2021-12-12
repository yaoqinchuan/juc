package com.yqc.asynctasks.dao;

import com.yqc.asynctasks.entity.TaskHistoryDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskHistoryDao extends JpaRepository<TaskHistoryDo, Long> {

}
