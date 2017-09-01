package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.vo.Vacancy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.Date;

//https://ua.indeed.com/вакансии?q=java&l=Одесса&start=0
public class UaIndeedComStrategy extends AbstractStrategy implements Strategy {
    private static final String SITE_NAME = "https://ua.indeed.com";
    private static final String URL_FORMAT = SITE_NAME + "/вакансии?q={JOB_STRING}&l={LOCATION_STRING}&start={PAGE_VALUE}";
    private static final int VACANCY_ITEMS_PER_PAGE = 10;

    private String prevPrevPageLabel = null;
    private String prevPageLabel = null;

    @Override
    protected int getMinimalEnabledPageNum() {
        return 0;
    }

    @Override
    protected Elements getVacancyElements(Document doc, int page) {
        Element vacanciesCountElement = doc.select("div#searchCount").first();
        if (vacanciesCountElement == null) {
            return null;
        }
        String vacanciesCountStr = vacanciesCountElement.text().trim();
        if (vacanciesCountStr != null && (vacanciesCountStr.equals(prevPrevPageLabel) || vacanciesCountStr.equals(prevPageLabel))) {
            prevPrevPageLabel = null;
            prevPageLabel = null;
            return null;
        }

        prevPrevPageLabel = prevPageLabel;
        prevPageLabel = vacanciesCountStr;
        return doc.select("div.result");
    }

    @Override
    protected Vacancy getVacancyFromElement(Element element) {
        Element sponsoredElement = element.select(".sponsoredGray").first();
        if (sponsoredElement != null || element.hasClass("sjlast")) {
            return null;
        }

        Vacancy vacancy = new Vacancy();

        vacancy.setPublisherSiteName(SITE_NAME);

        vacancy.setIdFromPublisherSite(element.attr("data-jk").trim());

        Element titleElement = element.select(".jobtitle a").first();
        vacancy.setUrl(SITE_NAME + titleElement.attr("href"));
        String titleStr = titleElement.text().trim();
        vacancy.setTitle(titleStr);

        Element companyElement = element.select(".company span").first();
        if (companyElement != null) {
            String companyStr = companyElement.text().replaceAll("\\u00a0", " ").trim();
            vacancy.setCompanyName(companyStr);
        }

        Element locationElement = element.select(".location span").first();
        if (locationElement != null) {
            vacancy.setCity(locationElement.text().trim());
        }

        Element timeAgoElement = element.select(".date").first();
        String timeAgo = null;
        if (timeAgoElement != null) {
            timeAgo = timeAgoElement.text();
        }
        vacancy.setVacancyDate(getVacancyDate(timeAgo));

        // No salary data
//        Element salaryElement = element.select(".salary").first();
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

    @Override
    protected int getPageValue(int pageNum) {
        return pageNum * VACANCY_ITEMS_PER_PAGE;
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
            boolean containsPlus = false;
            if (words[0].contains("+")) {
                containsPlus = true;
            }
            words[0] = words[0].replace("+", "").trim();
            int val = Integer.parseInt(words[0].trim());

            if (words[1].contains("мин")) {
                ldt = ldt.minusMinutes(val);
            }
            else if (words[1].contains("час")) {
                ldt = ldt.minusHours(val);
            }
            else if (words[1].contains("ден") || words[1].contains("дня") || words[1].contains("дне")) {
                if (containsPlus) {
                    val += 30;
                }
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

