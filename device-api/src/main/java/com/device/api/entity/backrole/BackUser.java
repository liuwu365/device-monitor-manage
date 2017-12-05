package com.device.api.entity.backrole;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class BackUser implements Serializable {
    private static final long serialVersionUID = -4521379310045731958L;
    private Long id;

    private String userName;

    private String accountName;

    private String password;

    private String credentialsSalt;

    private String email;

    private String description;

    private Boolean locked;

    private Date createTime;

    private Long roleId;

    private String roleName;

    private String sessionId;

    private List<BackUserRoleKey> roleList;

    private List<BackUserResKey> resKeyList;

}