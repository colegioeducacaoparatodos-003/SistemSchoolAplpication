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

import com.angola_argentina_portal.dto.DestinationTableDTO;
import com.angola_argentina_portal.model.Destination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

@Service
public class DestinationService {

    private static final String FILE_PATH = "/page_files/destinations.json";

    private final ObjectMapper mapper = new ObjectMapper();

    public List<Destination> findAll() {

        try {

            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();

            String path = context.getExternalContext().getRealPath("/");

            File file = new File(path + File.separator + "page_files" + File.separator + "destinations.json");

            System.out.println(file.getAbsolutePath());

            if (!file.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    file,
                    new TypeReference<List<Destination>>() {
                    });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Page<DestinationTableDTO> findLazy(int page, int size, Sort sort, Map<String, Object> filters) {

        List<Destination> destinations = findAll();

        List<DestinationTableDTO> dtos = destinations.stream()
                .map(d -> new DestinationTableDTO(
                        d.getId(),
                        d.getName(),
                        d.getCity(),
                        d.getCountry(),
                        d.getCategory(),
                        d.getImageUrl()))
                .toList();

        return new PageImpl<>(
                dtos,
                PageRequest.of(page, size, sort),

                dtos.size());
    }

    public void save(Destination destination) {

        try {

            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();

            String path = context.getExternalContext().getRealPath("/");

            File file = new File(path + File.separator + "page_files" + File.separator + "destinations.json");

            List<Destination> destinations = findAll();

            long nextId = destinations.stream()
                    .mapToLong(d -> d.getId() == null ? 0 : d.getId())
                    .max()
                    .orElse(0) + 1;

            destination.setId(nextId);

            destinations.add(destination);

            file.getParentFile().mkdirs();

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, destinations);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Destination destination) {

        try {

            List<Destination> destinations = findAll();

            for (int i = 0; i < destinations.size(); i++) {

                if (destinations.get(i).getId().equals(destination.getId())) {
                    destinations.set(i, destination);
                    break;
                }
            }

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), destinations);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {

        try {

            List<Destination> destinations = findAll();

            destinations.removeIf(d -> d.getId().equals(id));

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), destinations);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}