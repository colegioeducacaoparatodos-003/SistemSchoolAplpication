package com.SistemSchool.modulo_Financeiro.lazy;

import com.SistemSchool.modulo_Financeiro.dto.CashBoxDTO;
import com.SistemSchool.modulo_Financeiro.service.CashBoxService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashBoxLazyModel extends LazyDataModel<CashBoxDTO> {

    private static final long serialVersionUID = 1L;

    private final CashBoxService cashBoxService;

    public CashBoxLazyModel(CashBoxService cashBoxService) {
        this.cashBoxService = cashBoxService;
    }

    @Override
    public List<CashBoxDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy,
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

        Page<CashBoxDTO> result = cashBoxService.findLazy(page, pageSize, sort, null);

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
        Page<CashBoxDTO> page = cashBoxService.findLazy(0, 1, Sort.unsorted(), filters);
        return (int) page.getTotalElements();
    }

    @Override
    public CashBoxDTO getRowData(String rowKey) {
        return cashBoxService.getAllCashBoxes()
                .stream()
                .filter(c -> c.getPhCashBox().toString().equals(rowKey))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getRowKey(CashBoxDTO cashBoxDTO) {
        return cashBoxDTO.getPhCashBox() != null
                ? cashBoxDTO.getPhCashBox().toString()
                : null;
    }
}