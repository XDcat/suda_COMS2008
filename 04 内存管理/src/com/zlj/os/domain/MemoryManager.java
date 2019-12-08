package com.zlj.os.domain;

import com.zlj.os.controller.IDistributeMethod;
import com.zlj.os.controller.impl.DistributeBestFit;
import com.zlj.os.dao.Memory;
import com.zlj.os.dao.MemoryBlock;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MemoryManager {
    private Memory memory;
    private IDistributeMethod distributeMethod;
    public static void main(String[] args) {
        // 初始化
        Memory memory = new Memory();
        IDistributeMethod distributeMethod = new DistributeBestFit();
        MemoryManager manager = new MemoryManager();
        manager.setMemory(memory);
        manager.setDistributeMethod(distributeMethod);

        System.out.println("------ 内存管理模拟 -------");
        Scanner input = new Scanner(System.in);
        String in1;
        int in2;
        List<MemoryBlock> task = new ArrayList<>();
        System.out.println("为了显示效果内存初始化大小为100，请勿输入过大值");
        System.out.println("1. 使用内存:请输入形如\"+ 内存大小\"，例如\"+ 10\"");
        System.out.println("2. 释放内存:请输入形如\"- 作业序号\"，例如\"- 1\"");
        System.out.println("3. 退出：q");
        while (true) {
            System.out.print("请输入：");
            in1 = input.next();
            if (in1.equals("+") || in1.equals("-")) {
                in2 = input.nextInt();
                if (in1.equals("+")) {
                    // 使用内存
                    MemoryBlock block = manager.distribute(in2);
                    task.add(block);  // 添加到作业列表
                } else {
                    // 判断是否存在
                    if (in2 > task.size()) {
                        System.out.println("未存在此任务");
                        continue;
                    }
                    // 释放内存
                    manager.recycle(task.remove(in2));
                }
            } else if (in1.equals("q")) {
                System.out.println("感谢您的使用！");
                break;
            } else {
                System.out.println("!!! 输入错误，请重新输入");
                System.out.println("为了显示效果内存初始化大小为100，请勿输入过大值");
                System.out.println("1. 使用内存:请输入形如\"+ 内存大小\"，例如\"+ 10\"");
                System.out.println("2. 释放内存:请输入形如\"- 作业序号\"，例如\"- 1\"");
                System.out.println("3. 退出：q");
                continue;
            }
            System.out.println("------ 环境: #为已使用，-为未使用 ------");
            manager.getMemory().display();
            System.out.println("作业：");
            MemoryBlock tmp;
            for (int i = 0; i < task.size(); i++) {
                tmp = task.get(i);
                System.out.printf("\t%d: %2d -> %2d\n", i, tmp.getBase(), tmp.getBase() + tmp.getLimit() - 1);
            }
//            for (MemoryBlock mb :
//                    manager.getMemory().getFreeMemory()) {
//                System.out.println(mb.getBase() + " " + mb.getLimit());
//            }
            System.out.println("");
        }
        input.close();
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public IDistributeMethod getDistributeMethod() {
        return distributeMethod;
    }

    public void setDistributeMethod(IDistributeMethod distributeMethod) {
        this.distributeMethod = distributeMethod;
    }

    public MemoryBlock distribute(int require) {
        LinkedList<MemoryBlock> freeMemory = memory.getFreeMemory();
        MemoryBlock targetBlock = distributeMethod.distribute(require, freeMemory);  // 目标内存

        // 判断是否存在符合条件的内存块
        if (targetBlock == null) {
            throw new RuntimeException("没有足够大小的内存");
        }

        // 这个内存块将会被分为两块或者一块
        MemoryBlock newUsedyBlock = null;
        if (require == targetBlock.getLimit()) {
            // 1. 被分成一块：全部被使用
            freeMemory.remove(targetBlock);  // 在空闲中删除
            newUsedyBlock = targetBlock;
        } else {
            // 2. 分成两块：一块被使用，一块空闲
            newUsedyBlock = new MemoryBlock(targetBlock.getBase(), require);
            // 更新原来的内存块
            targetBlock.setBase(targetBlock.getBase() + require);
            targetBlock.setLimit(targetBlock.getLimit() - require);
        }

        // 添加到已用内存
        memory.getUsedMemory().add(newUsedyBlock);
        return newUsedyBlock;
    }

    public void recycle(MemoryBlock targetBlock) {
        // 1. 在已用中删除
        memory.getUsedMemory().remove(targetBlock);
        // 2. 将释放块放入到空闲内存中，但是存在着四种情况
        /*
        如何判断是那种情况呢？
        用三个值来决定：
            1. 要插入的位置
            2. 是否有前邻接
            3. 是否有后邻接
         */
        LinkedList<MemoryBlock> freeMemory = memory.getFreeMemory();
        int i;
        MemoryBlock before = null, after = null, tmp;
        for (i = 0; i < freeMemory.size(); i++) {
            tmp = freeMemory.get(i);
            // 如果是target的后一块则跳出
            if (targetBlock.getBase() + targetBlock.getLimit() <= tmp.getBase()) {
                if (targetBlock.getBase() + targetBlock.getLimit() == tmp.getBase()) {
                    after = tmp;
                }
                break;
            }

            if (tmp.getBase() + tmp.getLimit() == targetBlock.getBase()) {
                before = tmp;
            }
        }

        if (before == null && after == null) {
            // 2.1 前后无空闲块：直接将释放块插入到顺序的位置
            freeMemory.add(i, targetBlock);
        } else if (before != null && after == null) {
            // 2.2 前有空闲块，后无空闲块：将释放块与前空闲块合并
            before.setLimit(before.getLimit() + targetBlock.getLimit());
        } else if (before == null && after != null) {
            // 2.3 前无空闲块，后有空闲块：将释放块与后空闲块合并
            after.setBase(targetBlock.getBase());
            after.setLimit(after.getLimit() + targetBlock.getLimit());
        } else {
            // 2.4 前后均为空闲块：将连续的三块合并
            before.setLimit(before.getLimit() + targetBlock.getLimit() + after.getLimit());
            freeMemory.remove(after);
        }
    }
}
