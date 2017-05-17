
package com.pace.processor.internal.base;

import java.io.Serializable;
import java.util.List;

public class NetApduData implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    int index; // 指令的索引值
    int lastCheckPoint;// 上次有效节点，用于发生异常时的同步流程
    List<String> cmd; // 命令
    String checker; // 预期值，让客户端做判断？？
    boolean needupload;
    List<String> rsp;// 执行结果
}
