package com.aiyolo.controller;

import com.aiyolo.AuthenticateError;
import com.aiyolo.common.GZipHelper;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.common.StringHelper;
import com.aiyolo.service.api.ApiService;
import com.aiyolo.service.api.BaseService;
import com.aiyolo.service.api.request.FileUploadRequest;
import com.aiyolo.service.api.request.Request;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.ResponseObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Log apiLogger = LogFactory.getLog("apiLog");
    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired BaseService baseService;

    @Autowired ObjectMapper objectMapper;

    private String parseRequestContent(HttpServletRequest request) {
        String body = "";
        try {
            byte[] bodyBytes = new byte[request.getContentLength()];
            request.getInputStream().read(bodyBytes);

            if ("gzip".equals(request.getHeader("Content-Encoding"))) { // 解压缩
                body = GZipHelper.decompress(bodyBytes);
            } else {
                body = new String(bodyBytes, "UTF-8");
            }
        } catch (Exception e) {
            errorLogger.error("ApiController异常！Parse request content failed, Request:" + request.toString(), e);
        }
        return body;
    }

    @PostMapping("")
    @SuppressWarnings("unchecked")
    public @ResponseBody <S extends ApiService, Req extends RequestObject, Res extends ResponseObject> Res api(HttpServletRequest request) {
        String body = parseRequestContent(request);
        if (StringUtils.isEmpty(body)) {
            return (Res) baseService.responseRequestParameterError();
        }

        String action = "";
        Res response = null;
        try {
            Req requestObject = (Req) objectMapper.readValue(body, Request.class);

            JSONObject requestJSONObject = JSONObject.fromObject(requestObject);
            if (requestJSONObject.containsValue("")) {
                response = (Res) baseService.responseRequestParameterError(action);
                return response;
            }

            action = requestObject.getAction();
            if (SpringUtil.getApplicationContext().containsBean(StringHelper.underline2Camel(action) + "Request")) {
                Req requestBean = (Req) SpringUtil.getBean(StringHelper.underline2Camel(action) + "Request");
                requestObject = (Req) objectMapper.readValue(body, requestBean.getClass());
            }

            String serviceName = StringHelper.underline2Camel(action) + "Service";
            S service = (S) SpringUtil.getBean(serviceName);

            response = (Res) service.doExecute(requestObject);
            return response;
        } catch (Exception e) {
            errorLogger.error("ApiController异常！RequestBody:" + body, e);
            if (e instanceof JsonParseException ||
                    e instanceof JsonMappingException ||
                    e instanceof NoSuchBeanDefinitionException) {
                response = (Res) baseService.responseRequestParameterError();
                return response;
            } else if (e instanceof AuthenticateError) {
                response = (Res) baseService.responseAuthenticateError(action);
                return response;
            }
            response = (Res) baseService.responseInternalServerError(action);
            return response;
        } finally {
            apiLogger.info("Request: " + body.replace("\n", "") + ", Response: " + JSONObject.fromObject(response).toString());
        }
    }

    @PostMapping("/upload/{type:.+}")
    @SuppressWarnings("unchecked")
    public @ResponseBody <S extends ApiService, Res extends ResponseObject> Res upload(
            @PathVariable String type,
            @Valid FileUploadRequest fileUploadRequest,
            BindingResult bindingResult) {
        Res response = null;
        try {
            if (bindingResult.hasErrors()) {
                response = (Res) baseService.responseRequestParameterError(fileUploadRequest.getAction());
                return response;
            }

            String serviceName = "upload" + StringHelper.underline2Camel(type, true) + "Service";
            S service = (S) SpringUtil.getBean(serviceName);

            response = (Res) service.doExecute(fileUploadRequest);
            return response;
        } catch (Exception e) {
            errorLogger.error("ApiController异常！RequestBody(FileUpload:" + type + "):" + fileUploadRequest.toString(), e);
            if (e instanceof AuthenticateError) {
                response = (Res) baseService.responseAuthenticateError(fileUploadRequest.getAction());
                return response;
            }
            response = (Res) baseService.responseInternalServerError(fileUploadRequest.getAction());
            return response;
        } finally {
            apiLogger.info("Request(FileUpload:" + type + "): " + fileUploadRequest.toString() + ", Response: " + JSONObject.fromObject(response).toString());
        }
    }

}
