package com.hm.tzgis.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author shike
 * @date 2020/9/3 18:39
 */
@Slf4j
public class FileUtils {


    public static void uploadFile(String filePath, String fileName, String data) {
        try {
            File file = new File(filePath);
            if(!file.exists()){
                file.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(filePath+fileName);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void downloadFile(HttpServletResponse response, String fileName, String path) {
        if (fileName != null) {
            //设置文件路径
            File file = new File(path);
            if (file.exists()) {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                try {
                    response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {

            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }

    public static void ioClose(Closeable... closeables) {
        //遍历传入需要关闭的流
        for (Closeable closeable:closeables){
            try {
                //判断传入的流是否为空
                if (closeable != null) {
                    closeable.close();//不为空则关闭
                }
            } catch (IOException e) {
                //log.error("IO流关闭异常========="+e.getMessage(),e);
            }
        }
    }

    public static void zip(String strPath,String fileName, HttpServletResponse response){
        String sourceFileName = fileName;
        ZipOutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            //将zip以流的形式输出到前台
            response.setHeader("content-type", "application/octet-stream");
            response.setCharacterEncoding("utf-8");
            // 设置浏览器响应头对应的Content-disposition
            //参数中 testZip 为压缩包文件名，尾部的.zip 为文件后缀
            response.setHeader("Content-disposition",
                    "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1")+".zip");
            //创建zip输出流
            out = new ZipOutputStream(response.getOutputStream());
            //创建缓冲输出流
            bos = new BufferedOutputStream(out);
            File sourceFile = new File(sourceFileName);
            //调用压缩函数
            compress(out, bos, sourceFile, sourceFile.getName());
            out.flush();
            log.info("压缩完成");
        } catch (Exception e) {
            log.error("ZIP压缩异常："+e.getMessage(),e);
        } finally {
            ioClose(bos,out);
        }
    }

    public static void compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base){
        FileInputStream fos = null;
        BufferedInputStream bis = null;
        try {
            //如果路径为目录（文件夹）
            if (sourceFile.isDirectory()) {
                //取出文件夹中的文件（或子文件夹）
                File[] flist = sourceFile.listFiles();
                if (flist.length == 0) {//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                    out.putNextEntry(new ZipEntry(base + "/"));
                } else {//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                    for (int i = 0; i < flist.length; i++) {
                        compress(out, bos, flist[i], base + "/" + flist[i].getName());
                    }
                }
            } else {//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
                out.putNextEntry(new ZipEntry(base));
                fos = new FileInputStream(sourceFile);
                bis = new BufferedInputStream(fos);

                int tag;
                //将源文件写入到zip文件中
                while ((tag = bis.read()) != -1) {
                    out.write(tag);
                }

                bis.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ioClose(bis,fos);
        }
    }
}
