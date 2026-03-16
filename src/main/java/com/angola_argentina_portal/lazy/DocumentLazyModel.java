package com.angola_argentina_portal.lazy;

import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.angola_argentina_portal.dto.DocumentTableDTO;
import com.angola_argentina_portal.service.DocumentService;

public class DocumentLazyModel extends LazyDataModel<DocumentTableDTO> {

    private final DocumentService service;
    private final String referenceType;
    private final int referenceId;

    public DocumentLazyModel(
            DocumentService service,
            String referenceType,
            int referenceId) {

        this.service = service;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
    }

    @Override
    public List<DocumentTableDTO> load(
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

        Page<DocumentTableDTO> result = service.findLazy(referenceType, referenceId, page, pageSize, sort);

        this.setRowCount((int) result.getTotalElements());

        return result.getContent();
    }

    @Override
    public String getRowKey(DocumentTableDTO dto) {
        return String.valueOf(dto.getPkDocument());
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }
}
