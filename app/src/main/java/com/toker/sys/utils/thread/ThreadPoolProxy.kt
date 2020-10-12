package biz.kux.emergency.utils.thread

import android.annotation.TargetApi
import android.os.Build

import java.util.concurrent.*

/**
 * 线程池代理类
 * Created by _Smile on 2016/10/26.
 */
class ThreadPoolProxy(
    private val corePollSize: Int//核心线程数
    , private val maximumPoolSize: Int//最大线程数
    , private val keepAliveTime: Long//非核心线程持续时间
) {

    private var executor: ThreadPoolExecutor? = null

    /**
     * 单例，产生线程
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    fun getThreadPoolProxyInstant() {
        if (executor == null)
            synchronized(ThreadPoolProxy::class.java) {
                val workQueue = LinkedBlockingDeque<Runnable>(128)//任务队列容量128
                val threadFactory = Executors.defaultThreadFactory()//默认的线程工厂
                val handler = ThreadPoolExecutor.AbortPolicy()//拒绝任务时的处理策略，直接抛异常
                executor = ThreadPoolExecutor(
                    corePollSize,
                    maximumPoolSize,
                    keepAliveTime,
                    TimeUnit.SECONDS,
                    workQueue,
                    threadFactory,
                    handler
                )
            }

    }

    /**
     * 执行任务
     *
     * @param task
     */
    fun excute(task: Runnable) {
        getThreadPoolProxyInstant()
        executor!!.execute(task)
    }

    /**
     * 删除任务
     *
     * @param task
     */
    fun remove(task: Runnable): Boolean {
        getThreadPoolProxyInstant()
        return executor!!.remove(task)
    }
}
