package gitter.server;

import java.io.File;
import java.util.ArrayList;

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

    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        File file = new File(path);

        if(!file.isDirectory())
            return;

        File[] files = file.listFiles();
        for(int i=0;i<files.length;i++)
            if(files[i].isHidden())
                continue;
            else
                System.out.println(files[i].getName());

//        getFiles(path);
    }
}
