import com.adhell.providerparser.provider.TopSitesHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class OneMlnDownloaderTest {

    @Test
    public void testClassExist() {
        try {
            Class.forName("com.adhell.providerparser.provider.TopSitesHelper");
        } catch (ClassNotFoundException e) {
            Assert.fail("Class com.adhell.providerparser.provider.TopSitesHelper does not exist");
        }
    }

    @Test
    public void testDownloadTopSitesZipFile() {
        String url = "http://s3.amazonaws.com/alexa-static/top-1m.csv.zip";
        String filepath = "/home/raiym/hdd/AdhellTest";
        String zipFileName = "1-mln.zip";
        try {
            TopSitesHelper.downloadFile(url, filepath, zipFileName, true);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to download 1 mln top sites zip");
        }
        if (!(new File(filepath + File.separator + zipFileName).exists())) {
            Assert.fail(String.format("File doesn\'t %s/%s exist after download.", filepath, zipFileName));
        }
    }

    @Test
    public void testUnZipFile() {
        String filepath = "/home/raiym/hdd/AdhellTest/1-mln.zip";
        String outputPath = "/home/raiym/hdd/AdhellTest/topSitesList.txt";
        File zipFile = new File(filepath);
        try {
            TopSitesHelper.unZipFile(zipFile, outputPath);
        } catch (IOException e) {
            File file = new File(outputPath);
            if (!file.exists()) {
                Assert.fail("Unzip filed");
            }
        }
    }

}
