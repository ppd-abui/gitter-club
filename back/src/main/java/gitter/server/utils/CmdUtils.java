package gitter.server.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CmdUtils {


    /**
     * 命令行操作git
     */
    public static String run(String op){
        Runtime run = Runtime.getRuntime();

        try {
            String[] cmd =new String[]{"powershell.exe",op};
            Process p = run.exec(cmd);

            InputStream is = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            p.waitFor();
            if (p.exitValue() != 0) {
                //说明命令执行失败
                //可以进入到错误处理步骤中
                return null;
            }

            String result = "";
            String line = null;
            while ((line = reader.readLine()) != null) {
                result += line + '\n';
                p.waitFor();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
