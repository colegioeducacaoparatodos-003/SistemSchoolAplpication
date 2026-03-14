package com.angola_argentina_portal.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.angola_argentina_portal.model.TravelAgency;

@Service
public class TravelAgencyService {

    private static final String FILE_PATH = "data/travel_agencies.json";

    private final ObjectMapper mapper = new ObjectMapper();

    public List<TravelAgency> findAll() {

        try {

            File file = new File(FILE_PATH);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(file,
                    new TypeReference<List<TravelAgency>>() {
                    });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(TravelAgency agency) {

        try {

            List<TravelAgency> agencies = findAll();

            long nextId = agencies.stream()
                    .mapToLong(a -> a.getId() == null ? 0 : a.getId())
                    .max()
                    .orElse(0) + 1;

            agency.setId(nextId);

            agencies.add(agency);

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), agencies);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(TravelAgency agency) {

        try {

            List<TravelAgency> agencies = findAll();

            for (int i = 0; i < agencies.size(); i++) {

                if (agencies.get(i).getId().equals(agency.getId())) {
                    agencies.set(i, agency);
                    break;
                }
            }

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), agencies);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {

        try {

            List<TravelAgency> agencies = findAll();

            agencies.removeIf(a -> a.getId().equals(id));

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), agencies);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}