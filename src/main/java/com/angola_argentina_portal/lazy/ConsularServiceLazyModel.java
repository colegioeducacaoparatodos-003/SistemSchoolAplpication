package com.angola_argentina_portal.lazy;

import java.util.HashMap;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.angola_argentina_portal.dto.ConsularServiceTableDTO;
import com.angola_argentina_portal.service.ConsularServiceService;

import java.util.List;
import java.util.Map;

public class ConsularServiceLazyModel
        extends LazyDataModel<ConsularServiceTableDTO> {

    private final ConsularServiceService service;

    public ConsularServiceLazyModel(ConsularServiceService service) {
        this.service = service;
    }

    @Override
    public List<ConsularServiceTableDTO> load(
            int first,
            int pageSize,
            Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {

        int page = first / pageSize;

        Sort sort = Sort.unsorted();

        if (!sortBy.isEmpty()) {
            SortMeta meta = sortBy.values().iterator().next();
            sort = Sort.by(
                    meta.getOrder().isAscending()
                            ? Sort.Direction.ASC
                            : Sort.Direction.DESC,
                    meta.getField());
        }

        Page<ConsularServiceTableDTO> result = service.findLazy(page, pageSize, sort);

        setRowCount((int) result.getTotalElements());

        return result.getContent();
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
        Page<ConsularServiceTableDTO> page = service.findLazy(0, 1, Sort.unsorted());
        return (int) page.getTotalElements();
    }

    @Override
    public String getRowKey(ConsularServiceTableDTO dto) {
        return String.valueOf(dto.getPkService());
    }

    @Override
    public ConsularServiceTableDTO getRowData(String rowKey) {
        int id = Integer.parseInt(rowKey);

        if (getWrappedData() != null) {
            for (ConsularServiceTableDTO dto : getWrappedData()) {
                if (dto.getPkService() == id) {
                    return dto;
                }
            }
        }
        return null;
    }
}
