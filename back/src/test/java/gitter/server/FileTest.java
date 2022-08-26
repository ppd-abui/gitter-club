package gitter.server;

import gitter.server.utils.JGitUtils;
import org.eclipse.jgit.util.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;

public class FileTest {

    public static ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
              System.out.println("文     件：" + tempList[i]);
              files.add(tempList[i].toString());
            }
            if (tempList[i].isDirectory()) {
              System.out.println("文件夹：" + tempList[i]);
            }
        }
        return files;
    }

    //将file转化成string
    private static String fileToString(String filePath) throws IOException {
        //对一串字符进行操作
        StringBuffer fileData = new StringBuffer();
        //
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        //缓冲区使用完必须关掉
        reader.close();
        System.out.println(fileData.toString());
        return fileData.toString();
    }


    public static void main(String[] args) {
        String path = JGitUtils.getBaseDir() + "admin/testToken/test.txt";
        try {
            String data = fileToString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return null;
    }
}
