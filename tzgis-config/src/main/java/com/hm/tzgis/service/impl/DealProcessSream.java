package com.hm.tzgis.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author shike
 * @date 2020/9/16 16:39
 */
public class DealProcessSream extends Thread{

    private InputStream inputStream;

    public DealProcessSream(InputStream inputStream) {
        this.inputStream = inputStream;
    }


    @Override
    public void run() {
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            br = new BufferedReader(inputStreamReader);
            // 打印信息
            String line;
            System.out.println("输出开始！！！！！");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("输出结束！！！！！");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }finally {
            try {
                    br.close();
                    inputStreamReader.close();
                } catch (IOException e) {
                   e.printStackTrace();
                }
        }
    }


}
