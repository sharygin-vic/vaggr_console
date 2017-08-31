package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.vo.Vacancy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.Date;

// https://ua.jooble.org/работа-java/Одесса?p=1
public class UaJoobleOrgStrategy extends AbstractStrategy implements Strategy {
    private static final String SITE_NAME = "https://ua.jooble.org";
    private static final String URL_FORMAT = SITE_NAME + "/работа{JOB_STRING}/{LOCATION_STRING}?p={PAGE_VALUE}";

    @Override
    protected int getMinimalEnabledPageNum() {
        return 1;
    }

    @Override
    protected Elements getVacancyElements(Document doc, int page) {
        return doc.select("div.vacancy_wrapper");
    }

    @Override
    protected Vacancy getVacancyFromElement(Element element) {
        Vacancy vacancy = new Vacancy();

        vacancy.setPublisherSiteName(SITE_NAME);

        vacancy.setIdFromPublisherSite(element.id().trim());

        Element titleElement = element.select("a.link-position").first();
        vacancy.setUrl(titleElement.attr("href"));
        String titleStr = titleElement.select(".position").text().trim();
        vacancy.setTitle(titleStr);

        Element companyElement = element.select(".company-name").first();
        if (companyElement != null) {
            String companyStr = companyElement.text().replaceAll("\\u00a0", " ").trim();
            vacancy.setCompanyName(companyStr);
        }

        Element locationElement = element.select(".job-location").first();
        if (locationElement != null) {
            vacancy.setCity(locationElement.text().trim());
        }

        Element timeAgoElement = element.select(".date_add").first();
        String timeAgo = null;
        if (timeAgoElement != null) {
            timeAgo = timeAgoElement.text().trim();
        }
        Date vacansyDate = getVacancyDate(timeAgo);
        if (vacansyDate == null) {
            return null;
        }
        vacancy.setVacancyDate(vacansyDate);

        Element salaryElement = element.select(".salary").first();
        if (salaryElement != null) {
            vacancy.setSalary(salaryElement.text().trim());
        }

        return vacancy;
    }

    @Override
    protected String getUrlOfWantedPage(String vacancyJobString, String vacancyLocationName) {
        String jobString =  vacancyJobString == null ? "" : "-" + vacancyJobString.trim().replaceAll("\\s+", "+");
        String locationString = vacancyLocationName == null ? "" : vacancyLocationName.trim();
        locationString = getMappedLocationValue(locationString).replace("\"", "");
        String result = URL_FORMAT.replace("{JOB_STRING}", jobString);
        if ("".equals(locationString)) {
            return result.replace("/{LOCATION_STRING}", locationString);
        }
        else {
            return result.replace("{LOCATION_STRING}", locationString);
        }
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
        boolean containsBigger = timeAgo.contains("более");
        timeAgo = timeAgo.replace("более", "").replace("Быстрый отклик", "").trim();

        LocalDateTime ldt = LocalDateTime.now();
        String[] words = timeAgo.split("[\\u00a0\\s]+");
        if (words.length >= 2) {


            words[0] = words[0].replace("+", "").trim();
            int val = Integer.parseInt(words[0].trim());

            if (words[1].contains("мин")) {
                ldt = ldt.minusMinutes(val);
            }
            else if (words[1].contains("час")) {
                ldt = ldt.minusHours(val);
            }
            else if (words[1].contains("ден") || words[1].contains("дня") || words[1].contains("дне")) {
                if (val > 31) {
                    return null;
                }
                ldt = ldt.minusDays(val);
            }
            else if (words[1].contains("недел")) {
                ldt = ldt.minusWeeks(val);
            }
            else if (words[1].contains("месяц")) {
                if (containsBigger) {
                    val += 1;
                }
                if (val > 1) {
                    return null;
                }
                ldt = ldt.minusMonths(val);
            }
        }
        return java.sql.Date.valueOf(ldt.toLocalDate());
    }
}
