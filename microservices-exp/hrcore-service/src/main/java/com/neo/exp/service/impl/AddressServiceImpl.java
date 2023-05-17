package com.neo.exp.service.impl;

import com.neo.exp.domain.Address;
import com.neo.exp.dto.AddressDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressServiceImpl {

    public static AddressDto createFromDto(Address address) {
        return new AddressDto(
                address.getAddress1(),
                address.getCity(),
                address.getPostalCode(),
                address.getDepartmentState(),
                address.getCountry()
        );
    }

    public static AddressDto mapToDto(Address address) {
        return new AddressDto(
                address.getAddress1(),
                address.getCity(),
                address.getPostalCode(),
                address.getDepartmentState(),
                address.getCountry()
        );
    }
}

