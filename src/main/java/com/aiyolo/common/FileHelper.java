package com.aiyolo.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.core.io.Resource;

public class FileHelper {

    public static String getFileContent(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line.trim());
        }
        br.close();

        return sb.toString();
    }

    public static String getResourceContent(String location) throws IOException {
        Resource resource = SpringUtil.getResource(location);

        InputStream is = resource.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line.trim());
        }
        br.close();

        return sb.toString();
    }

}
