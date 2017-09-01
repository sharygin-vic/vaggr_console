package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.services.PathHelper;
import com.github.vacancy_aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
//        System.out.println(urlOfWantedPage);

        System.out.print(this.getVacanciesSourceName() + "   page:");

        while (true) {
            try {
                Document doc = getDocument(urlOfWantedPage, page);
                if (doc == null) {
                    break;
                }
//                System.out.println(doc);

                System.out.print("  " + page);

                Elements elements = getVacancyElements(doc, page);
                if (elements == null || elements.size() == 0) {
                    break;
                }
                for (Element element : elements) {
                    Vacancy vacancy = getVacancyFromElement(element);
                    if (vacancy != null) {
                        vacancy.setJob(vacancyJobString);
                        if (vacancy.getCompanyName() == null || vacancy.getCompanyName().trim().length() == 0) {
                            vacancy.setCompanyName("Anonymous employer");
                        }
                        res.add(vacancy);
                    }
                }
                page++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
        return res;
    }

    @Override
    public abstract String getVacanciesSourceName();

    protected abstract int getMinimalEnabledPageNum();    // 0 or 1

    protected abstract Elements getVacancyElements(Document doc, int page);

    protected abstract Vacancy getVacancyFromElement(Element element);

    protected abstract String getUrlOfWantedPage(String vacancyJobString, String vacancyLocationName);

    protected int getPageValue(int pageNum) {
        return pageNum;
    }

    protected Document getDocument(String urlOfWantedPage, int page) throws IOException {
        Document doc = null;
        try {
            doc = Jsoup.connect(urlOfWantedPage.replace("{PAGE_VALUE}", "" + getPageValue(page)))
                    .userAgent(USER_AGENT)
                    .referrer(REFERRER)
                    .timeout(TIMEOUT)
                    .get();
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
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PathHelper.getConfigSysAbsolutePathString() + this.getClass().getSimpleName() + "Locations.properties"),
                StandardCharsets.UTF_8)
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
