package com.SistemSchool.modulo_secrtaria.lazy;

import com.SistemSchool.modulo_secrtaria.dto.DocumentDTO;
import com.SistemSchool.modulo_secrtaria.service.DocumentService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentLazyModel extends LazyDataModel<DocumentDTO> {

    private static final long serialVersionUID = 1L;

    private final DocumentService documentService;

    public DocumentLazyModel(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Override
    public List<DocumentDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy,
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

        Page<DocumentDTO> result = documentService.findLazy(page, pageSize, sort, null);

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
        Page<DocumentDTO> page = documentService.findLazy(0, 1, Sort.unsorted(), filters);
        return (int) page.getTotalElements();
    }

    @Override
    public DocumentDTO getRowData(String rowKey) {
        return documentService.getAllDocuments()
                .stream()
                .filter(d -> d.getPhDocument().toString().equals(rowKey))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getRowKey(DocumentDTO documentDTO) {
        return documentDTO.getPhDocument() != null
                ? documentDTO.getPhDocument().toString()
                : null;
    }
}