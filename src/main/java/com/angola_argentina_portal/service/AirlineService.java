package com.angola_argentina_portal.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.angola_argentina_portal.model.Airline;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AirlineService {

    private static final String FILE_PATH = "page_files/airlines.json";

    private final ObjectMapper mapper = new ObjectMapper();

    public List<Airline> findAll() {

        try {

            File file = new File(FILE_PATH);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(file,
                    new TypeReference<List<Airline>>() {
                    });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Airline airline) {

        try {

            List<Airline> airlines = findAll();

            if (airline.getId() == null) {

                long nextId = airlines.stream()
                        .mapToLong(a -> a.getId() == null ? 0 : a.getId())
                        .max()
                        .orElse(0) + 1;

                airline.setId(nextId);
            }

            airlines.add(airline);

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), airlines);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
