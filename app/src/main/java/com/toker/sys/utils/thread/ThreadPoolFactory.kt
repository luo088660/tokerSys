package biz.kux.emergency.utils.thread

/**
 * 自定义线程池工厂
 * Created by _Smile on 2016/10/26.
 */
object ThreadPoolFactory {

    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()//获得CPU数量
    private val COMMOM_CORE_POOL_SIZE = CPU_COUNT + 1//根据CPU核数来设定核心线程数
    private val COMMON_MAXPOOL_SIZE = CPU_COUNT * 2 + 1//最大线程池数
    private val COMMON_KEEP_LIVE_TIME = 1L//持续时间
    val commonThreadPool =
        ThreadPoolProxy(COMMOM_CORE_POOL_SIZE, COMMON_MAXPOOL_SIZE, COMMON_KEEP_LIVE_TIME)//获取一个普通的线程池实例
}
