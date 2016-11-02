package com.adhell.providerparser.provider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.DomainValidator;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TopSiteProvider {
    public static final String ALEXA_TOP_SITES = "http://s3.amazonaws.com/alexa-static/top-1m.csv.zip";
    private static final String TAG = TopSiteProvider.class.getCanonicalName();
    private static final Logger logger = Logger.getLogger(TAG);

    public static List<String> getList() throws IOException {
        logger.info("Enterting getSet() method");
        File topSitesFileArchive = new File("top_sites.zip");
        logger.info("Starting copy .zip file into " + topSitesFileArchive.getAbsolutePath());
        FileUtils.copyURLToFile(new URL(ALEXA_TOP_SITES), topSitesFileArchive, 10000000, 10000000);
        logger.info("File downloaded");
        logger.info("Starting unzip file");
        File topListFile = unZipFile(topSitesFileArchive);
        logger.info("Unzip file completed into: " + topListFile.getAbsolutePath());
        logger.info("Starting generate hash set");
        List<String> topUrlsSet = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(topListFile));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.split(",")[1];
            line = line.trim();
            if (DomainValidator.getInstance().isValid(line)) {
                topUrlsSet.add(line);
            }
        }
        return topUrlsSet;
    }

    private static File unZipFile(File topSitesFile) throws IOException {
        ZipFile zipFile = new ZipFile(topSitesFile);
        File entryDestination = null;
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            entryDestination = new File(entry.getName());
            InputStream in = zipFile.getInputStream(entry);
            OutputStream out = new FileOutputStream(entryDestination);
            IOUtils.copy(in, out);
            IOUtils.closeQuietly(in);
            out.close();
        }
        zipFile.close();
        return entryDestination;
    }
}
