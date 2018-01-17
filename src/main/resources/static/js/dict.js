var dictLoader = {};
var sc = document.getElementsByTagName('script');
var sr = sc[sc.length-1].src.split('?');
if (typeof(sr[1]) !== 'undefined' && sr[1] != '') {
    var dict_names = sr[1].split(',');
    for (i in dict_names) {
        document.write('<script src="/dict/' + dict_names[i] + '"></script>');
        dictLoader[dict_names[i]] = true;
    }
}

var dict = function(data, name) {
    data = data.toString();
    if (typeof(dictLoader[name]) !== 'undefined') {
        var values = eval('dict_' + name);
        return typeof(values[data]) !== 'undefined' ? values[data] : data;
    } else {
        return data;
    }
}

var areaCodeDict = {
    init: function() {
        var dict = {};

        if (typeof(dictLoader['area_code']) !== 'undefined') {
            dict.areaCodes = eval('dict_area_code');
        } else {
            return dict;
        }

        dict.getAreaName = function(areaCode) {
            if (areaCode.length < 6) {
                return ['', '', ''];
            }

            var _p = areaCode.substring(0, 2);
            var _c = areaCode.substring(2, 4);
            var _d = areaCode.substring(4);

            var province = typeof(dict.areaCodes['100000'][_p + '0000']) !== 'undefined' ? dict.areaCodes['100000'][_p + '0000'] : '';
            var city = '';
            var district = '';

            if (_c != '00') {
                var parent = _p + '0000';
                city = typeof(dict.areaCodes[parent][_p + _c + '00']) !== 'undefined' ? dict.areaCodes[parent][_p + _c + '00'] : '';

                if (_d != '00') {
                    var parent = _p + _c + '00';
                    district = typeof(dict.areaCodes[parent][areaCode]) !== 'undefined' ? dict.areaCodes[parent][areaCode] : '';
                }
            }

            return [province, city, district];
        };
        return dict;
    }
};
