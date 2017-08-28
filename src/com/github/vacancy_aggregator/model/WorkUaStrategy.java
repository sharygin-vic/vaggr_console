package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.vo.Vacancy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// https://www.work.ua/jobs-odesa-java+sql/?page=1
public class WorkUaStrategy  extends AbstractStrategy implements Strategy {
    private static final String SITE_NAME = "https://www.work.ua";
    private static final String URL_FORMAT = SITE_NAME + "/jobs{LOCATION_STRING}{JOB_STRING}/?page={PAGE_VALUE}";

    @Override
    protected int getMinimalEnabledPageNum() {
        return 1;
    }

    @Override
    protected Elements getVacancyElements(Document doc, int page) {
        return doc.getElementsByClass("job-link");
    }

    // Element :
    //<div class="card card-hover card-visited job-link">
    //  next div is optional:
    //    <div class="logo-img">
    //        <img width="100" height="50" alt="Web AMG" src="//i.work.ua/employer_design/0/5/5/768055_company_logo_1.png">
    //    </div>
    //    <h2><a href="/jobs/2721081/" title="Senior Automation (Java) QA Engineer (SDET), вакансия от 21.08.17">Senior Automation (Java) QA Engineer (SDET)</a>
    //        next span is optional:
    //        <span class="text-muted">
    //            <span>,</span>
    //            <span class="nowrap " data-toggle="popover" data-content="<span class='wordwrap'>в евро, брутто</span>"> 45000&nbsp;грн<sup>*</sup></span>
    //        </span>
    //    </h2>
    //    <div>
    //        <span>Web AMG</span>&nbsp;
    //        <span data-content="Компания использует услугу «Бизнес-размещение» Подлинность компании подтверждена Work.ua
    //            <a href='/help/?id=177' class='bp-more' target='_blank'>Подробнее
    //                <span class='glyphicon glyphicon-chevron-right'></span>
    //            </a>" data-toggle="popover">
    //            <span class="glyphicon glyphicon-business text-success "></span>
    //        </span>&nbsp;
    //        <span class="text-muted ">· </span>
    //        <span>Киев
    //            <span class="text-muted">&nbsp;·&nbsp;</span>
    //            <span class="label label-hot">Горячая</span>
    //        </span>
    //    </div>
    //    <p class="text-muted overflow"> Полная занятость. Опыт работы от 2 лет.<br> Мы&nbsp;приглашаем Senior Automation (Java) QA&nbsp;Engineer (SDET) присоединится к&nbsp;команде разработчиков и&nbsp;принять…<a href="/jobs/2721081/"><span class="glyphicon glyphicon-chevron-right"></span></a></p>
    //</div>
    @Override
    protected Vacancy getVacancyFromElement(Element element) {
        Vacancy vacancy = new Vacancy();
        vacancy.setPublisherSiteName(SITE_NAME);

        vacancy.setTitle(element.getElementsByAttribute("title").get(0).text().trim());

        String url = element.getElementsByAttribute("title").get(0).attr("href").trim();
        vacancy.setUrl(SITE_NAME + url);

        String[] urlParams = url.split("/");
        vacancy.setIdFromPublisherSite(urlParams[2].trim());

        String dateStr = element.getElementsByAttribute("title").get(0).attr("title").trim();
        int pos = dateStr.toLowerCase().indexOf("вакансия от");
        if (pos >= 0) {
            dateStr = dateStr.substring(pos + "вакансия от".length()).trim();
            vacancy.setVacancyDate(getVacancyDate(dateStr));
        }

        Elements children = element.children();
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            if (child.is("div")) {
                if (child.hasClass("logo-img")) {
                    continue;
                }
                Elements spans = child.children();
                String company = spans.get(0).text().trim();
                vacancy.setCompanyName(company);

                for (int j = 1; j < spans.size(); j++) {
                    Element span = spans.get(j);
                    if (span.hasClass("text-muted")) {
                        String city = spans.get(j+1).text().split("·")[0].trim();
                        vacancy.setCity(city);
                        break;
                    }
                }
            }
            else if (child.is("h2")) {
                // may be a salary
                Elements spans = child.getElementsByTag("span");
                for (Element span : spans) {
                    if (span.hasClass("nowrap")) {
                        String salary = span.text().trim();
                        if (salary.endsWith("*")) {
                            salary = salary.substring(0, salary.length()-1);
                        }
                        vacancy.setSalary(salary);
                    }
                }
            }
        }
        return vacancy;
    }

    @Override
    protected String getUrlOfWantedPage(String vacancyJobString, String vacancyLocationName) {
        String jobString =  vacancyJobString == null ? "" : vacancyJobString.trim().replaceAll("\\s+", "+");
        if (jobString.length() > 0) {
            jobString = "-" + jobString;
        }
        String locationString = vacancyLocationName == null ? "" : vacancyLocationName.trim();
        //if (!locationString.equals("")) {
            locationString = getMappedLocationValue(locationString).replace("\"", "");
        //}
        if (locationString.length() > 0) {
            locationString = "-" + locationString;
        }
        return URL_FORMAT.replace("{JOB_STRING}", jobString).replace("{LOCATION_STRING}", locationString);
    }

    private Date getVacancyDate(String dateStr) {
        Date result = null;
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yy");
        try {
            result = fmt.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

}
