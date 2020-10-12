package biz.kux.emergency.utils.thread

import android.os.Handler

import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 线程池工具类
 * Created by _Smile on 2016/10/26.
 */
object ThreadPoolUtil {

    val customerExecutorService: ExecutorService =
        ThreadPoolExecutor(3, Integer.MAX_VALUE, 0, TimeUnit.MILLISECONDS, SynchronousQueue())

    val handler = Handler()

    /**
     * 在非UI线程中执行
     *
     * @param task
     */
    fun runTaskInThread(task: Runnable) {
        ThreadPoolFactory.commonThreadPool.excute(task)
    }

    /**
     * 在UI线程中执行
     *
     * @param task
     */
    fun runTaskInUIThread(task: Runnable) {
        handler.post(task)
    }

    /**
     * 移除线程池中线程
     *
     * @param task
     */
    fun removeTask(task: Runnable): Boolean {
        return ThreadPoolFactory.commonThreadPool.remove(task)
    }

    /**
     *
     * @param task
     */
    fun runTaskExecutor(task: Runnable) {
        customerExecutorService.execute(task)
    }
}
