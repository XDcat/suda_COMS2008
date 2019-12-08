package com.zlj.os.controller;

import com.zlj.os.dao.Memory;
import com.zlj.os.dao.MemoryBlock;

import java.util.LinkedList;

public interface IDistributeMethod {
    /**
     * 分配内存
     * @param require 需要的空间
     * @param freeBlock 内存块
     */
    MemoryBlock distribute(int require, LinkedList<MemoryBlock> freeBlock);
}
