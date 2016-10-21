import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.routines.DomainValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdProvider {
    public static List<String> adProviderList;

    public static void init() {
        adProviderList = new ArrayList<String>();
        adProviderList.add("https://adaway.org/hosts.txt");
        adProviderList.add("http://pgl.yoyo.org/adservers/serverlist.php?hostformat=hosts&showintro=0&mimetype=plaintext");
        adProviderList.add("http://hosts-file.net/ad_servers.txt");
    }

    public static Set<String> getAdProvidersSet() {
        Set<String> adProvidersSet = new HashSet<String>();
        for (String url : adProviderList) {
            Set<String> tempSet = loadUrlSet(url);
            if (tempSet != null) {
                adProvidersSet.addAll(tempSet);
            }
        }
        System.out.println("Url size: " + adProvidersSet.size());
        return adProvidersSet;
    }

    private static Set<String> loadUrlSet(String url) {
        System.out.print("Loading url: " + url);
        try {
            File tempAd = new File("tempAd.txt");
            FileUtils.copyURLToFile(new URL(url), tempAd, 10000000, 10000000);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(tempAd));
            Set<String> urlSet = new HashSet<String>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.replace("127.0.0.1", "");
                line = line.trim();
                if (DomainValidator.getInstance().isValid(line)) {
                    urlSet.add(line);
                }
            }
            bufferedReader.close();
            return urlSet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
