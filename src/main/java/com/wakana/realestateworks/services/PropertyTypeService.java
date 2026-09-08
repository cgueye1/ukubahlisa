package com.wakana.realestateworks.services;

import java.util.List;

import com.wakana.realestateworks.model.PropertyType;

public interface PropertyTypeService {
    PropertyType create(PropertyType propertyType);
    PropertyType update(Long id, PropertyType propertyType);
    void delete(Long id);
    List<PropertyType> getAll();
    PropertyType getById(Long id);
}
