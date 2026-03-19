package com.angola_argentina_portal.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageImpl;

import com.angola_argentina_portal.dto.AirlineTableDTO;
import com.angola_argentina_portal.dto.GovernmentDTO;
import com.angola_argentina_portal.dto.TravelAgencyTableDTO;
import com.angola_argentina_portal.interfaces.GovernmentTableProjection;
import com.angola_argentina_portal.model.Airline;
import com.angola_argentina_portal.model.TravelAgency;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContext;

@Service
public class AirlineService {

    private static final String FILE_PATH = "page_files/airlines.json";

    private final ObjectMapper mapper = new ObjectMapper();

    public List<Airline> findAll() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();

            String path = context.getExternalContext().getRealPath("/");

            File file = new File(path + File.separator + "page_files" + File.separator + "airlines.json");

            System.out.println(file.getAbsolutePath());

            if (!file.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    file,
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

    public Page<AirlineTableDTO> findLazy(int page, int size, Sort sort, Map<String, Object> filters) {

        List<Airline> airlines = findAll();

        List<AirlineTableDTO> dtos = airlines.stream()
                .map(a -> new AirlineTableDTO(
                        a.getId(),
                        a.getName(),
                        a.getLogoUrl(),
                        a.getCountry(),
                        a.getWebsite(),
                        a.isDirectFlights()))
                .toList();

        return new PageImpl<>(
                dtos,
                PageRequest.of(page, size, sort),
                dtos.size());
    }
}
