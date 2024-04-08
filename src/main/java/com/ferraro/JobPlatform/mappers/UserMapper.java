package com.ferraro.JobPlatform.mappers;

import com.ferraro.JobPlatform.dto.UserDTO;
import com.ferraro.JobPlatform.dto.request.UserSignUpRequest;
import com.ferraro.JobPlatform.model.document.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "Spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User requestToUser(UserSignUpRequest request);

    UserDTO userToDto(User user);
}
