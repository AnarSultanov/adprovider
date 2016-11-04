package com.adhell.providerparser;

import com.adhell.providerparser.provider.AdProvider;
import com.adhell.providerparser.provider.MyUtils;
import com.adhell.providerparser.provider.PopularUrlSorter;
import com.adhell.providerparser.provider.TopSitesHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getCanonicalName());


    public static void main(String[] args) {

        String linkToTopSites = "http://s3.amazonaws.com/alexa-static/top-1m.csv.zip";
        String pathToAdHellFolder = "/home/raiym/hdd/AdhellTest";
        String zipFileName = "1-mln.zip";
        String topSitesFileName = "top_sites_file.txt";
        String adProvidersFileName = "ad_providers.txt";
        String sortedUrlListFileName = "sorted_url_list.txt";
        String reachableUrlListFileName = "reachable_url_list.txt";
        String listToBlock = "list_to_block.txt";

        boolean rewrite = false;
        logger.info("Starting application");
        System.setProperty("http.agent", "Chrome");
        try {
            logger.info("Getting ad provider url list");
            AdProvider.init();
            AdProvider.writeFile(pathToAdHellFolder, adProvidersFileName, rewrite);
            Set<String> adProvidersSet = AdProvider.loadAdUrlsFromFile(pathToAdHellFolder + File.separator + adProvidersFileName);
            logger.info("End getting ad provider url list");

            logger.info("Starting download top sites");
            File topSitesZipFile = TopSitesHelper.downloadFile(linkToTopSites, pathToAdHellFolder, zipFileName, rewrite);
            File topSitesFile = TopSitesHelper.unZipFile(topSitesZipFile, pathToAdHellFolder + File.separator + topSitesFileName);
            List<String> topSitesList = TopSitesHelper.loadTopListOfUrlsFromFile(topSitesFile);
            logger.info("End downloading top sites");

            logger.info("Starting sort urls");
            File sortedUrlListFile = PopularUrlSorter.writePopularUrlList(topSitesList, adProvidersSet, pathToAdHellFolder + File.separator + sortedUrlListFileName, rewrite);
            List<String> sortedPopularUrlList = PopularUrlSorter.loadAdUrlsFromFile(sortedUrlListFile.getAbsolutePath());
            logger.info("Done sort urls");

            logger.info("Start check reachable");
            PopularUrlSorter.writeReachableList(sortedPopularUrlList, pathToAdHellFolder + File.separator + reachableUrlListFileName, rewrite);
            logger.info("End check reachable");

            logger.info("Starting prepare blocker list");
            MyUtils.convert(pathToAdHellFolder + File.separator + reachableUrlListFileName, pathToAdHellFolder + File.separator + listToBlock, rewrite);
            logger.info("Block list created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
