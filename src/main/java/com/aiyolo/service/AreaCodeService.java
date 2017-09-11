package com.aiyolo.service;

import com.aiyolo.common.FileHelper;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AreaCodeService {

    private static final Log errorLogger = LogFactory.getLog("errorLog");

    public JSONObject getAreaCodeDict() throws Exception {
        String areaCodeJsonString = FileHelper.getResourceContent("classpath:adcode.json");
        JSONObject areaCodeJson = JSONObject.fromObject(areaCodeJsonString);
        return areaCodeJson;
    }

    public Boolean checkAreaCode(String areaCode) {
        Integer areaCodeNumber;
        try {
            areaCodeNumber = Integer.valueOf(areaCode);
        } catch (NumberFormatException e) {
            return false;
        }

        if (areaCodeNumber < 100000 || areaCodeNumber > 999999) {
            return false;
        }

        return true;
    }

    public Boolean checkAreaCode(String areaCode, String[] exceptions) {
        for (String exception : exceptions) {
            if (areaCode.equals(exception)) {
                return true;
            }
        }

        return checkAreaCode(areaCode);
    }

    public Boolean areaCodeMatches(String areaCode, List<String> opAreaCode) {
        for (int i = 0; i < opAreaCode.size(); i++) {
            if (areaCodeMatches(areaCode, opAreaCode.get(i))) {
                return true;
            }
        }

        return false;
    }

    public Boolean areaCodeMatches(String areaCode, String opAreaCode) {
        Integer _opAreaCode;
        try {
            _opAreaCode = Integer.valueOf(opAreaCode);
        } catch (NumberFormatException e) {
            return false;
        }

        // 0为特殊编码，匹配所有区域
        if (_opAreaCode == 0) {
            return true;
        }

        Integer _areaCode;
        try {
            _areaCode = Integer.valueOf(areaCode);
        } catch (NumberFormatException e) {
            return false;
        }

        if (_areaCode < 100000 || _areaCode > 999999 ||
                _opAreaCode < 100000 || _opAreaCode > 999999) {
            return false;
        }

        // 完全匹配，areaCode必须是opAreaCode的子集
        if ((_areaCode / 10000) == (_opAreaCode / 10000)) {
            if (_opAreaCode % 10000 == 0) {
                return true;
            } else if ((_areaCode / 100) == (_opAreaCode / 100)) {
                if (_opAreaCode % 100 == 0) {
                    return true;
                } else if (_areaCode.equals(_opAreaCode)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getAreaCodeSearchPat(List<String> searchAreaCodes) {
        List<Object> areaCodeSearchPat = new ArrayList<Object>();
        for (int i = 0; i < searchAreaCodes.size(); i++) {
            if (checkAreaCode(searchAreaCodes.get(i))) {
                Integer areaCodeNumber = Integer.valueOf(searchAreaCodes.get(i));
                if (areaCodeNumber % 10000 == 0) {
                    areaCodeSearchPat.add("^" + String.valueOf(areaCodeNumber / 10000));
                } else if (areaCodeNumber % 100 == 0) {
                    areaCodeSearchPat.add("^" + String.valueOf(areaCodeNumber / 100));
                } else {
                    areaCodeSearchPat.add(String.valueOf(areaCodeNumber));
                }
            }
        }

        return StringUtils.join(areaCodeSearchPat.toArray(new String[0]), "|");
    }

    public String getAreaCodeSearchPat(Map<String, List<String>> authorities, String areaCode) {
        List<String> searchAreaCodes = new ArrayList<String>();

        if (authorities.get("areas").contains("0") && "0".equals(areaCode)) {
            return ".*";
        } else if (authorities.get("areas").contains("0")) {
            searchAreaCodes.add(areaCode);
        } else if ("0".equals(areaCode)) {
            searchAreaCodes = authorities.get("areas");
        } else {
            List<String> areaAuthorities = authorities.get("areas");

            // 选择的区域比权限区域大，提取出有权限的小区域
            for (int i = 0; i < areaAuthorities.size(); i++) { // 遍历权限区域
                if (areaCodeMatches(areaAuthorities.get(i), areaCode)) {
                    searchAreaCodes.add(areaAuthorities.get(i));
                }
            }
            // 选择的区域比权限区域小，直接用选择的区域
            for (int i = 0; i < areaAuthorities.size(); i++) { // 遍历权限区域
                if (areaCodeMatches(areaCode, areaAuthorities.get(i)) && !searchAreaCodes.contains(areaCode)) {
                    searchAreaCodes.add(areaCode);
                    break;
                }
            }
        }

        return getAreaCodeSearchPat(searchAreaCodes);
    }

    public String[] getAreaNameArray(String areaCode) {
        if (!checkAreaCode(areaCode)) {
            return null;
        }

        try {
            JSONObject areaCodeDict = getAreaCodeDict();

            Integer areaCodeNumber = Integer.valueOf(areaCode);
            String provinceCode = String.valueOf(areaCodeNumber / 10000) + "0000";
            String provinceName = (String) areaCodeDict.getJSONObject("100000").get(provinceCode);

            String cityName = "";
            String districtName = "";
            if (areaCodeNumber % 10000 != 0) {
                String cityCode = String.valueOf(areaCodeNumber / 100) + "00";
                cityName = (String) areaCodeDict.getJSONObject(provinceCode).get(cityCode);

                if (areaCodeNumber % 100 != 0) {
                    String districtCode = areaCode;
                    districtName = (String) areaCodeDict.getJSONObject(cityCode).get(districtCode);
                }
            }

            String[] areaName = new String[3];
            areaName[0] = provinceName;
            areaName[1] = cityName;
            areaName[2] = districtName;

            return areaName;
        } catch (Exception e) {
            errorLogger.error("getAreaName异常！", e);
        }

        return null;
    }

    public String getAreaName(String areaCode) {
        String[] areaNameArray = getAreaNameArray(areaCode);
        return areaNameArray != null ? StringUtils.join(areaNameArray) : null;
    }

}
