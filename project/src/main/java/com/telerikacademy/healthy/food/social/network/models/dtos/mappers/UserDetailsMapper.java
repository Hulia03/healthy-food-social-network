package com.telerikacademy.healthy.food.social.network.models.dtos.mappers;

import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.dtos.users.UserDetailsDto;
import com.telerikacademy.healthy.food.social.network.models.dtos.users.UserDetailsUpdateDto;
import com.telerikacademy.healthy.food.social.network.models.dtos.users.UserRegisterDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.GenderService;
import com.telerikacademy.healthy.food.social.network.services.contracts.NationalitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.telerikacademy.healthy.food.social.network.models.dtos.mappers.Mapper.getNotNull;

@Component
public class UserDetailsMapper {
    private static final int WIDTH = 500;

    private NationalitiesService nationalitiesService;
    private GenderService genderService;

    private MediaMapper mediaMapper;

    public UserDetailsMapper() {
        //Empty constructor
    }

    @Autowired
    public UserDetailsMapper(NationalitiesService nationalitiesService, GenderService genderService, MediaMapper mediaMapper) {
        this.nationalitiesService = nationalitiesService;
        this.genderService = genderService;
        this.mediaMapper = mediaMapper;
    }

    public UserDetails toUser(UserRegisterDto dto) {
        UserDetails user = new UserDetails();
        user.setEmail(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDescription(dto.getLastName());
        user.setDescription(dto.getDescription());
        user.setAge(Math.max(dto.getAge(), 0));
        user.setNationality(dto.getNationalityId() > 0 ? nationalitiesService.getNationalityById(dto.getNationalityId()) : null);
        user.setGender(dto.getGenderId() > 0 ? genderService.getGenderById(dto.getGenderId()) : null);
        user.setPicture(mediaMapper.toMedia(dto.getPicture(), dto.getVisibilityId(), WIDTH));
        return user;
    }

    public UserDetails mergeUser(UserDetails oldUser, UserDetailsUpdateDto dto) {
        oldUser.setFirstName(getNotNull(oldUser.getFirstName(), dto.getFirstName()));
        oldUser.setLastName(getNotNull(oldUser.getLastName(), dto.getLastName()));
        oldUser.setDescription(dto.getDescription());
        oldUser.setPicture(mediaMapper.mergeMedia(oldUser.getPicture(), dto.getPicture(), dto.getVisibilityId(), WIDTH));
        oldUser.setAge(Math.max(dto.getAge(), 0));
        oldUser.setNationality(dto.getNationalityId() > 0 ? nationalitiesService.getNationalityById(dto.getNationalityId()) : null);
        oldUser.setGender(dto.getGenderId() > 0 ? genderService.getGenderById(dto.getGenderId()) : null);
        return oldUser;
    }

    public UserDetailsDto mergeUser(UserDetails oldUser, boolean isPublic) {
        UserDetailsDto dto = new UserDetailsDto();
        dto.setId(oldUser.getId());
        dto.setEmail(oldUser.getEmail());
        dto.setFirstName(oldUser.getFirstName());
        dto.setLastName(oldUser.getLastName());
        dto.setDescription(oldUser.getDescription());
        dto.setAge(oldUser.getAge());
        dto.setNationality(oldUser.getNationality());
        dto.setGender(oldUser.getGender());

        if (isPublic) {
            dto.setPicture(oldUser.getPicture().getPicture());
        }

        return dto;
    }

    public UserDetailsUpdateDto mergeUser(UserDetails oldUser) {
        UserDetailsUpdateDto dto = new UserDetailsUpdateDto();
        dto.setEmail(oldUser.getEmail());
        dto.setFirstName(oldUser.getFirstName());
        dto.setLastName(oldUser.getLastName());
        dto.setPictureUrl(oldUser.getPicture().getPicture());
        dto.setDescription(oldUser.getDescription());
        dto.setVisibilityId(oldUser.getPicture().getVisibility().getId());
        dto.setAge(oldUser.getAge());

        if (oldUser.getNationality() != null) {
            dto.setNationalityId(oldUser.getNationality().getId());
        }

        if (oldUser.getGender() != null) {
            dto.setGenderId(oldUser.getGender().getId());
        }
        return dto;
    }

}
