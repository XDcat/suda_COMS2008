package com.zlj.os.dao;


import java.util.Arrays;
import java.util.LinkedList;

public class Memory {
    private int LENGTH = 100;  // 内存总大小
    private LinkedList<MemoryBlock> freeMemory;  // 空闲内存
    private LinkedList<MemoryBlock> usedMemory;  // 已用内存

    public Memory() {
        // 空闲内存为满
        freeMemory = new LinkedList<>();
        freeMemory.add(new MemoryBlock(0, LENGTH));

        // 已用内存为空
        usedMemory = new LinkedList<>();
    }

    public Memory(LinkedList<MemoryBlock> freeMemory, LinkedList<MemoryBlock> usedMemory) {
        this.freeMemory = freeMemory;
        this.usedMemory = usedMemory;
    }

    public LinkedList<MemoryBlock> getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(LinkedList<MemoryBlock> freeMemory) {
        this.freeMemory = freeMemory;
    }

    public LinkedList<MemoryBlock> getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(LinkedList<MemoryBlock> usedMemory) {
        this.usedMemory = usedMemory;
    }

    @Override
    public String toString() {
        // 初始化字符为 -
        char[] chars = new char[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            chars[i] = '-';
        }

        // 将已使用的内存改为 #
        int base;
        for (MemoryBlock mb:
             usedMemory) {
            base = mb.getBase();
            for (int i = 0; i < mb.getLimit(); i++) {
                chars[base + i] = '#';
            }
        }
        String res = "内存: ";
        res = res + new String(chars);
        return res;
    }
    public void display() {
        System.out.println(toString());
    }

    public static void main(String[] args) {
        Memory memory = new Memory();
        memory.display();
        char[] chars = new char[]{'1', '2', 3};
        System.out.println(new String(chars));
    }
}

