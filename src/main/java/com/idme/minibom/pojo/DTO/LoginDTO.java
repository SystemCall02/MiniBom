package com.idme.minibom.pojo.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDTO {
    private String name;
    private String password;
}
