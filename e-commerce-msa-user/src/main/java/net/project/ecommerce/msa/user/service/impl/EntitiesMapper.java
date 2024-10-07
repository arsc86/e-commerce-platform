package net.project.ecommerce.msa.user.service.impl;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import net.project.ecommerce.dependency.api.dto.user.AddressDTO;
import net.project.ecommerce.dependency.api.dto.user.ProfileDTO;
import net.project.ecommerce.dependency.api.dto.user.request.UserRequestDTO;
import net.project.ecommerce.msa.user.model.InfoAddress;
import net.project.ecommerce.msa.user.model.InfoProfile;
import net.project.ecommerce.msa.user.model.InfoUser;

@Mapper(componentModel = "spring")
public interface EntitiesMapper {
    void updateUserFromDto(UserRequestDTO dto, @MappingTarget InfoUser user);
    void updateProfileFromDto(ProfileDTO dto, @MappingTarget InfoProfile profile); 
    void updateAddressFromDto(AddressDTO dto, @MappingTarget InfoAddress address);
}