package com.neo.exp.service.impl;

import com.neo.exp.domain.ContactInfo;
import com.neo.exp.dto.ContactInfoDto;

public class ContactInfoServiceImpl {
    public static ContactInfoDto createFromDto(ContactInfo contactInfo) {
        return new ContactInfoDto(
                contactInfo.getPersonalEmail(),
                contactInfo.getProfessionalEmail(),
                contactInfo.getPersonalPhone(),
                contactInfo.getBusinessPhone()
        );
    }
    public static ContactInfoDto mapToDto(ContactInfo contactInfo) {
        return new ContactInfoDto(
                contactInfo.getPersonalEmail(),
                contactInfo.getProfessionalEmail(),
                contactInfo.getPersonalPhone(),
                contactInfo.getBusinessPhone()
        );
    }
}
