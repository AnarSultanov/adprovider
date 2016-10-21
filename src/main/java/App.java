import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

public class App {


    public static void main(String[] args) {
        System.setProperty("http.agent", "Chrome");

        try {
            Set<String> topSitesSet = TopSiteProvider.getSet();
            AdProvider.init();
            Set<String> adProvidersSet = AdProvider.getAdProvidersSet();

            StringBuilder stringBuilderAd = new StringBuilder();
            StringBuilder stringBuilderSite = new StringBuilder();

            for (String adProviderUrl : adProvidersSet) {
                for (String topSiteUrl : topSitesSet) {
                    if (adProviderUrl.endsWith(topSiteUrl)) {
                        stringBuilderAd.append(adProviderUrl);
                        stringBuilderAd.append(System.lineSeparator());
                        stringBuilderSite.append(topSiteUrl);
                        stringBuilderSite.append(System.lineSeparator());
                        break;
                    }
                }
            }

            PrintWriter out = new PrintWriter("adProviders.txt");
            out.println(stringBuilderAd);
            out.close();
            PrintWriter out2 = new PrintWriter("topSitesProviders.txt");
            out2.println(stringBuilderSite);
            out2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
