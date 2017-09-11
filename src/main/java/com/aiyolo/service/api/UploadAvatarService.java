package com.aiyolo.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.aiyolo.controller.FileController;
import com.aiyolo.entity.AppUser;
import com.aiyolo.repository.AppUserRepository;
import com.aiyolo.service.api.request.FileUploadRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.ResponseObject;
import com.aiyolo.service.api.response.UploadAvatarResponse;
import com.aiyolo.service.storage.StorageService;

@Service
public class UploadAvatarService extends BaseService {

    private final String storageDir = "avatar";

    @Autowired AppUserRepository appUserRepository;

    @Autowired StorageService storageService;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        FileUploadRequest fileUploadRequest = (FileUploadRequest) request;
        MultipartFile avatarFile = fileUploadRequest.getFile();

        storageService.setDirectory(storageDir);
        storageService.init();
        String filename = storageService.store(avatarFile);
        String avatar = MvcUriComponentsBuilder
                .fromMethodName(FileController.class, "serveFile", storageDir, filename)
                .build().toString();

        AppUser appUser = appUserRepository.findFirstByUserIdOrderByIdDesc(userId);
        if (appUser != null) {
            appUser.setAvatar(avatar);
        } else {
            appUser = new AppUser(userId, avatar);
        }
        appUserRepository.save(appUser);

        return (Res) new UploadAvatarResponse(request, appUser);
    }

}
