var enableAreaCode = {};
var distpickerInit = function(authorities) {
    $.each(authorities, function(key, value) {
        if (value.authority.substring(0, 4) == "AREA" && value.authority != "AREA_0") {
            var _p = value.authority.substring(5, 7);
            if (!enableAreaCode[_p])
                enableAreaCode[_p] = {};

            var _c = value.authority.substring(7, 9);
            if (_c != '00' && !enableAreaCode[_p][_c])
                enableAreaCode[_p][_c] = {};

            var _d = value.authority.substring(9);
            if (_d != '00')
                enableAreaCode[_p][_c][_d] = {};
        }
    });
}

var checkPermission = function(areaCode) {
    if (!areaCode || jQuery.isEmptyObject(enableAreaCode)) {
        return true;
    }

    var _p = areaCode.substring(0, 2);
    var _c = areaCode.substring(2, 4);
    var _d = areaCode.substring(4);
    if (_d == '00' && _c == '00') { // 省
        if (enableAreaCode[_p])
            return true;
    } else if (_d == '00') { // 市
        if (jQuery.isEmptyObject(enableAreaCode[_p]) || enableAreaCode[_p][_c])
            return true;
    } else { // 区
        if (jQuery.isEmptyObject(enableAreaCode[_p][_c]) || enableAreaCode[_p][_c][_d])
            return true;
    }

    return false;
}
