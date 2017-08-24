package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.services.PathHelper;
import com.github.vacancy_aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class AbstractStrategy implements Strategy {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
    private static final String REFERRER = "referrer";
    private static final int TIMEOUT = 10 * 1000;

    @Override
    public List<Vacancy> getVacancies(String vacancyJobString, String vacancyLocationName) {
        ArrayList<Vacancy> res = new ArrayList<Vacancy>();
        int page = getMinimalEnabledPageNum();

        String urlOfWantedPage = getUrlOfWantedPage(vacancyJobString, vacancyLocationName);

        while (true) {
            try {
                Document doc = getDocument(urlOfWantedPage, page);
                if (doc == null) {
                    break;
                }
                Elements elements = getVacancyElements(doc);
                if (elements.size() == 0) {
                    break;
                }
                for (Element element : elements) {
//                    System.out.println(element.toString());
//                    System.out.println("---------");

                    Vacancy vacancy = getVacancyFromElement(element);
                    if (vacancy != null) {
                        vacancy.setJob(vacancyJobString);
                        res.add(vacancy);
                    }
                }
                page++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }


    protected abstract int getMinimalEnabledPageNum();    // 0 or 1

    protected abstract Elements getVacancyElements(Document doc);

    protected abstract Vacancy getVacancyFromElement(Element element);

    protected abstract String getUrlOfWantedPage(String vacancyJobString, String vacancyLocationName);

    protected Document getDocument(String urlOfWantedPage, int page) throws IOException {
        Document doc = null;
        try {
            doc = Jsoup.connect(urlOfWantedPage.replace("{PAGE_VALUE}", "" + page))
                    .userAgent(USER_AGENT)
                    .referrer(REFERRER)
                    .timeout(TIMEOUT)
                    .get();
            //System.out.println(doc.html());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return doc;
    }

    protected String getMappedLocationValue(String locationString) {
        if (locationString.equals("")) {
            locationString = "default";
        }
        String result;
        Properties prop = new Properties();
        try (Reader reader = new FileReader(
                Paths.get(PathHelper.getConfigAbsolutePathString() + this.getClass().getSimpleName() + "Locations.properties").toFile())
        ){
            prop.load(reader);
            result = prop.getProperty(locationString);
            if (result == null) {
                result = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = "";
        }
        return result;
    }

}
