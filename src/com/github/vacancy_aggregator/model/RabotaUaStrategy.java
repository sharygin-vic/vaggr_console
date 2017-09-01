package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.vo.Vacancy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.Date;

// https://rabota.ua/jobsearch/vacancy_list?regionId=3&keyWords=Java&pg=1
public class RabotaUaStrategy extends AbstractStrategy implements Strategy {
    private static final String SITE_NAME = "https://rabota.ua";
    private static final String URL_FORMAT = SITE_NAME + "/jobsearch/vacancy_list?regionId={LOCATION_STRING}&keyWords={JOB_STRING}&pg={PAGE_VALUE}";

    @Override
    protected int getMinimalEnabledPageNum() {
        return 1;
    }

    @Override
    protected Elements getVacancyElements(Document doc, int page) {
        return doc.select("table.f-vacancylist-tablewrap tr[id~=[0..9]*]");
    }

    @Override
    protected Vacancy getVacancyFromElement(Element element) {
        Vacancy vacancy = new Vacancy();

        //System.out.println(element);

        vacancy.setPublisherSiteName(SITE_NAME);
        vacancy.setIdFromPublisherSite(element.id());

        Element titleElement = element.select(".f-vacancylist-vacancytitle a").first();
        vacancy.setTitle(titleElement.text());
        vacancy.setUrl(SITE_NAME + titleElement.attr("href"));

        Element companyElement = element.select(".f-vacancylist-companyname a").first();
        if (companyElement != null) {
            vacancy.setCompanyName(companyElement.text());
        }

        Element locationElement = element.select("div.f-vacancylist-characs-block p.fd-merchant").first();
        if (locationElement != null) {
            vacancy.setCity(locationElement.text());
        }

        Element salaryElement = element.select("div.f-vacancylist-characs-block p.-price").first();
        if (salaryElement != null) {
            vacancy.setSalary(salaryElement.text());
        }

        Element timeAgoElement = element.select(".f-vacancylist-agotime").first();
        String timeAgo = null;
        if (timeAgoElement != null) {
            timeAgo = timeAgoElement.text();
        }
        vacancy.setVacancyDate(getVacancyDate(timeAgo));

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
