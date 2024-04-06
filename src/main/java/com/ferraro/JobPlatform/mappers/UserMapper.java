package com.ferraro.JobPlatform.mappers;

import com.ferraro.JobPlatform.dto.UserDTO;
import com.ferraro.JobPlatform.dto.request.SignUpRequest;
import com.ferraro.JobPlatform.model.document.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "Spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    public User requestToUser(SignUpRequest request);

    public UserDTO userToDto(User user);
}
