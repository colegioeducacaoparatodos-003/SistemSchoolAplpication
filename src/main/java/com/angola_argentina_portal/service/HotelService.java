package com.angola_argentina_portal.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContext;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.angola_argentina_portal.dto.HotelTableDTO;
import com.angola_argentina_portal.model.Hotel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

@Service
public class HotelService {

    private static final String FILE_PATH = "page_files/hotels.json";

    private final ObjectMapper mapper = new ObjectMapper();

    public List<Hotel> findAll() {

        try {

            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();

            String path = context.getExternalContext().getRealPath("/");

            File file = new File(path + File.separator + "page_files" + File.separator + "hotels.json");

            System.out.println(file.getAbsolutePath());

            if (!file.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    file,
                    new TypeReference<List<Hotel>>() {
                    });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Page<HotelTableDTO> findLazy(int page, int size, Sort sort, Map<String, Object> filters) {

        List<Hotel> hotels = findAll();

        List<HotelTableDTO> dtos = hotels.stream()
                .map(d -> new HotelTableDTO(
                        d.getId(),
                        d.getName(),
                        d.getCity(),
                        d.getStars(),
                        d.getPhone(),
                        d.getImageUrl(),
                        d.getAddress(),
                        d.getEmail(),
                        d.getWebsite(),
                        d.getMapLocation()))
                .toList();

        return new PageImpl<>(
                dtos,
                PageRequest.of(page, size, sort),

                dtos.size());
    }

    public void save(Hotel hotel) {

        try {

            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();

            String path = context.getExternalContext().getRealPath("/");

            File file = new File(path + File.separator + "page_files" + File.separator + "hotels.json");

            List<Hotel> hotels = findAll();

            long nextId = hotels.stream()
                    .mapToLong(d -> d.getId() == null ? 0 : d.getId())
                    .max()
                    .orElse(0) + 1;

            hotel.setId(nextId);

            hotels.add(hotel);

            file.getParentFile().mkdirs();

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, hotels);

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