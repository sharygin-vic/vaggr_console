package com.github.vacancy_aggregator.vo;

import java.util.Date;

/**
 * Created by dell on 09-Jul-17.
 */
public class Vacancy {
    private String title;                 //vacancy title
    private String url;                   //vacancy detail URL from publisher site
    private String salary;                //vacancy salary
    private String city;                  //vacancy city
    private String companyName;           //Company employer
    private String publisherSiteName;     //vacancy publisher site
    private String idFromPublisherSite;   //publisher site's vacancy ID
    private Date vacancyDate;             //date of vacancy publishing
    private String job;                   //job type of vacancy
    //private String companySite;         //site of employer company

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPublisherSiteName() {
        return publisherSiteName;
    }

    public void setPublisherSiteName(String publisherSiteName) {
        this.publisherSiteName = publisherSiteName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getIdFromPublisherSite() {
        return idFromPublisherSite;
    }

    public void setIdFromPublisherSite(String idFromPublisherSite) {
        this.idFromPublisherSite = idFromPublisherSite;
    }

    public Date getVacancyDate() {
        return vacancyDate;
    }

    public void setVacancyDate(Date vacancyDate) {
        this.vacancyDate = vacancyDate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    //public String getCompanySite() {
    //    return companySite;
    //}

    //public void setCompanySite(String companySite) {
    //    this.companySite = companySite;
    //}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vacancy vacancy = (Vacancy) o;

        if (title != null ? !title.equals(vacancy.title) : vacancy.title != null) return false;
        if (url != null ? !url.equals(vacancy.url) : vacancy.url != null) return false;
        if (salary != null ? !salary.equals(vacancy.salary) : vacancy.salary != null) return false;
        if (city != null ? !city.equals(vacancy.city) : vacancy.city != null) return false;
        if (companyName != null ? !companyName.equals(vacancy.companyName) : vacancy.companyName != null) return false;
        if (publisherSiteName != null ? !publisherSiteName.equals(vacancy.publisherSiteName) : vacancy.publisherSiteName != null)
            return false;
        if (idFromPublisherSite != null ? !idFromPublisherSite.equals(vacancy.idFromPublisherSite) : vacancy.idFromPublisherSite != null)
            return false;
        if (vacancyDate != null ? !vacancyDate.equals(vacancy.vacancyDate) : vacancy.vacancyDate != null) return false;
        //if (companySite != null ? !companySite.equals(vacancy.companySite) : vacancy.companySite != null) return false;
        return job != null ? job.equals(vacancy.job) : vacancy.job == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (salary != null ? salary.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (publisherSiteName != null ? publisherSiteName.hashCode() : 0);
        result = 31 * result + (idFromPublisherSite != null ? idFromPublisherSite.hashCode() : 0);
        result = 31 * result + (vacancyDate != null ? vacancyDate.hashCode() : 0);
        result = 31 * result + (job != null ? job.hashCode() : 0);
        //result = 31 * result + (companySite != null ? companySite.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Vacancy{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", salary='" + salary + '\'' +
                ", city='" + city + '\'' +
                ", companyName='" + companyName + '\'' +
                ", publisherSiteName='" + publisherSiteName + '\'' +
                ", idFromPublisherSite='" + idFromPublisherSite + '\'' +
                ", vacancyDate=" + vacancyDate +
                ", job='" + job + '\'' +
                //", companySite='" + companySite + '\'' +
                '}';
    }
}
