package com.zlj.os.controller.impl;

import com.zlj.os.controller.IDistributeMethod;
import com.zlj.os.dao.Memory;
import com.zlj.os.dao.MemoryBlock;

import java.util.LinkedList;

public class DistributeFirstFit implements IDistributeMethod {
    /*
    最先适应算法
     */
    @Override
    public MemoryBlock distribute(int require, LinkedList<MemoryBlock> freeBlock) {
        // 遍历空闲内存找到足够大的内存
        MemoryBlock firstFit = null;
        for (MemoryBlock memoryBlock:
                freeBlock) {
            if (memoryBlock.getLimit() > require){
                firstFit = memoryBlock;
                break;
            }
        }

        return firstFit;
    }
}
