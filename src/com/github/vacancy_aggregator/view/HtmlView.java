package com.github.vacancy_aggregator.view;

import com.github.vacancy_aggregator.Controller;
import com.github.vacancy_aggregator.model.VacanciesSearchCommand;
import com.github.vacancy_aggregator.vo.Vacancy;
import com.github.vacancy_aggregator.services.PathHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by dell on 11-Jul-17.
 */
public class HtmlView implements View {
    private final String htmlTemplateFilePath = PathHelper.getConfigSysAbsolutePathString() + "result_tmpl.html";
    private Controller controller;

    @Override
    public void update(List<Vacancy> vacancies, String vacancyJobString, String vacancyLocationName) {
        try {
            updateFile(getUpdatedFileContent(vacancies, vacancyJobString, vacancyLocationName), vacancyJobString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userSearchParamsChangedEmulationMethod(VacanciesSearchCommand vacanciesSearchCommand) {
        controller.onSearchParamsChanged(vacanciesSearchCommand);
    }

    public void userSearchParamsChangedEmulationMethod(List<VacanciesSearchCommand> commands) {
        controller.onSearchParamsChanged(commands);
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies, String vacancyJobString, String vacancyLocationName) {
        try {
            Document doc = getDocument();

            doc.getElementsByClass("vacancy_job").first().appendText(String.format("%s  (%s)", vacancyJobString, vacancyLocationName));

            Element tempElement = doc.getElementsByClass("template").first();

            Element tempNewElement = tempElement.clone();
            tempNewElement.removeAttr("style");
            tempNewElement.removeClass("template");

            int index = 0;
            for (Vacancy vacancy : vacancies) {
                index++;
                Element vacansyElement = tempNewElement.clone();
                vacansyElement.getElementsByClass("index").first().appendText("" + index);
                if (vacancy.getCity() != null) {
                    vacansyElement.getElementsByClass("city").first().appendText(vacancy.getCity());
                }
                if (vacancy.getCompanyName() != null) {
                    vacansyElement.getElementsByClass("companyName").first().appendText(vacancy.getCompanyName());
                }
                if (vacancy.getSalary() != null) {
                    vacansyElement.getElementsByClass("salary").first().appendText(vacancy.getSalary());
                }
                if (vacancy.getPublisherSiteName() != null) {
                    vacansyElement.getElementsByClass("publisher").first().appendText(vacancy.getPublisherSiteName());
                }
                if (vacancy.getIdFromPublisherSite() != null) {
                    vacansyElement.getElementsByClass("publisher_id").first().appendText(vacancy.getIdFromPublisherSite());
                }
                if (vacancy.getVacancyDate() != null) {
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd");
                    String dateStr = fmt.format(vacancy.getVacancyDate());
                    vacansyElement.getElementsByClass("date").first().appendText(dateStr);
                }

                Element refElement = vacansyElement.getElementsByClass("title").first().getElementsByTag("a").first();
                if (vacancy.getTitle() != null) {
                    refElement.appendText(vacancy.getTitle());
                }
                if (vacancy.getUrl() != null) {
                    refElement.attr("href", vacancy.getUrl());
                }

                tempElement.before(vacansyElement.outerHtml());
            }
            return doc.html();
        }
        catch (IOException e) {
            e.printStackTrace();
            return new StringBuilder()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"ru\">\n")
                .append("<head>\n")
                .append("    <meta charset=\"utf-8\">\n")
                .append("    <title>Вакансии</title>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("    Some exception occurred\n")
                .append("</body>\n")
                .append("</html>\n")
                .toString();
        }
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(Paths.get(htmlTemplateFilePath).toFile(), "UTF-8");

    }

    private void updateFile(String fileContent, String vacancyJobString) {
        if (fileContent == null || fileContent.length() == 0) {
            return;
        }
        String fileName = PathHelper.generateNextHtmlResultAbsolutePathString(vacancyJobString);
        try (
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Paths.get(fileName).toFile()),StandardCharsets.UTF_8))
                //BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8);
        ) {
            writer.write(fileContent);
            openFileInBrowser(fileName);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void openFileInBrowser(String fileName) throws URISyntaxException, IOException {
        String url = "file:///" + fileName.replace(" ", "%20").replace("\\", "/");

        if(Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            URI uri = new URI(url);
            desktop.browse(uri);
        }
        else {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("xdg-open " + url);
        }
    }

}
