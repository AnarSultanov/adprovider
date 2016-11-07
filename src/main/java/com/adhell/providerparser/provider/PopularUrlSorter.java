package com.adhell.providerparser.provider;

import org.apache.commons.validator.routines.DomainValidator;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class PopularUrlSorter {

    private static final Logger logger = Logger.getLogger(PopularUrlSorter.class.getCanonicalName());

    private static List<String> getPopularUrlFile(List<String> topSitesList, Set<String> toSortSet) throws FileNotFoundException {
        List<String> sortedUrlList = new ArrayList<>();
        HashSet<String> repeatedUrlsSet = new HashSet<>();
        for (String topSiteUrl : topSitesList) {
            int count = 0;
            for (String adProviderUrl : toSortSet) {
                if (
                        (adProviderUrl.endsWith("." + topSiteUrl) || adProviderUrl.equals(topSiteUrl))
                                && !sortedUrlList.contains(adProviderUrl)
                        ) {
                    sortedUrlList.add(adProviderUrl);
                    count++;
                }
            }
            if (count > 20) {
                repeatedUrlsSet.add(topSiteUrl);
            }
        }
        toSortSet.removeAll(sortedUrlList);
        for (String url : repeatedUrlsSet) {
            sortedUrlList.removeIf(i -> i.endsWith("." + url));
            sortedUrlList.add(url);
        }
        sortedUrlList.addAll(toSortSet);

        return sortedUrlList;
    }

    private static File writeUrlListToFile(List<String> urlsList, String outputFilePath) throws FileNotFoundException {
        File outputFile = new File(outputFilePath);
        StringBuilder stringBuilder = new StringBuilder();
        for (String url1 : urlsList) {
            stringBuilder
                    .append(url1)
                    .append(System.lineSeparator());
        }
        PrintWriter out = new PrintWriter(outputFile);
        out.println(stringBuilder);
        out.close();
        return outputFile;
    }

    public static File writePopularUrlList(List<String> topSitesList, Set<String> toSortSet, String outputFilePath, boolean rewrite) throws FileNotFoundException {
        File file = new File(outputFilePath);
        if (file.exists() && !rewrite) {
            return file;
        }
        List<String> topUlrsList = getPopularUrlFile(topSitesList, toSortSet);
        writeUrlListToFile(topUlrsList, outputFilePath);
        return file;
    }

    public static File writeReachableList(List<String> urlList, String outputFilePath, boolean rewrite) throws FileNotFoundException {
        File file = new File(outputFilePath);
        if (file.exists() && !rewrite) {
            return file;
        }
        List<String> reachableUrlList = getReachableUrlList(urlList);
        writeUrlListToFile(reachableUrlList, outputFilePath);
        return file;
    }

    private static List<String> getReachableUrlList(List<String> urlList) {
        List<String> reachableList = new ArrayList<>();
        for (String url : urlList) {
            if (isServerReachable(url)) {
                reachableList.add(url);
            }
        }
        return reachableList;
    }

    private static boolean isServerReachable(String url) {
        logger.info("Checking: " + url);
        try {
            boolean isReachable = InetAddress.getByName(url).isReachable(10);
            logger.info("reachable: " + url + " " + isReachable);
            return true;
        } catch (IOException e) {
            logger.info("Not reachable: " + url);
            return false;
        }
    }

    public static List<String> loadAdUrlsFromFile(String filePath) throws IOException {
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
