package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.vo.Vacancy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;

// https://jobs.dou.ua/vacancies/?category=Java&search=Одесса
public class JobsDouUaStrategy  extends AbstractStrategy implements Strategy {
    private static final String SITE_NAME = "https://jobs.dou.ua";
    private static final String URL_FORMAT = SITE_NAME + "/vacancies/?category={JOB_STRING}&search={LOCATION_STRING}";  //&p={PAGE_VALUE}";

    @Override
    protected int getMinimalEnabledPageNum() {
        return 0;
    }

    @Override
    protected Elements getVacancyElements(Document doc, int page) {
        if (page == getMinimalEnabledPageNum()) {
            return doc.select("div.vacancy");
        }
        else {
            return  null;
        }
    }

    @Override
    protected Vacancy getVacancyFromElement(Element element) {
        Vacancy vacancy = new Vacancy();

        vacancy.setPublisherSiteName(SITE_NAME);
        vacancy.setIdFromPublisherSite(element.attr("_id").trim());

        Element titleElement = element.select("a.vt").first();
        vacancy.setUrl(titleElement.attr("href"));
        String titleStr = titleElement.text().trim();
        vacancy.setTitle(titleStr);


        Element companyElement = element.select("a.company").first();
        if (companyElement != null) {
            String companyStr = companyElement.text().replaceAll("\\u00a0", " ").trim();
            vacancy.setCompanyName(companyStr);
        }

        Element locationElement = element.select(".cities").first();
        if (locationElement != null) {
            vacancy.setCity(locationElement.text().trim());
        }

//        Element timeAgoElement = element.select("div.j-date").first();
//        String timeAgo = null;
//        if (timeAgoElement != null) {
//            timeAgo = timeAgoElement.text();
//        }
//        vacancy.setVacancyDate(getVacancyDate(timeAgo));

        Element salaryElement = element.select(".salary").first();
        if (salaryElement != null) {
            vacancy.setSalary(salaryElement.text());
        }

        return vacancy;
    }

    @Override
    protected String getUrlOfWantedPage(String vacancyJobString, String vacancyLocationName) {
        String jobString =  vacancyJobString == null ? "" : vacancyJobString.trim().replaceAll("\\s+", "+");
        String locationString = vacancyLocationName == null ? "" : vacancyLocationName.trim();
        locationString = getMappedLocationValue(locationString).replace("\"", "");
        return URL_FORMAT.replace("{JOB_STRING}", jobString).replace("{LOCATION_STRING}", locationString);
    }

    @Override
    public String getVacanciesSourceName() {
        return SITE_NAME;
    }

}
