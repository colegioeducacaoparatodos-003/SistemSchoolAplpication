package com.angola_argentina_portal.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.angola_argentina_portal.model.Hotel;

@Service
public class HotelService {

    private static final String FILE_PATH = "page_files/hotels.json";

    private final ObjectMapper mapper = new ObjectMapper();

    public List<Hotel> findAll() {

        try {

            File file = new File(FILE_PATH);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(file,
                    new TypeReference<List<Hotel>>() {
                    });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Hotel hotel) {

        try {

            List<Hotel> hotels = findAll();

            long nextId = hotels.stream()
                    .mapToLong(h -> h.getId() == null ? 0 : h.getId())
                    .max()
                    .orElse(0) + 1;

            hotel.setId(nextId);

            hotels.add(hotel);

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), hotels);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Hotel hotel) {

        try {

            List<Hotel> hotels = findAll();

            for (int i = 0; i < hotels.size(); i++) {

                if (hotels.get(i).getId().equals(hotel.getId())) {
                    hotels.set(i, hotel);
                    break;
                }
            }

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), hotels);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {

        try {

            List<Hotel> hotels = findAll();

            hotels.removeIf(h -> h.getId().equals(id));

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), hotels);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}