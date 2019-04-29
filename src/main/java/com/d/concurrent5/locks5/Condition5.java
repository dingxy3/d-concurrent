package com.d.concurrent5.locks5;

/**
 * ============================
 *
 * @version [版本号, 2019/3/29]
 * @Auther: dingxy
 * @Description: 条件
 * @since [产品/模块版本]
 * =============================
 */
public interface Condition5 {

    /**
     * current thread go waiting
     */
    void await();

    /**
     * notify the thread which waiting on the condition
     */
    void singal();
}
