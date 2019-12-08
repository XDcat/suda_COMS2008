package com.zlj.os.controller.impl;

import com.zlj.os.controller.IDistributeMethod;
import com.zlj.os.dao.Memory;
import com.zlj.os.dao.MemoryBlock;

import java.util.LinkedList;

public class DistributeBestFit implements IDistributeMethod {
    /*
    最佳适应算法
     */
    @Override
    public MemoryBlock distribute(int require, LinkedList<MemoryBlock> freeBlock) {
        MemoryBlock bestFit = null;  // 目标内存
        int sub = Integer.MAX_VALUE;
        for (MemoryBlock mb:
                freeBlock) {
            // 选择足够大的内存
            if (mb.getLimit() >= require && mb.getLimit() - require < sub) {
                bestFit = mb;
                sub = mb.getLimit() - require;
            }
        }
        return bestFit;
    }
}
