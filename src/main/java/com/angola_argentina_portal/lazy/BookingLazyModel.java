package com.angola_argentina_portal.lazy;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.angola_argentina_portal.dto.BookingTableDTO;
import com.angola_argentina_portal.service.BookingService;

import java.util.List;
import java.util.Map;

public class BookingLazyModel
        extends LazyDataModel<BookingTableDTO> {

    private final BookingService service;

    public BookingLazyModel(BookingService service) {
        this.service = service;
    }

    @Override
    public List<BookingTableDTO> load(
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

        Page<BookingTableDTO> result = service.findLazy(page, pageSize, sort);

        setRowCount((int) result.getTotalElements());

        return result.getContent();
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        Page<BookingTableDTO> page = service.findLazy(0, 1, Sort.unsorted());
        return (int) page.getTotalElements();
    }

    @Override
    public String getRowKey(BookingTableDTO dto) {
        return String.valueOf(dto.getPkBooking());
    }
}