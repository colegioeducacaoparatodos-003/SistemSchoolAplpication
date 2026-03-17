package com.angola_argentina_portal.lazy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.angola_argentina_portal.dto.ConsularServicesDTO;
import com.angola_argentina_portal.dto.NewsTableDTO;
import com.angola_argentina_portal.service.ConsularService;

public class CosularServiceLazyModel extends LazyDataModel<ConsularServicesDTO> {

    private final ConsularService consularService;

    public CosularServiceLazyModel(ConsularService consularService) {
        this.consularService = consularService;
    }

    
    @Override
    public List<ConsularServicesDTO> load(
            int first,
            int pageSize,
            Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {

        int page = first / pageSize;

        // Definindo ordenação
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            SortMeta meta = sortBy.values().iterator().next();
            sort = Sort.by(
                    meta.getOrder().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC,
                    meta.getField());
        }

        // Convertendo filtros
        Map<String, Object> filters = new HashMap<>();
        if (filterBy != null) {
            for (FilterMeta meta : filterBy.values()) {
                Object value = meta.getFilterValue();
                if (value != null && !value.toString().isBlank()) {
                    filters.put(meta.getField(), value);
                }
            }
        }

        Page<ConsularServicesDTO> result = consularService.findLazy(page, pageSize, sort, filters);

        this.setRowCount((int) result.getTotalElements());
        return result.getContent();
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        Map<String, Object> filters = new HashMap<>();
        if (filterBy != null) {
            for (FilterMeta meta : filterBy.values()) {
                Object value = meta.getFilterValue();
                if (value != null && !value.toString().isBlank()) {
                    filters.put(meta.getField(), value);
                }
            }
        }
        Page<ConsularServicesDTO> page = consularService.findLazy(0, 1, Sort.unsorted(), filters);
        return (int) page.getTotalElements();
    }

    @Override
    public String getRowKey(ConsularServicesDTO dto) {
        return String.valueOf(dto.getServiceName());
    }

    // @Override
    // public ConsularServicesDTO getRowData(String rowKey) {
    //     if (getWrappedData() != null) {
    //         for (NewsTableDTO dto : getWrappedData()) {
    //             if (dto.getId().toString().equals(rowKey)) {
    //                 return dto;
    //             }
    //         }
    //     }
    //     return null;
    // }

}
