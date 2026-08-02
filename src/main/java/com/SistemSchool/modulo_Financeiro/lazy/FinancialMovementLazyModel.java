package com.SistemSchool.modulo_Financeiro.lazy;

import com.SistemSchool.modulo_Financeiro.interfaces.FinancialMovementTableProjection;
import com.SistemSchool.modulo_Financeiro.service.FinancialMovementService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinancialMovementLazyModel extends LazyDataModel<FinancialMovementTableProjection> {

    private static final long serialVersionUID = 1L;

    private final FinancialMovementService financialMovementService;

    public FinancialMovementLazyModel(FinancialMovementService financialMovementService) {
        this.financialMovementService = financialMovementService;
    }

    @Override
    public List<FinancialMovementTableProjection> load(int first, int pageSize, Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {
        int page = first / pageSize;

        Sort sort = Sort.by(Sort.Direction.DESC, "movementDate");

        if (sortBy != null && !sortBy.isEmpty()) {
            SortMeta sortMeta = sortBy.values().iterator().next();
            Sort.Direction direction = sortMeta.getOrder().isAscending()
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;
            sort = Sort.by(direction, sortMeta.getField());
        }

        Page<FinancialMovementTableProjection> result =
                financialMovementService.findLazy(page, pageSize, sort, null);

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
        Page<FinancialMovementTableProjection> page =
                financialMovementService.findLazy(0, 1, Sort.unsorted(), filters);
        return (int) page.getTotalElements();
    }

    @Override
    public FinancialMovementTableProjection getRowData(String rowKey) {
        return financialMovementService.getAllForTable()
                .stream()
                .filter(m -> m.getPhMovement().toString().equals(rowKey))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getRowKey(FinancialMovementTableProjection movement) {
        return movement.getPhMovement() != null
                ? movement.getPhMovement().toString()
                : null;
    }
}