package com.angola_argentina_portal.lazy;

import org.primefaces.model.LazyDataModel;

import com.angola_argentina_portal.dto.AirlineTableDTO;
import com.angola_argentina_portal.model.Airline;
import com.angola_argentina_portal.service.AirlineService;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public class AirlineLazyModel extends LazyDataModel<AirlineTableDTO> {

    private final AirlineService service;

    public AirlineLazyModel(AirlineService service) {
        this.service = service;
    }

    @Override
    public List<AirlineTableDTO> load(int first,
            int pageSize,
            Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {

        List<Airline> airlines = service.findAll();

        List<AirlineTableDTO> dtos = airlines.stream()
                .map(a -> new AirlineTableDTO(
                        a.getId(),
                        a.getName(),
                        a.getLogoUrl(),
                        a.getCountry(),
                        a.getWebsite(),
                        a.isDirectFlights()))
                .toList();

        int dataSize = dtos.size();

        this.setRowCount(dataSize);

        return dtos.stream()
                .skip(first)
                .limit(pageSize)
                .toList();
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public String getRowKey(AirlineTableDTO entity) {
        return String.valueOf(entity.getId());
    }

    @Override
    public AirlineTableDTO getRowData(String rowKey) {
        int id = Integer.parseInt(rowKey);

        if (this.getWrappedData() != null) {
            for (AirlineTableDTO entity : this.getWrappedData()) {
                if (entity.getId() == id) {
                    return entity;
                }
            }
        }
        return null;
    }
}