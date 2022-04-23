package me.TahaCheji.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileUtil {

    public static void delete(File file) {
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            if(files == null) return;
            for(File child : files) {
                delete(child);
            }
        }
        file.delete();
    }

    public static void copyFolder(File source, File target) throws IOException {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFolder(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
