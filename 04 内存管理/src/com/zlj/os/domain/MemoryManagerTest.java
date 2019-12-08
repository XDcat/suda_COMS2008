package com.zlj.os.domain;

import com.zlj.os.controller.impl.DistributeBestFit;
import com.zlj.os.dao.Memory;
import com.zlj.os.dao.MemoryBlock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemoryManagerTest {
    private Memory memory;
    private MemoryManager manager;

    @Before
    public void setUp() throws Exception {
        memory = new Memory();
        manager = new MemoryManager();
        manager.setMemory(memory);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * 测试 BestFit 分配算法
     */
    @Test
    public void distributeBestFit() {
        System.out.println("BestFit Distribute");
        manager.setDistributeMethod(new DistributeBestFit());
        manager.distribute(10);
        manager.getMemory().display();
    }

    /**
     * 测试 FirstFit 分配算法
     */
    @Test
    public void distributeFirstFit() {
        System.out.println("FirstFit Distribute");
        manager.setDistributeMethod(new DistributeBestFit());
        manager.distribute(10);
        manager.getMemory().display();
    }

}