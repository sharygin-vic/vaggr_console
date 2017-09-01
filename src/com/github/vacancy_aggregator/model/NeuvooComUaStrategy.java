package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.vo.Vacancy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.Date;

//https://neuvoo.com.ua/работа/?k=java&l=&p=1
//https://neuvoo.com.ua/работа/?k=java&l=Odesa&p=1
public class NeuvooComUaStrategy extends AbstractStrategy implements Strategy {
    private static final String SITE_NAME = "https://neuvoo.com.ua";
    private static final String URL_FORMAT = SITE_NAME + "/работа/?k={JOB_STRING}&l={LOCATION_STRING}&p={PAGE_VALUE}";

    @Override
    protected int getMinimalEnabledPageNum() {
        return 1;
    }

    @Override
    protected Elements getVacancyElements(Document doc, int page) {
        return doc.select("div.job");
    }

    @Override
    protected Vacancy getVacancyFromElement(Element element) {
        Vacancy vacancy = new Vacancy();

        vacancy.setPublisherSiteName(SITE_NAME);
        vacancy.setIdFromPublisherSite(element.attr("dataid").trim());

        Element titleElement = element.select("a.gojob").first();
        vacancy.setUrl(SITE_NAME + titleElement.attr("href"));
        String titleStr = titleElement.select("span").text().trim();
        vacancy.setTitle(titleStr);


        Element companyElement = element.select("div.j-empname span").first();
        if (companyElement != null) {
            String companyStr = companyElement.text().replaceAll("\\u00a0", " ").trim();
            vacancy.setCompanyName(companyStr);
        }

        Element locationElement = element.select("div.j-location span span").first();
        if (locationElement != null) {
            vacancy.setCity(locationElement.text().trim());
        }

        Element timeAgoElement = element.select("div.j-date").first();
        String timeAgo = null;
        if (timeAgoElement != null) {
            timeAgo = timeAgoElement.text();
        }
        vacancy.setVacancyDate(getVacancyDate(timeAgo));

//        Element salaryElement = element.select("").first();
//        if (salaryElement != null) {
//            vacancy.setSalary(salaryElement.text());
//        }

        return vacancy;
    }

    @Override
    protected String getUrlOfWantedPage(String vacancyJobString, String vacancyLocationName) {
        String jobString =  vacancyJobString == null ? "" : vacancyJobString.trim().replaceAll("\\s+", "+");
        String locationString = vacancyLocationName == null ? "" : vacancyLocationName.trim();
        locationString = getMappedLocationValue(locationString).replace("\"", "");
        return URL_FORMAT.replace("{JOB_STRING}", jobString).replace("{LOCATION_STRING}", locationString);
    }

    // timeAgo =
    //    20 минут назад
    //    5 часов назад
    //    1 день назад
    //    2 дня назад
    //    7 дней назад
    //    1 неделю назад
    //    3 недели назад
    //    2 месяца назад
    private Date getVacancyDate(String timeAgo) {
        if (timeAgo == null) {
            return new Date();
        }

        timeAgo = timeAgo.replace("-", "").trim();

        LocalDateTime ldt = LocalDateTime.now();
        String[] words = timeAgo.split("[\\u00a0\\s]+");
        if (words.length >= 2) {
            int val = Integer.parseInt(words[0].trim());
            if (words[1].contains("мин")) {
                ldt = ldt.minusMinutes(val);
            }
            else if (words[1].contains("час")) {
                ldt = ldt.minusHours(val);
            }
            else if (words[1].contains("ден") || words[1].contains("дня") || words[1].contains("дне")) {
                ldt = ldt.minusDays(val);
            }
            else if (words[1].contains("недел")) {
                ldt = ldt.minusWeeks(val);
            }
            else if (words[1].contains("месяц")) {
                ldt = ldt.minusMonths(val);
            }
        }
        return java.sql.Date.valueOf(ldt.toLocalDate());
    }

    @Override
    public String getVacanciesSourceName() {
        return SITE_NAME;
    }
}

