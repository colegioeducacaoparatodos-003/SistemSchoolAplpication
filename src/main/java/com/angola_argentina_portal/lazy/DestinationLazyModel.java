package com.angola_argentina_portal.lazy;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.angola_argentina_portal.dto.DestinationTableDTO;
import com.angola_argentina_portal.service.DestinationService;

import com.angola_argentina_portal.model.Destination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DestinationLazyModel
        extends LazyDataModel<DestinationTableDTO> {

    private final DestinationService service;

    public DestinationLazyModel(DestinationService service) {
        this.service = service;
    }

    @Override
    public List<DestinationTableDTO> load(int first,
            int pageSize,
            Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {

        List<Destination> destinations = service.findAll();

        List<DestinationTableDTO> dtos = destinations.stream()
                .map(d -> new DestinationTableDTO(
                        d.getId(),
                        d.getName(),
                        d.getCity(),
                        d.getCountry(),
                        d.getCategory(),
                        d.getImageUrl()))
                .toList();

        this.setRowCount(dtos.size());

        return dtos.stream()
                .skip(first)
                .limit(pageSize)
                .toList();
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        Map<String, Object> filters = new HashMap<>();

        if (filterBy != null) {
            for (Map.Entry<String, FilterMeta> entry : filterBy.entrySet()) {
                Object value = entry.getValue().getFilterValue();
                if (value != null && !value.toString().isBlank()) {
                    // filters.put(entry.getKey(), value);
                    filters.put(entry.getValue().getField(), value);
                }
            }
        }
        Page<DestinationTableDTO> page = service.findLazy(0, 1, Sort.unsorted(), filters);
        return (int) page.getTotalElements();
    }

    @Override
    public String getRowKey(DestinationTableDTO entity) {
        return String.valueOf(entity.getId());
    }

    @Override
    public DestinationTableDTO getRowData(String rowKey) {
        int id = Integer.parseInt(rowKey);

        if (this.getWrappedData() != null) {
            for (DestinationTableDTO entity : this.getWrappedData()) {
                if (entity.getId() == id) {
                    return entity;
                }
            }
        }
        return null;
    }
}
