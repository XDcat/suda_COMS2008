package com.os.zlj;

import sun.awt.windows.WPrinterJob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatricesMul {
    float[][] A, B;

    public MatricesMul(FileToMatrices ma) {
        A = ma.getA();
        B = ma.getB();
    }

    public void run() {
        run(1);
    }
    public float[][] run(int threadNum) {
        float[][] C = new float[A.length][B[0].length];
        long startTime, endTime;
        System.out.printf(
                "线程数：%2d, A：[%4d][%4d], B：[%4d][%4d],",
                threadNum,
                A.length,
                A[0].length,
                B.length,
                B[0].length
        );

        // 开始时间
        startTime = System.currentTimeMillis();

        // 使用线程池
        ExecutorService pool = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < A.length; i++) {
            int finalI = i;
            // 把每一行加入线程池中
            pool.submit(() -> {
                for (int j = 0; j < A[0].length; j++) {
                    for (int k = 0; k < B[0].length; k++) {
                        C[finalI][j] += A[finalI][k] * B[k][j];
                    }
                }
            });
        }


        // 等待线程池完成
        pool.shutdown();
        while(! pool.isTerminated());

//        System.out.println(Arrays.toString(C[0]));
        // 结束时间
        endTime = System.currentTimeMillis();
        System.out.println("计算完成" + ",用时：" + (endTime - startTime) + "ms");

        return C;
    }
    public static void main(String[] args) {
        // 文件相对路径
        String[][] fileNames = new String[][]{
                {"./src/resources/M64A.txt", "./src/resources/M64B.txt"},
                {"./src/resources/M128A.txt", "./src/resources/M128B.txt"},
                {"./src/resources/M512A.txt", "./src/resources/M512B.txt"},
                {"./src/resources/M1024A.txt", "./src/resources/M1024B.txt"}
        };

        System.out.println("在多次运行统一代码逻辑时，因为 java 内部的机制，第一次运行的的时间总会大于之后运行的时间 \n" +
                "在此先全部运行一次，但不记录结果，消除对之后进程的影响");
        // 循环遍历，分别进行答单、四、十六线程
        for (String[] files :
                fileNames) {
            System.out.println(files[0] + ", " + files[1]);
            FileToMatrices fileToMatrices1 = new FileToMatrices(files[0], files[1]);
            MatricesMul matricesMul1 = new MatricesMul(fileToMatrices1);
            matricesMul1.run(1);
            matricesMul1.run(4);
            matricesMul1.run(16);
        }

        System.out.println("-------------正式实验部分-----------------");
        int[] index = new int[]{1, 4, 16};
        for (int i : index) {
            System.out.println("------- 线程数为" + i + " ------");
            for (String[] files : fileNames) {
                FileToMatrices fileToMatrices1 = new FileToMatrices(files[0], files[1]);
                MatricesMul matricesMul1 = new MatricesMul(fileToMatrices1);
                matricesMul1.run(i);
            }
        }

        // 循环遍历，分别进行答单、四、十六线程
        for (String[] files :
                fileNames) {
            System.out.println(files[0] + ", " + files[1]);
            FileToMatrices fileToMatrices1 = new FileToMatrices(files[0], files[1]);
            MatricesMul matricesMul1 = new MatricesMul(fileToMatrices1);
            matricesMul1.run(1);
            matricesMul1.run(4);
            matricesMul1.run(16);
        }
    }
}
