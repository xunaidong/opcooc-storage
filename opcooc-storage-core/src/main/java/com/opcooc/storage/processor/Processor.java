/**
 * Copyright (C), 2015-2019, 华规软件（上海）有限公司
 * FileName: Processor
 * Author:   shenqicheng
 * Date:     2020/9/30 11:31
 * Description:
 * History:
 */
package com.opcooc.storage.processor;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author shenqicheng
 * @Date: 2020/9/30 11:31
 * @Description:
 */
public interface Processor {

    String doDetermineStorage(MethodInvocation invocation, String key);

}
