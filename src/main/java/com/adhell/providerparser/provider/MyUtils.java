package com.adhell.providerparser.provider;

import org.apache.commons.validator.routines.DomainValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyUtils {
    public static File convert(String sourceFilePath, String targetFilePath, boolean rewrite) throws IOException {
        File targetFile = new File(targetFilePath);
        if (targetFile.exists() && !rewrite) {
            return targetFile;
        }
        List<String> urlList = MyUtils.loadAdUrlsFromFile(sourceFilePath);
        File outputFile = new File(targetFilePath);
        StringBuilder stringBuilder = new StringBuilder();
        for (String url1 : urlList) {
            stringBuilder
                    .append("\"")
                    .append(url1)
                    .append("\",")
                    .append(System.lineSeparator());
        }
        PrintWriter out = new PrintWriter(outputFile);
        out.println(stringBuilder);
        out.close();
        return outputFile;

    }

    private static List<String> loadAdUrlsFromFile(String filePath) throws IOException {
        List<String> topUrlsSet = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.trim();
            if (DomainValidator.getInstance().isValid(line)) {
                topUrlsSet.add(line);
            }
        }
        return topUrlsSet;
    }
}
