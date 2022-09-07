package economy.reverse.repurchase.agreement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: xuxianbei
 * Date: 2022/9/7
 * Time: 10:56
 * Version:V1.0
 */
@Component
public class ThreadPoolConfig {

    @Bean
    public AsyncTaskExecutor rpcThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        // 设置最大线程数
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 3);
        // 设置队列容量 默认就是阻塞队列
        executor.setQueueCapacity(5000);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(10);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 处理异常
        executor.setThreadFactory(new ThreadFactoryImpl("公共默认线程"));
        return executor;
    }

    public static class ThreadFactoryImpl implements ThreadFactory {

        private String threadNamePrefix;

        private AtomicInteger threadIndex = new AtomicInteger();

        public ThreadFactoryImpl(final String threadNamePrefix){
            this.threadNamePrefix = threadNamePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(threadNamePrefix + threadIndex.incrementAndGet());
            thread.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
                throw new RuntimeException(e);
            });
            return thread;
        }
    }


}
