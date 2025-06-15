package com.hello.server.mapper;

import java.util.List;
import java.util.stream.Collectors;
import com.hello.shared.model.ContactInfo;


public class ContactInfoMapper {

    // Convert từ Entity sang DTO
    public static ContactInfo toDTO(ContactInfo entity) {
        if (entity == null) return null;

        ContactInfo dto = new ContactInfo();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setFullName(entity.getFullName());
        dto.setGender(entity.getGender());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        return dto;
    }
//
//    // Convert từ DTO sang Entity
//    public static ContactInfo toEntity(ContactInfo dto) {
//        if (dto == null) return null;
//
//        ContactInfo entity = new ContactInfo();
//        entity.setFirstName(dto.getFirstName());
//        entity.setLastName(dto.getLastName());
//        entity.setPhoneNumber(dto.getPhoneNumber());
//        entity.setAddress(dto.getAddress());
//        return entity;
//    }
    
    public static List<ContactInfo> toDTOList(List<ContactInfo> contacts){
    	if (contacts.isEmpty()) return null;
    	
    	List<ContactInfo> dtos = contacts.stream().map(ContactInfoMapper::toDTO)
    			.collect(Collectors.toList());
    	return dtos;
    }
}