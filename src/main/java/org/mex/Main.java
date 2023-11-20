package org.mex;

import java.util.Scanner;

/**
 * 身份证穷举
 * 已知身份证号码n位和最后一位, 穷举出所有可能的身份证号码
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入身份证号,未知位用?代替,例如: 123456789??2345670");
        System.out.print("输入身份证号:");
        String id = scanner.nextLine();
        if (id.length() != 18) {
            System.out.println("身份证号码长度不正确");
            return;
        }
        if (id.charAt(17) == '?') {
            System.out.println("身份证号码最后一位必须已知");
            return;
        }
        // 获取共有多少个未知位
        int count = 0;
        for (int i = 0; i < 18; i++) {
            if (id.charAt(i) == '?') {
                count++;
            }
        }
        long iter = (int) Math.pow(10, count);// 迭代次数,这个数字是10的count次方
        // 开始迭代
        for (int i = 0; i < iter; i++) {
            StringBuilder tmp = new StringBuilder(String.valueOf(i));
            // 补全未知位
            while (tmp.length() < count) {
                tmp.insert(0, "0");
            }
            // 生成身份证号
            String idIt = id;
            int index = 0;
            for (int j = 0; j < 18; j++) {
                if (idIt.charAt(j) == '?') {
                    idIt = idIt.substring(0, j) + tmp.charAt(index) + idIt.substring(j + 1);
                    index++;
                }
            }
            // 验证身份证号是否正确
            if (check(idIt)) {
                System.out.println(idIt);
            }
        }
    }

    // 验证身份证号是否正确
    public static boolean check(String id) {
        // 1. 验证身份证号码长度
        if (id.length() != 18) {
            return false;
        }
        // 2. 验证身份证号码前17位是否都是数字
        for (int i = 0; i < 17; i++) {
            if (!Character.isDigit(id.charAt(i))) {
                return false;
            }
        }
        // 3. 验证身份证号码最后一位是否是数字或者X
        if (!Character.isDigit(id.charAt(17)) && id.charAt(17) != 'X') {
            return false;
        }
        // 4. 验证身份证号码前两位是否是有效的省份代码
        String[] provinceCodes = new String[]{"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34",
                "35", "36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",
                "64", "65", "71", "81", "82"};
        boolean isProvinceCode = false;
        for (String provinceCode : provinceCodes) {
            if (id.substring(0, 2).equals(provinceCode)) {
                isProvinceCode = true;
                break;
            }
        }
        if (!isProvinceCode) {
            return false;
        }
        // 5. 验证身份证号码出生年月日是否正确
        String year = id.substring(6, 10);
        String month = id.substring(10, 12);
        String day = id.substring(12, 14);
        if (Integer.parseInt(year) < 1900 || Integer.parseInt(year) > 2020) {
            return false;
        }
        if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12) {
            return false;
        }
        if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > 31) {
            return false;
        }
        // 6. 验证身份证号码最后一位是否正确
        String[] parityBit = new String[]{"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        int[] power = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7 ,9 ,10 ,5 ,8 ,4 ,2};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += Integer.parseInt(String.valueOf(id.charAt(i))) * power[i];
        }
        return parityBit[sum % 11].equals(String.valueOf(id.charAt(17)));
    }
}