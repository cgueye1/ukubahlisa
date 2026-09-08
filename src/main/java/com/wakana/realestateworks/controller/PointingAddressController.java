package com.wakana.realestateworks.controller;

import com.wakana.realestateworks.dto.PointingAddressRequest;
import com.wakana.realestateworks.model.PointingAddress;
import com.wakana.realestateworks.services.PointingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pointing-addresses")
@RequiredArgsConstructor
public class PointingAddressController {

    private final PointingAddressService pointingAddressService;

    @PostMapping
    public PointingAddress create(@RequestBody PointingAddressRequest pointingAddress) {
        return pointingAddressService.save(pointingAddress);
    }

    @PutMapping("/{id}")
    public PointingAddress update(@PathVariable Long id, @RequestBody PointingAddressRequest pointingAddress) {
        return pointingAddressService.update(id, pointingAddress);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pointingAddressService.delete(id);
    }

    @GetMapping("/{id}")
    public PointingAddress getById(@PathVariable Long id) {
        return pointingAddressService.findById(id);
    }

    @GetMapping("/property/{propertyId}")
    public List<PointingAddress> getPointingAddressesByProperty(@PathVariable Long propertyId) {
        return pointingAddressService.getPointingAddressesByProperty(propertyId);
    }
}
