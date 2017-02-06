package yxcode.com.cn.yxdecoder.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

import yxcode.com.cn.yxdecoder.base.APP;

/**
 * Created by yupu on 2016/5/24-9:54.
 * Email:459112332@qq.com
 */
public class FileUtil {
    private static String YX_IMAGE = "YXCode";
    private static String YX_SUCCESS_IMAGE = "success_image";
    private static String YX_FAILED_IMAGE = "failed_image";
    private static String YX_CRASH_IMAGE = "crash_image";
    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + YX_IMAGE + File.separator;
    private static String LOGFILE = "log.txt";

    public static String CRASH_PATH = path + YX_CRASH_IMAGE + File.separator;
    public static String SUCCESS_PATH= path + YX_SUCCESS_IMAGE + File.separator;
    public static String FAILED_PATH= path + YX_FAILED_IMAGE + File.separator;

    public static void initSdCard() throws IOException {


        File file = new File(path);
        File file1 = new File(SUCCESS_PATH);
        File file2 = new File(FAILED_PATH);
        File file3 = new File(CRASH_PATH);

        File logFile = new File(LOGFILE);

        if (!file.exists()) {
            file.mkdirs();
        }

        if (!file1.exists()) {
            file1.mkdirs();
        }

        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!file3.exists()) {
            file3.mkdirs();
        }

    }

    public static File getLogFile(String dir,String fileName) throws IOException {
        String path = dir + File.separator + fileName;
        File logFile = new File(path);

        Log.e("FileUtil","log --> "+logFile.getPath());
        if(!logFile.exists()){
            try {
                logFile.createNewFile();
            } catch (Exception e){
                e.printStackTrace();
            }

        }
        return logFile;
    }


    public static void writeStrToFile(String dir,String str){
        writeStrToFile(dir,LOGFILE,str);
    }

    public static void writeStrToFile(String dir,String fileName,String str){
        if(!APP.debug){
            return;
        }
        try {
            FileWriter fileWriter = new FileWriter(getLogFile(dir,fileName), false);

            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void deleteLog(String dir){
        deleteLog(dir,LOGFILE);
    }

    public static void deleteLog(String dir,String fileName){
        if(!APP.debug){
            return;
        }
        try {
            File file = getLogFile(dir,fileName);
            if(file.exists()){
                file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void bitmapToSuccessFile(Bitmap bitmap, String name) {
        bitmapToFile(bitmap,SUCCESS_PATH,name);
    }

    public static void bitmapToCrashFile(Bitmap bitmap, String name) {
        bitmapToFile(bitmap,CRASH_PATH,name);
    }

    public static void bitmapToFailedFile(Bitmap bitmap, String name) {
        bitmapToFile(bitmap,FAILED_PATH,name);
    }

    public static void bitmapToFile(Bitmap bitmap,String dir,String fileName){
        File file = new File(dir + fileName + ".jpg");
        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static File[] listCrashFile(){
        File file = new File(path + YX_CRASH_IMAGE + File.separator);
        return file.listFiles();
    }

    public static File[] listFailedFile(){
        File file = new File(path + YX_FAILED_IMAGE + File.separator);
        return file.listFiles();
    }


    public static void deleteCrashData(String fileName) throws IOException{
        if(!APP.debug){
            return;
        }
        File file = new File(CRASH_PATH + fileName + ".dat");
        if(file.exists()){
            file.delete();
        }
    }

    public static void saveCrashData(String dir,String fileName,byte[] data) throws IOException{
        if(!APP.debug){
            return;
        }
        File file = new File(dir + fileName + ".dat");
        if(!file.exists()){
            file.createNewFile();
        }

        createFile(file.getPath(),data);
    }

    public static void saveCrashData(String fileName,byte[] data) throws IOException{
        if(!APP.debug){
            return;
        }
        File file = new File(CRASH_PATH + fileName + ".dat");
        if(!file.exists()){
            file.createNewFile();
        }

        createFile(file.getPath(),data);
    }

    //将byte数组写入文件
    public static void createFile(String path, byte[] content) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(content);
        fos.close();
    }

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static byte[] readFileByBytes(String fileName) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

        byte[] temp = new byte[1024];
        int size = 0;
        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        in.close();
        return out.toByteArray();
    }

    public static boolean copyFile(File srcFile, File dstFile) {
        if (srcFile.exists() && srcFile.isFile()) {
            if (dstFile.isDirectory()) {
                return false;
            }
            if (dstFile.exists()) {
                dstFile.delete();
            }
            FileInputStream fi = null;
            FileOutputStream fo = null;
            FileChannel in = null;
            FileChannel out = null;

            try {
                fi = new FileInputStream(srcFile);
                fo = new FileOutputStream(dstFile);
                in = fi.getChannel();
                out = fo.getChannel();
                in.transferTo(0, in.size(), out);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fi != null) {
                        fi.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    if (fo != null) {
                        fo.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    final static int MAX_ERROR_FILES = 1;
    /**
     * @return -1 if error or over max file num , return error copy num
     */
    public static int copyDirectory(File sourceDir, File targetDir) {
        int errorFileNum = 0;
        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                return -1;
            }
        }
        File[] file = sourceDir.listFiles();
        if (file == null) {
            return errorFileNum;
        }
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                File sourceFile = file[i];
                File targetFile = new File(targetDir, file[i].getName());
                boolean result = copyFile(sourceFile, targetFile);
                if (!result) {
                    errorFileNum++;
                    if (errorFileNum > MAX_ERROR_FILES) {
                        return -1;
                    }
                }
            }
            if (file[i].isDirectory()) {
                int error = copyDirectory(file[i],
                        new File(targetDir, file[i].getName()));
                if (error < 0) {
                    return error;
                }
                else {
                    errorFileNum += error;
                    if (errorFileNum > MAX_ERROR_FILES) {
                        return -1;
                    }
                }
            }
        }
        return errorFileNum;
    }


    /**
     * @return -1 if error or over max file num , return error copy num
     */
    public static int copyAllFiles(String sourceDir, String targetDir) {
        File sourceDF = new File(sourceDir);
        int errorFileNum = 0;
        if (!sourceDF.exists()) {
            return -1;
        }
        File targetDF = new File(targetDir);
        if (!targetDF.exists()) {
            return -1;
        }
        File[] file = sourceDF.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                boolean result = copyFile(file[i], new File(targetDir, file[i].getName()));
                if (!result) {
                    errorFileNum++;
                    if (errorFileNum > MAX_ERROR_FILES) {
                        return -1;
                    }
                }
            }

            if (file[i].isDirectory()) {
                int error = copyDirectory(file[i],
                        new File(targetDir, file[i].getName()));
                if (error < 0) {
                    return error;
                }
                else {
                    errorFileNum += error;
                    if (errorFileNum > MAX_ERROR_FILES) {
                        return -1;
                    }
                }
            }
        }
        return errorFileNum;
    }

    public static boolean copyFile(String srcPath, String dstPath) {
        return copyFile(new File(srcPath), new File(dstPath));
    }

}
