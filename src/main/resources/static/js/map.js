function initMap() {
    createMap();
    setMapEvent();
    addMapControl();
}
function createMap() {
    map = new BMap.Map("realMapContainer", {minZoom: 5});
    //map.centerAndZoom(new BMap.Point(118.059014, 36.812474), 13);
    map.centerAndZoom("西安", 5);
    map.addEventListener("load", function(e) {
        center = map.getCenter();
        zoom = map.getZoom();
        addMapOverlay();
    });
}
function setMapEvent() {
    map.enableScrollWheelZoom(); // 启用地图滚轮放大缩小
    map.enableKeyboard(); // 启用键盘上下左右键移动地图
}
function addMapControl() {
    //var scaleControl = new BMap.ScaleControl({
    //    anchor : BMAP_ANCHOR_BOTTOM_LEFT
    //});
    //scaleControl.setUnit(BMAP_UNIT_IMPERIAL);
    //map.addControl(scaleControl);
    //var navControl = new BMap.NavigationControl({
    //    anchor : BMAP_ANCHOR_TOP_LEFT,
    //    type : BMAP_NAVIGATION_CONTROL_LARGE
    //});
    //map.addControl(navControl);
    //var overviewControl = new BMap.OverviewMapControl({
    //    anchor : BMAP_ANCHOR_BOTTOM_RIGHT,
    //    isOpen : true
    //});
    //map.addControl(overviewControl);
}
function addMapOverlay(i) {
    if (zoom < 5) return;

    if (typeof i === 'undefined') {
        i = 0;
        $.ajaxSettings.async = false;
        $.getJSON("/home/mapData?center=" + center.lng + "," + center.lat + "&zoom=" + zoom, function (data) {
            markerData = data;

            var _overlayStyle = overlayStyle;
            getOverlayStyle();
            if (_overlayStyle != overlayStyle) {
                getOverlayOpts();
                map.clearOverlays();
                markers = [];
            }
        });
    }

    if (overlayStyle == "DEVICE") {
        addDeviceOverlay();
        return;
    }

    if (typeof markerData[i] === 'undefined') {
        complete = true;
        return;
    }

    var labelContent = "<div style='width: " + overlayOpts.icon.size + "px; text-align: center;'>"
            + markerData[i].position[markerData[i].position.length - 1]
            + "<br />"
            + markerData[i].onlineDev + "台"
            + "</div>";
    if (typeof markers[markerData[i].position.join('')] === 'undefined') {
        new BMap.Geocoder().getPoint(markerData[i].position.join(''), function(point) {
            var marker = new BMap.Marker(
                    point,
                    {
                        icon : new BMap.Icon(
                                "/images/" + overlayOpts.icon.size + ".png",
                                new BMap.Size(overlayOpts.icon.size, overlayOpts.icon.size))
                    });
            var label = new BMap.Label(labelContent);
            label.setOffset(new BMap.Size(0, overlayOpts.icon.size/5));
            label.setStyle(overlayOpts.label.styles);
            marker.setLabel(label);
            map.addOverlay(marker);

            markers[markerData[i].position.join('')] = marker;
            addMapOverlay(++i);
        });
    } else {
        markers[markerData[i].position.join('')].getLabel().setContent(labelContent);
        addMapOverlay(++i);
    }
}
function addDeviceOverlay() {
    for (var i = 0; i < markerData.length; i++) {
        if (markerData[i].addressLocation == "") {
            continue;
        }

        var infoWindowContent = '<dl>' +
                '<dt>地址</dt>' +
                '<dd>' + areaCodeDict.init().getAreaName(markerData[i].areaCode).join('') + markerData[i].address + '</dd>' +
                '<dt>联系人</dt>' +
                '<dd>' + markerData[i].userName + '</dd>' +
                '<dt>联系电话</dt>' +
                '<dd>' + JSON.parse(markerData[i].userPhones).toString() + '</dd></dl>';
        if (typeof markers[markerData[i].addressLocation] === 'undefined') {
            var lo = markerData[i].addressLocation.split(",");
            var marker = new BMap.Marker(
                    new BMap.Point(lo[0], lo[1]),
                    {
                        icon : new BMap.Icon(
                                "/images/" + markerData[i].status + ".png",
                                new BMap.Size(25, 30),
                                { anchor: new BMap.Size(12.5, 30), infoWindowAnchor: new BMap.Size(12.5, 0) })
                    });

            var infoWindow = new BMap.InfoWindow(infoWindowContent, overlayOpts.infoWindow);
            addClickHandler(marker, infoWindow);
            map.addOverlay(marker);
            markers[markerData[i].addressLocation] = marker;
        } else {
            markers[markerData[i].addressLocation].getIcon().setImageUrl("/images/" + markerData[i].status + ".png");
            var infoWindow = new BMap.InfoWindow(infoWindowContent, overlayOpts.infoWindow);
            addClickHandler(markers[markerData[i].addressLocation], infoWindow);
        }
    }
    complete = true;
}
function addClickHandler(target, window) {
    target.addEventListener("click", function() {
        target.openInfoWindow(window);
    });
}
function getOverlayStyle() {
    if (zoom < 8) {
        overlayStyle = "PROVINCE";
    } else if (zoom < 12) {
        overlayStyle = "CITY";
    } else if (zoom < 15) {
        overlayStyle = "DISTRICT";
    } else {
        overlayStyle = "DEVICE";
    }
}
function getOverlayOpts() {
    switch (overlayStyle) {
        case "PROVINCE":
            overlayOpts.icon.size = 60;
            overlayOpts.label.styles = { color: "rgb(98,94,80)", fontSize : "12px" };
            break;
        case "CITY":
            overlayOpts.icon.size = 70;
            overlayOpts.label.styles = { color: "rgb(98,94,80)", fontSize : "14px" };
            break;
        case "DISTRICT":
            overlayOpts.icon.size = 90;
            overlayOpts.label.styles = { color: "rgb(98,94,80)", fontSize : "16px" };
            break;
        case "DEVICE":
            overlayOpts.infoWindow = { width: 400, enableMessage: false };
            break;
    }
}
function refresh() {
    if ($("#realMapContainer").is(":hidden")) return;
    if (!complete) return;
    complete = false;

    center = map.getCenter();
    zoom = map.getZoom();
    addMapOverlay();
}
var map;
var markers = [];
var overlayStyle = "";
var overlayOpts = {icon: {}, label: {}};
var center;
var zoom; // 5-7国 8-11省 12-14区 15-设备
var complete = false;