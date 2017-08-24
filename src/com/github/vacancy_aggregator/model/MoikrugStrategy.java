package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dell on 12-Jul-17.
 */


public class MoikrugStrategy extends AbstractStrategy implements Strategy {
    private static final String SITE_NAME = "https://moikrug.ru";
    private static final String URL_FORMAT = SITE_NAME + "/vacancies?q={JOB_STRING}&city_id={LOCATION_STRING}&page={PAGE_VALUE}";

    @Override
    protected int getMinimalEnabledPageNum() {
        return 1;
    }

    @Override
    protected Elements getVacancyElements(Document doc) {
        return doc.getElementsByClass("job");
    }

    @Override
    protected Vacancy getVacancyFromElement(Element element) {
        Vacancy vacancy = new Vacancy();

        vacancy.setIdFromPublisherSite(element.id());

        Element titleHrefElement = element.getElementsByClass("title").first().getElementsByAttribute("href").first();
        vacancy.setTitle(titleHrefElement.text());
        vacancy.setUrl(SITE_NAME + titleHrefElement.attr("href"));

        Element companyHrefElement = element.getElementsByClass("company_name").first();  //.getElementsByAttribute("href").first();
        vacancy.setCompanyName(companyHrefElement.text());  //(siteName + companyHrefElement.attr("href"));

        Element salaryElement = element.getElementsByClass("salary").first().getElementsByClass("count").first();
        vacancy.setSalary( (salaryElement != null) ? salaryElement.text() : "");

        Element cityElement = element.getElementsByClass("location").first();
        Element cityTitleElement = null;
        if (cityElement != null) {
            cityTitleElement = cityElement.getElementsByAttribute("href").first();
        }
        vacancy.setCity( (cityTitleElement != null) ? cityTitleElement.text() : "");

        Element dateElement = element.getElementsByClass("date").first();
        SimpleDateFormat fmt = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
        Date date = null;
        try {
            date = (dateElement != null) ? fmt.parse(dateElement.text()) : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        vacancy.setVacancyDate(date);

        vacancy.setPublisherSiteName(SITE_NAME);
        //vacancy.setResponsibility();
        //vacancy.setRequirement();

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

}
