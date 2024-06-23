package project.mockshop.mapper;

import project.mockshop.dto.CustomerCreationDto;
import project.mockshop.dto.CustomerDto;
import project.mockshop.entity.Customer;

public class CustomerMapper {
    public static Customer toEntity(CustomerCreationDto dto) {
        return Customer.builder()
                .loginId(dto.getLoginId())
                .name(dto.getName())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .point(0)
                .isDeleted(false)
                .build();
    }

    public static Customer toEntity(CustomerDto dto) {
        return Customer.builder()
                .id(dto.getId())
                .loginId(dto.getLoginId())
                .name(dto.getName())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .point(0)
                .isDeleted(false)
                .build();
    }

    public static CustomerDto toDto(Customer entity) {
        return CustomerDto.builder()
                .id(entity.getId())
                .loginId(entity.getLoginId())
                .name(entity.getName())
                .password(entity.getPassword())
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .point(entity.getPoint())
                .isDeleted(entity.isDeleted())
                .build();
    }

    public static CustomerCreationDto toCreationDto(Customer entity) {
        return CustomerCreationDto.builder()
                .loginId(entity.getLoginId())
                .name(entity.getName())
                .password(entity.getPassword())
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .build();
    }
}
