package com.os.zlj;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileToMatrices {
    private float[][] A, B;

    public FileToMatrices(String fileA, String fileB) {
        // 读取文件
        A = toMatrices(fileA);
        B = toMatrices(fileB);
    }

    public FileToMatrices() {
        // 默认构造测试数据
        this("./src/resources/M64A.txt", "./src/resources/M64B.txt");
    }

    public static void main(String[] args) {
        File file = new File(".");
        System.out.println(file.getAbsolutePath());
        FileToMatrices fileToMatrices = new FileToMatrices();
        System.out.println(fileToMatrices);
    }

    public float[][] getA() {
        return A;
    }

    public void setA(float[][] a) {
        A = a;
    }

    public float[][] getB() {
        return B;
    }

    public void setB(float[][] b) {
        B = b;
    }


    private float[][] toMatrices(String fileName) {
        float[][] array = new float[0][];
        // 读取文件
        try (
                Reader reader = new FileReader(fileName);
                BufferedReader bufferedReader = new BufferedReader(reader)
        ) {
            ArrayList<String> arrayList = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                arrayList.add(line);
            }

            // 创建数组
            int high = arrayList.size();
            int width = arrayList.get(0).split(" ").length;
            array = new float[high][width];

            String[] splitLine;
            for (int i = 0; i < high; i++) {
                splitLine = arrayList.get(i).split(" ");
                for (int j = 0; j < width; j++) {
                    array[i][j] = Float.parseFloat(splitLine[j]);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return array;
    }

    @Override
    public String toString() {
        return "FileToMatrices{" +
                "\nA=" + Arrays.deepToString(A) +
                ", \nB=" + Arrays.deepToString(B) +
                "\n}";
    }
}