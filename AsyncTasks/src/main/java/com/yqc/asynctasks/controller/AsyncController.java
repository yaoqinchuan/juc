package com.yqc.asynctasks.controller;

import com.yqc.asynctasks.common.ResetResult;
import com.yqc.asynctasks.manager.AsyncManager;
import com.yqc.asynctasks.utils.RestResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "async/v1")
public class AsyncController {

    @Autowired
    private AsyncManager asyncManager;

    @RequestMapping(path = "/redis/topic", method = RequestMethod.POST)
    public ResetResult<Map<String, String>> createRedisTopicAsync(
            @RequestParam(required = true) String startMessage) {
        Map<String, String> result = new HashMap<>();
        asyncManager.createRedisTopicAsyncTask(startMessage);
        result.put("result", "success");
        return RestResultUtils.success(result);
    }
}
