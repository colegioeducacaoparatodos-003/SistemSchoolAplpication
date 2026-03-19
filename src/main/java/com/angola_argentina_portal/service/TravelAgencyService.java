package com.angola_argentina_portal.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.angola_argentina_portal.dto.HotelTableDTO;
import com.angola_argentina_portal.dto.TravelAgencyTableDTO;
import com.angola_argentina_portal.model.Airline;
import com.angola_argentina_portal.model.Hotel;
import com.angola_argentina_portal.model.TravelAgency;

@Service
public class TravelAgencyService {

    private static final String FILE_PATH = "page_files/travel_agencies.json";

    private final ObjectMapper mapper = new ObjectMapper();

    public List<TravelAgency> findAll() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();

            String path = context.getExternalContext().getRealPath("/");

            File file = new File(path + File.separator + "page_files" + File.separator + "travel_agencies.json");

            System.out.println(file.getAbsolutePath());

            if (!file.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    file,
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

    public Page<TravelAgencyTableDTO> findLazy(int page, int size, Sort sort, Map<String, Object> filters) {

        List<TravelAgency> agencies = findAll();

        List<TravelAgencyTableDTO> dtos = agencies.stream()
                .map(a -> new TravelAgencyTableDTO(
                        a.getId(),
                        a.getName(),
                        a.getLogoUrl(),
                        a.getCity(),
                        a.getPhone(),
                        a.getWebsite()))
                .toList();

        return new PageImpl<>(
                dtos,
                PageRequest.of(page, size, sort),
                dtos.size());
    }

}