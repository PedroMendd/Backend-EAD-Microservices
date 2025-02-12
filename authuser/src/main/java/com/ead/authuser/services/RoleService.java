package com.ead.authuser.services;

import com.ead.authuser.enums.RoleType;
import com.ead.authuser.models.RoleModel;

public interface RoleService {

    RoleModel findByRoleName(RoleType roleType);

}
