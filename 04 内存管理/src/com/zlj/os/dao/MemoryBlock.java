package com.zlj.os.dao;

/**
 * 内存块
 */
public class MemoryBlock {
    private int base;
    private int limit;

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public MemoryBlock(int base, int limit) {
        this.base = base;
        this.limit = limit;
    }
}
