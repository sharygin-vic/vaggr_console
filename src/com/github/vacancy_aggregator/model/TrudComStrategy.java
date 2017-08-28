package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.vo.Vacancy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


// http://ua.trud.com/jobs/?q=java&page=1			all regions
// http://odessa.trud.com/?q=java&page=1
public class TrudComStrategy extends AbstractStrategy implements Strategy {
    private static final String SITE_NAME = "http://trud.com";
    private static final String SITE_MASK_NAME = "http://{LOCATION_STRING}.trud.com";
    private static final String URL_FORMAT = SITE_MASK_NAME + "/jobs/?q={JOB_STRING}&page={PAGE_VALUE}";

    private String jobString;
    private String locationString;

    @Override
    protected int getMinimalEnabledPageNum() {
        return 1;
    }

    @Override
    protected Elements getVacancyElements(Document doc, int page) {
        if (page > getMinimalEnabledPageNum()) {
            Element enabledElem = doc.select("div.card span.available").first();
            if (enabledElem != null) {
                String[] words = enabledElem.text().split(" ");
                if ("1".equals(words[1])) {
                    return null;
                }
            }
        }
        Elements elements = doc.getElementsByClass("card");
        return elements;
    }

    @Override
    protected Vacancy getVacancyFromElement(Element element) {
        Element idElement = element.getElementsByClass("item").first();
        if (idElement == null) {
            return  null;
        }

        Element vipElement = element.getElementsByClass("vip").first();
        if (vipElement != null) {
            return  null;
        }

        Element partnerElement = element.getElementsByAttribute("partner").first();
        String partnerStr = partnerElement.attr("partner");

        Vacancy vacancy = new Vacancy();

        String publisherSiteNameStr = SITE_NAME;
        if (partnerStr != null) {
            publisherSiteNameStr = String.format("%s -> %s", publisherSiteNameStr, partnerStr);
        }
        vacancy.setPublisherSiteName(publisherSiteNameStr);

        vacancy.setIdFromPublisherSite(idElement.id());

        Element titleElement = element.select("a.item-link").first();
        vacancy.setTitle(titleElement.text());
        String urlResource = titleElement.attr("href");
        vacancy.setUrl(SITE_MASK_NAME.replace("{LOCATION_STRING}", locationString) + urlResource);

        Element companyElement = element.select("span.institution span").first();
        if (companyElement == null) {
            companyElement = element.select("a.institution").first();
        }
        if (companyElement != null) {
            vacancy.setCompanyName(companyElement.text());
        }

        Element locationElement = element.select("span.geo-location").first();
        if (locationElement != null) {
            vacancy.setCity(locationElement.text());
        }
        else {
            vacancy.setCity(locationString);
        }

        Element salaryElement = element.select("span.salary").first();
        if (salaryElement != null) {
            vacancy.setSalary(salaryElement.text());
        }

        return vacancy;
    }

    @Override
    protected String getUrlOfWantedPage(String vacancyJobString, String vacancyLocationName) {
        jobString =  vacancyJobString == null ? "" : vacancyJobString.trim().replaceAll("\\s+", "+");
        locationString = vacancyLocationName == null ? "" : vacancyLocationName.trim();
        locationString = getMappedLocationValue(locationString).replace("\"", "");
        String result = URL_FORMAT.replace("{JOB_STRING}", jobString).replace("{LOCATION_STRING}", locationString);
        if (!"ua".equals(locationString)) {
            result = result.replace("/jobs", "");
        }
        return result;
    }

}
