package com.aiyolo.service.api.response;

import java.util.List;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.service.api.request.RequestObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserListResponse extends Response {

    private List<AppUserObject> users;

//    public UserListResponse() {
//    }

    public UserListResponse(RequestObject request, List<AppUserObject> users) {
        super(request.getAction(), users.size() == 0 ? ApiResponseStateEnum.ERROR_NONE_DATA : ApiResponseStateEnum.SUCCESS);

        this.users = users;
    }

    @Override
    public String toString() {
        return super.toString() + ", UserListResponse{" +
                "users='" + users.toString() + '\'' +
                '}';
    }

    public List<AppUserObject> getUsers() {
        return users;
    }

    public void setUsers(List<AppUserObject> users) {
        this.users = users;
    }

}
