package com.angola_argentina_portal.lazy;

import com.angola_argentina_portal.dto.NewsTableDTO;
import com.angola_argentina_portal.service.NewsService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsLazyModel extends LazyDataModel<NewsTableDTO> {

    private final NewsService service;

    public NewsLazyModel(NewsService service) {
        this.service = service;
    }

    @Override
    public List<NewsTableDTO> load(
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
                    meta.getField()
            );
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

        Page<NewsTableDTO> result = service.findLazy(page, pageSize, sort, filters);

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
        Page<NewsTableDTO> page = service.findLazy(0, 1, Sort.unsorted(), filters);
        return (int) page.getTotalElements();
    }

    @Override
    public String getRowKey(NewsTableDTO dto) {
        return String.valueOf(dto.getId());
    }

    @Override
    public NewsTableDTO getRowData(String rowKey) {
        if (getWrappedData() != null) {
            for (NewsTableDTO dto : getWrappedData()) {
                if (dto.getId().toString().equals(rowKey)) {
                    return dto;
                }
            }
        }
        return null;
    }
}