package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.vo.Vacancy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by dell on 09-Jul-17.
 */

// https://hh.ua/search/vacancy?text=java&area=127&page=0
public class HHStrategy extends AbstractStrategy implements Strategy {
    private static final String SITE_NAME = "https://hh.ua";
    private static final String URL_FORMAT = SITE_NAME + "/search/vacancy?text={JOB_STRING}&area={LOCATION_STRING}&page={PAGE_VALUE}";

    @Override
    protected int getMinimalEnabledPageNum() {
        return 0;
    }

    @Override
    protected Elements getVacancyElements(Document doc) {
        return doc.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");
    }

    @Override
    protected Vacancy getVacancyFromElement(Element element) {
        Vacancy vacancy = new Vacancy();
        vacancy.setTitle(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").get(0).text());
        String salary = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text();
        vacancy.setSalary( (salary != null) ? salary : "");
        vacancy.setCity(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").get(0).text());
        vacancy.setCompanyName(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").get(0).text());
        vacancy.setPublisherSiteName(SITE_NAME);
        vacancy.setUrl(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").get(0).attr("href"));
        String[] urlParams = vacancy.getUrl().substring((SITE_NAME + "/vacancy/").length()).split("\\?");
        vacancy.setIdFromPublisherSite(urlParams[0]);

        String dayMonthStr = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-date").text();
        vacancy.setVacancyDate(getVacancyDate(dayMonthStr));

//      vacancy.setResponsibility(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy_snippet_responsibility").get(0).text());
//      vacancy.setRequirement(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy_snippet_requirement").get(0).text());
        return vacancy;
    }

    @Override
    protected String getUrlOfWantedPage(String vacancyJobString, String vacancyLocationName) {
        String jobString =  vacancyJobString == null ? "" : vacancyJobString.trim().replaceAll("\\s+", "+");
        String locationString = vacancyLocationName == null ? "" : vacancyLocationName;
        if (!locationString.equals("")) {
            locationString = getMappedLocationValue(locationString);
        }
        return URL_FORMAT.replace("{JOB_STRING}", jobString).replace("{LOCATION_STRING}", locationString);
    }

    private Date getVacancyDate(String dayMonth) {
        Date result;
        String[] months = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        String[] dayMonthArray = dayMonth.split("[\\u00a0\\s]");
        int monthNum = -1;
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(dayMonthArray[1])) {
                monthNum = i+1;
                break;
            }
        }
        int dayNum = Integer.parseInt(dayMonthArray[0]);
        if (monthNum >= 0) {
            LocalDate ld = LocalDate.now().withMonth(monthNum).withDayOfMonth(dayNum);
            if (ld.isAfter(LocalDate.now())) {
                ld.minusYears(1);
            }
            result = java.sql.Date.valueOf(ld);
        }
        else {
            result = new Date();
        }
        return result;
    }

}
