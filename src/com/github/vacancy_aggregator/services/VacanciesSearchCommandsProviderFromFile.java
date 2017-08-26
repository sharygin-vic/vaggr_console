package com.github.vacancy_aggregator.services;

import com.github.vacancy_aggregator.model.VacanciesSearchCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class VacanciesSearchCommandsProviderFromFile implements VacanciesSearchCommandsProvider {
    @Override
    public List<VacanciesSearchCommand> getCommands() {
        ArrayList<VacanciesSearchCommand> result = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PathHelper.getConfigAbsolutePathString() + "VacanciesSearch.script"))) {
            String line;
            while( (line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0 && !line.startsWith("#")) {
                    String[] words = line.split(";");
                    if (words.length >= 2) {
                        result.add(new VacanciesSearchCommand(words[0].trim(), words[1].trim()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
