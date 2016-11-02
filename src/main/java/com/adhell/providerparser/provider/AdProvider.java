package com.adhell.providerparser.provider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.routines.DomainValidator;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class AdProvider {
    private static final Logger logger = Logger.getLogger(AdProvider.class.getCanonicalName());
    public static List<String> adProviderUrlList;

    public static void init() {
        adProviderUrlList = new ArrayList<>();
        adProviderUrlList.add("https://adaway.org/hosts.txt");
        adProviderUrlList.add("http://pgl.yoyo.org/adservers/serverlist.php?hostformat=hosts&showintro=0&mimetype=plaintext");
//        adProviderUrlList.add("http://hosts-file.net/ad_servers.txt"); // Too big
    }

    public static Set<String> getAdProvidersSet() {
        Set<String> adProvidersSet = new HashSet<String>();
        for (String url : adProviderUrlList) {
            Set<String> tempSet = loadUrlSet(url);
            if (tempSet != null) {
                adProvidersSet.addAll(tempSet);
            }
        }
        logger.info("Final size of url providers is: " + adProvidersSet.size());
        return adProvidersSet;
    }

    private static Set<String> loadUrlSet(String url) {
        logger.info("Loading url: " + url);
        try {
            File tempAd = new File("tempAd.txt");
            FileUtils.copyURLToFile(new URL(url), tempAd, 10000000, 10000000);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(tempAd));
            Set<String> urlSet = new HashSet<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.replace("127.0.0.1", "").trim();
                if (DomainValidator.getInstance().isValid(line)) {
                    urlSet.add(line);
                }
            }
            bufferedReader.close();
            logger.info("Url loading finished. Size of set of valid urls is: " + urlSet.size());
            return urlSet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile() throws IOException {
        Set<String> fullAdProvidersSet = getAdProvidersSet();
        StringBuilder stringBuilder = new StringBuilder();
        for (String url1 : fullAdProvidersSet) {
            stringBuilder
                    .append(url1)
                    .append(System.lineSeparator());
        }
        PrintWriter out = new PrintWriter("adProvidersMain.txt");
        out.println(stringBuilder);
        out.close();
    }
}
