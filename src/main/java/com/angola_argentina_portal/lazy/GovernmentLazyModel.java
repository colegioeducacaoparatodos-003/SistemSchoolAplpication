package com.angola_argentina_portal.lazy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.angola_argentina_portal.dto.GovernmentDTO;
import com.angola_argentina_portal.dto.NewsTableDTO;
import com.angola_argentina_portal.service.GovernmentService;

public class GovernmentLazyModel extends LazyDataModel<GovernmentDTO> {

    private final GovernmentService governmentService;

    public GovernmentLazyModel(GovernmentService governmentService){
        this.governmentService = governmentService;
    }


    @Override
    public List<GovernmentDTO> load(
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

        Page<GovernmentDTO> result = governmentService.findLazy(page, pageSize, sort);

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
        Page<GovernmentDTO> page = governmentService.findLazy(0, 1, Sort.unsorted());
        return (int) page.getTotalElements();
    }

        @Override
    public String getRowKey(GovernmentDTO governmentDTO) {
        return String.valueOf(governmentDTO.getId());
    }

    @Override
    public GovernmentDTO getRowData(String rowKey) {
        if (getWrappedData() != null) {
            for (GovernmentDTO governmentDTO : getWrappedData()) {
                if (governmentDTO.getId().toString().equals(rowKey)) {
                    return governmentDTO;
                }
            }
        }
        return null;
    }
}
