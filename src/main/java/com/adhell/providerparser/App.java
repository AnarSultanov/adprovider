package com.adhell.providerparser;

import com.adhell.providerparser.provider.AdProvider;
import com.adhell.providerparser.provider.TopSiteProvider;
import org.apache.commons.validator.routines.DomainValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getCanonicalName());


    public static void main(String[] args) {
        logger.info("Starting application");
        System.setProperty("http.agent", "Chrome");
        try {
            AdProvider.init();
            AdProvider.writeFile();
            Set<String> adProvidersSet = new HashSet<>();
            try (BufferedReader br = new BufferedReader(new FileReader(new File("adProvidersMain.txt")))) {
                for (String line; (line = br.readLine()) != null; ) {
                    adProvidersSet.add(line.trim());
                }
            }
            List<String> adProviderUrlList = new ArrayList<>();
            List<String> topSitesList = TopSiteProvider.getList();
            HashSet<String> adTop = new HashSet<>();
            logger.info("Starting search for popular ad providers");
            for (String topSiteUrl : topSitesList) {
                int count = 0;
                for (String adProviderUrl : adProvidersSet) {
                    if (
                            (adProviderUrl.endsWith("." + topSiteUrl) || adProviderUrl.equals(topSiteUrl))
                                    && !adProviderUrlList.contains(adProviderUrl)
                            ) {
                        adProviderUrlList.add(adProviderUrl);
                        count++;
                    }
                }
                if (count > 20) {
                    adTop.add(topSiteUrl);
                }
            }
            adProvidersSet.removeAll(adProviderUrlList);
            for (String url : adTop) {
                adProviderUrlList.removeIf(i -> i.endsWith("." + url));
                adProviderUrlList.add(url);
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (String url1 : adProviderUrlList) {
                if (DomainValidator.getInstance().isValid(url1)) {
                    stringBuilder
                            .append("\"")
                            .append(url1)
                            .append("\",")
                            .append(System.lineSeparator());
                }
            }
            for (String url1 : adProvidersSet) {
                if (DomainValidator.getInstance().isValid(url1)) {
                    stringBuilder
                            .append("\"")
                            .append(url1)
                            .append("\",")
                            .append(System.lineSeparator());
                }
            }
            PrintWriter out = new PrintWriter("ad_providers_final_list.txt");
            out.println(stringBuilder);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
