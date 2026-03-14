package com.angola_argentina_portal.lazy;

import org.primefaces.model.LazyDataModel;

import com.angola_argentina_portal.dto.HotelTableDTO;
import com.angola_argentina_portal.model.Hotel;
import com.angola_argentina_portal.service.HotelService;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public class HotelLazyModel extends LazyDataModel<HotelTableDTO> {

    private final HotelService service;

    public HotelLazyModel(HotelService service) {
        this.service = service;
    }

    @Override
    public List<HotelTableDTO> load(int first,
            int pageSize,
            Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {

        List<Hotel> hotels = service.findAll();

        List<HotelTableDTO> dtos = hotels.stream()
                .map(h -> new HotelTableDTO(
                        h.getId(),
                        h.getName(),
                        h.getCity(),
                        h.getStars(),
                        h.getPhone(),
                        h.getImageUrl()))
                .toList();

        int dataSize = dtos.size();

        this.setRowCount(dataSize);

        return dtos.stream()
                .skip(first)
                .limit(pageSize)
                .toList();
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public String getRowKey(HotelTableDTO entity) {
        return String.valueOf(entity.getId());
    }

    @Override
    public HotelTableDTO getRowData(String rowKey) {
        int id = Integer.parseInt(rowKey);

        if (this.getWrappedData() != null) {
            for (HotelTableDTO entity : this.getWrappedData()) {
                if (entity.getId() == id) {
                    return entity;
                }
            }
        }
        return null;
    }
}