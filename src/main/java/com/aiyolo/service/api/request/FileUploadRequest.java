package com.aiyolo.service.api.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequest extends Request {

    private String action = "upload";

    @Size(min=1)
    private String cid;

    @Size(min=1)
    private String session;

    @NotNull
    private MultipartFile file;

    public FileUploadRequest() {
    }

    @Override
    public String toString() {
        return "FileUploadRequest{" +
                "action='" + action + '\'' +
                ", cid='" + cid + '\'' +
                ", session='" + session + '\'' +
                ", file={" +
                    "name='" + file.getOriginalFilename() + '\'' +
                    ", type='" + file.getContentType() + '\'' +
                    ", size=" + file.getSize() +
                "}" +
                '}';
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String getCid() {
        return cid;
    }

    @Override
    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public String getSession() {
        return session;
    }

    @Override
    public void setSession(String session) {
        this.session = session;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
