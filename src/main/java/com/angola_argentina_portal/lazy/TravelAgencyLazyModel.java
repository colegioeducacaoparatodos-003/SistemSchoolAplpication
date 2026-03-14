package com.angola_argentina_portal.lazy;

import org.primefaces.model.LazyDataModel;

import com.angola_argentina_portal.dto.TravelAgencyTableDTO;
import com.angola_argentina_portal.model.TravelAgency;
import com.angola_argentina_portal.service.TravelAgencyService;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public class TravelAgencyLazyModel
        extends LazyDataModel<TravelAgencyTableDTO> {

    private final TravelAgencyService service;

    public TravelAgencyLazyModel(TravelAgencyService service) {
        this.service = service;
    }

    @Override
    public List<TravelAgencyTableDTO> load(int first,
            int pageSize,
            Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {

        List<TravelAgency> agencies = service.findAll();

        List<TravelAgencyTableDTO> dtos = agencies.stream()
                .map(a -> new TravelAgencyTableDTO(
                        a.getId(),
                        a.getName(),
                        a.getLogoUrl(),
                        a.getCity(),
                        a.getPhone(),
                        a.getWebsite()))
                .toList();

        this.setRowCount(dtos.size());

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
    public String getRowKey(TravelAgencyTableDTO entity) {
        return String.valueOf(entity.getId());
    }

    @Override
    public TravelAgencyTableDTO getRowData(String rowKey) {
        int id = Integer.parseInt(rowKey);

        if (this.getWrappedData() != null) {
            for (TravelAgencyTableDTO entity : this.getWrappedData()) {
                if (entity.getId() == id) {
                    return entity;
                }
            }
        }
        return null;
    }
}