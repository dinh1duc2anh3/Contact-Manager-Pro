package koolsoft.server.mapper;

import koolsoft.server.ContactInfo;
import koolsoft.shared.ContactInfoDTO;

public class ContactInfoMapper {

    // Convert từ Entity sang DTO
    public static ContactInfoDTO toDTO(ContactInfo entity) {
        if (entity == null) return null;

        ContactInfoDTO dto = new ContactInfoDTO();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        return dto;
    }

    // Convert từ DTO sang Entity
    public static ContactInfo toEntity(ContactInfoDTO dto) {
        if (dto == null) return null;

        ContactInfo entity = new ContactInfo();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAddress(dto.getAddress());
        return entity;
    }
}