package com.yqc.asynctasks.tasks;

import com.yqc.asynctasks.params.SimplePrintTaskParams;
import com.yqc.asynctasks.tasks.exceptions.TaskNeedToBeContinueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// 一个简单地示例
@Component
public class SimplePrintAsyncTask extends AsyncTask<SimplePrintTaskParams> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimplePrintAsyncTask.class);

    private final Integer initTaskStep = 30;

    private final Integer executingTaskStep = 50;

    private final Integer finalTaskStep = 70;

    private final Integer endTaskStep = 80;

    @Override
    public void run(SimplePrintTaskParams asyncRedisParams) {
        Mono.just(asyncRedisParams)
                .flatMap(this::initPrintTask)
                .flatMap(this::executingTask)
                .flatMap(this::finalTask)
                .flatMap(this::endTask)
                .subscribe();
    }

    private Mono<SimplePrintTaskParams> initPrintTask(SimplePrintTaskParams params) {
        if (params.getStep() >= initTaskStep) {
            return Mono.just(params);
        }
        String message = params.getPrintMessage();
        LOGGER.info("message {} is received.", message);
        message = "message with initPrintTask method in SimplePrintAsyncTask has printed for " + params.getStep() + " times.";
        params.setPrintMessage(message);
        params.setStep(params.getStep() + 1);
        throw new TaskNeedToBeContinueException(this, params);
    }

    private Mono<SimplePrintTaskParams> executingTask(SimplePrintTaskParams params) {
        if (params.getStep() < initTaskStep || params.getStep() >= executingTaskStep) {
            return Mono.just(params);
        }
        String message = params.getPrintMessage();
        LOGGER.info("message {} is received.", message);
        message = "message with executingTask method in SimplePrintAsyncTask has printed for " + params.getStep() + " times.";
        params.setPrintMessage(message);
        params.setStep(params.getStep() + 1);
        throw new TaskNeedToBeContinueException(this, params);
    }

    private Mono<SimplePrintTaskParams> finalTask(SimplePrintTaskParams params) {
        if (params.getStep() < executingTaskStep || params.getStep() >= finalTaskStep) {
            return Mono.just(params);
        }
        String message = params.getPrintMessage();
        LOGGER.info("message {} is received.", message);
        message = "message with finalTask method in SimplePrintAsyncTask has printed for " + params.getStep() + " times.";
        params.setPrintMessage(message);
        params.setStep(params.getStep() + 1);
        throw new TaskNeedToBeContinueException(this, params);
    }

    private Mono<SimplePrintTaskParams> endTask(SimplePrintTaskParams params) {
        if (params.getStep() < finalTaskStep || params.getStep() >= endTaskStep) {
            return Mono.just(params);
        }
        String message = params.getPrintMessage();
        LOGGER.info("message {} is received.", message);
        message = "message with endTask method in SimplePrintAsyncTask has printed for " + params.getStep() + " times.";
        params.setPrintMessage(message);
        params.setStep(params.getStep() + 1);
        throw new TaskNeedToBeContinueException(this, params);
    }
}
