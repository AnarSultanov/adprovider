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

public class TopSitesHelper {
    public static final String ALEXA_TOP_SITES = "http://s3.amazonaws.com/alexa-static/top-1m.csv.zip";
    private static final String TAG = TopSitesHelper.class.getCanonicalName();
    private static final Logger logger = Logger.getLogger(TAG);
    private static final String TOP_SITES_ZIP_FILE_NAME = "top_sites.zip";

    public static File unZipFile(File topSitesFile, String outputFilePath) throws IOException {
        ZipFile zipFile = new ZipFile(topSitesFile);
        File entryDestination = null;
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            entryDestination = new File(outputFilePath);
            InputStream in = zipFile.getInputStream(entry);
            OutputStream out = new FileOutputStream(entryDestination);
            IOUtils.copy(in, out);
            IOUtils.closeQuietly(in);
            out.close();
        }
        zipFile.close();
        return entryDestination;
    }

    public static File downloadFile(String url, String filepath, String zipFileName, boolean rewriteIfExist) throws IOException {
        File topSitesFileZipFile = new File(filepath + File.separator + zipFileName);
        if (topSitesFileZipFile.exists() && !rewriteIfExist) {
            return topSitesFileZipFile;
        }
        FileUtils.copyURLToFile(new URL(url), topSitesFileZipFile, 10000000, 10000000);
        return topSitesFileZipFile;
    }

    public static List<String> loadTopListOfUrlsFromFile(File urlListFile) throws IOException {
        List<String> topUrlsSet = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(urlListFile));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.split(",")[1];
            line = line.trim().toLowerCase();
            if (DomainValidator.getInstance().isValid(line)) {
                topUrlsSet.add(line);
            }
        }
        return topUrlsSet;
    }
}
