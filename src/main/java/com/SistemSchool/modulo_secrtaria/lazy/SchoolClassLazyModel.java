package com.SistemSchool.modulo_secrtaria.lazy;

import com.SistemSchool.modulo_secrtaria.dto.SchoolClassDTO;
import com.SistemSchool.modulo_secrtaria.service.SchoolClassService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolClassLazyModel extends LazyDataModel<SchoolClassDTO> {

    private static final long serialVersionUID = 1L;

    private final SchoolClassService schoolClassService;

    public SchoolClassLazyModel(SchoolClassService schoolClassService) {
        this.schoolClassService = schoolClassService;
    }

    @Override
    public List<SchoolClassDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {
        int page = first / pageSize;

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        if (sortBy != null && !sortBy.isEmpty()) {
            SortMeta sortMeta = sortBy.values().iterator().next();
            Sort.Direction direction = sortMeta.getOrder().isAscending()
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;
            sort = Sort.by(direction, sortMeta.getField());
        }

        Page<SchoolClassDTO> result = schoolClassService.findLazy(page, pageSize, sort, null);

        setRowCount((int) result.getTotalElements());

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
        Page<SchoolClassDTO> page = schoolClassService.findLazy(0, 1, Sort.unsorted(), filters);
        return (int) page.getTotalElements();
    }

    @Override
    public SchoolClassDTO getRowData(String rowKey) {
        return schoolClassService.getAllSchoolClasses()
                .stream()
                .filter(sc -> sc.getPkSchoolClass().toString().equals(rowKey))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getRowKey(SchoolClassDTO schoolClassDTO) {
        return schoolClassDTO.getPkSchoolClass() != null
                ? schoolClassDTO.getPkSchoolClass().toString()
                : null;
    }
}