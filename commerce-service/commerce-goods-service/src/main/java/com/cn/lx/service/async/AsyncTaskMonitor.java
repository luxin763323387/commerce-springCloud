package com.cn.lx.service.async;

import com.cn.lx.enums.AsyncTaskStatusEnum;
import com.cn.lx.vo.AsyncTaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author StevenLu
 * @date 2021/8/28 下午4:23
 */
@Slf4j
@Component
@Aspect
public class AsyncTaskMonitor {

    private final AsyncTaskManager asyncTaskManager;

    public AsyncTaskMonitor(AsyncTaskManager asyncTaskManager) {
        this.asyncTaskManager = asyncTaskManager;
    }

    /**
     * <h2>异步任务执行的环绕切面</h2>
     * 环绕切面让我们可以在方法执行之前和执行之后做一些 "额外" 的操作
     */
    @Around("execution(* com.cn.lx.service.async.AsyncServiceImpl.*(..))")
    public Object taskHandler(ProceedingJoinPoint proceedingJoinPoint) {

        // 获取 taskId, 调用异步任务传入的第二个参数
        String taskId = proceedingJoinPoint.getArgs()[1].toString();

        // 获取任务信息, 在提交任务的时候就已经放入到容器中了
        AsyncTaskInfo taskInfo = asyncTaskManager.getTaskInfo(taskId);
        log.info("AsyncTaskMonitor is monitoring async task: [{}]", taskId);

        taskInfo.setStatus(AsyncTaskStatusEnum.RUNNING);
        // 设置为运行状态, 并重新放入容器
        asyncTaskManager.setTaskInfo(taskInfo);

        AsyncTaskStatusEnum status;
        Object result;

        try {
            // 执行异步任务
            result = proceedingJoinPoint.proceed();
            status = AsyncTaskStatusEnum.SUCCESS;
        } catch (Throwable ex) {
            // 异步任务出错
            result = null;
            status = AsyncTaskStatusEnum.FAILED;
            log.error("AsyncTaskMonitor: async task [{}] is failed, Error Info: [{}]",
                    taskId, ex.getMessage(), ex);
        }

        // 设置异步任务其他的信息, 再次重新放入到容器中
        taskInfo.setEndTime(new Date());
        taskInfo.setStatus(status);
        taskInfo.setTotalTime(String.valueOf(
                taskInfo.getEndTime().getTime() - taskInfo.getStartTime().getTime()
        ));
        asyncTaskManager.setTaskInfo(taskInfo);
        return result;
    }
}
