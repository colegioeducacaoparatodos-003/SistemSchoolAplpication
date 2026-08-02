package com.SistemSchool.modulo_Financeiro.lazy;

import com.SistemSchool.modulo_Financeiro.dto.FeeDTO;
import com.SistemSchool.modulo_Financeiro.service.FeeService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeeLazyModel extends LazyDataModel<FeeDTO> {

    private static final long serialVersionUID = 1L;

    private final FeeService feeService;

    public FeeLazyModel(FeeService feeService) {
        this.feeService = feeService;
    }

    @Override
    public List<FeeDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {
        int page = first / pageSize;

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        if (sortBy != null && !sortBy.isEmpty()) {
            SortMeta sortMeta = sortBy.values().iterator().next();
            Sort.Direction direction = sortMeta.getOrder().isAscending()
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;
            String field = mapSortField(sortMeta.getField());
            sort = Sort.by(direction, field);
        }

        Map<String, Object> filters = extractFilters(filterBy);

        Page<FeeDTO> result = feeService.findLazy(page, pageSize, sort, filters);

        setRowCount((int) result.getTotalElements());

        return result.getContent();
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        Map<String, Object> filters = extractFilters(filterBy);
        Page<FeeDTO> page = feeService.findLazy(0, 1, Sort.unsorted(), filters);
        return (int) page.getTotalElements();
    }

    @Override
    public FeeDTO getRowData(String rowKey) {
        return feeService.getAllFees()
                .stream()
                .filter(f -> f.getPhFee().toString().equals(rowKey))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getRowKey(FeeDTO feeDTO) {
        return feeDTO.getPhFee() != null
                ? feeDTO.getPhFee().toString()
                : null;
    }

    // ─────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────

    /**
     * Traduz os nomes de campo expostos no FeeDTO (usados no sortBy das colunas
     * PrimeFaces) para o "property path" real da entidade Fee, evitando
     * PropertyReferenceException quando o campo pertence a uma entidade
     * relacionada (ex.: schoolClassName -> schoolClass.name).
     *
     * IMPORTANTE: ajuste "schoolClass.name" conforme o nome real do atributo
     * em SchoolClass (não incluído nos arquivos analisados).
     */
    private String mapSortField(String field) {
        if (field == null) {
            return "createdAt";
        }
        return switch (field) {
            case "schoolClassName" -> "schoolClass.name";
            case "phFee", "feeCode", "description", "schoolYear",
                 "amount", "startDate", "endDate", "status",
                 "createdAt", "updatedAt" -> field;
            default -> "createdAt";
        };
    }

    private Map<String, Object> extractFilters(Map<String, FilterMeta> filterBy) {
        Map<String, Object> filters = new HashMap<>();
        if (filterBy != null) {
            for (FilterMeta meta : filterBy.values()) {
                Object value = meta.getFilterValue();
                if (value != null && !value.toString().isBlank()) {
                    filters.put(meta.getField(), value);
                }
            }
        }
        return filters;
    }
}