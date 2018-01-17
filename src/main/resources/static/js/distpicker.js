/*!
 * Distpicker v2.0.0-beta.1
 * https://github.com/fengyuanchen/distpicker
 *
 * Copyright (c) 2014-2016 Fengyuan Chen
 * Released under the MIT license
 *
 * Date: 2016-10-15T07:21:39.426Z
 */

/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;
/******/
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	var _jquery = __webpack_require__(1);

	var _jquery2 = _interopRequireDefault(_jquery);

	var _distpicker = __webpack_require__(2);

	var _distpicker2 = _interopRequireDefault(_distpicker);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var NAMESPACE = 'distpicker';
	var OtherDistpicker = _jquery2.default.fn.distpicker;

	_jquery2.default.fn.distpicker = function jQueryDistpicker(option) {
	  for (var _len = arguments.length, args = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
	    args[_key - 1] = arguments[_key];
	  }

	  var result = void 0;

	  this.each(function each() {
	    var $this = (0, _jquery2.default)(this);
	    var data = $this.data(NAMESPACE);

	    if (!data) {
	      if (/destroy/.test(option)) {
	        return;
	      }

	      var options = _jquery2.default.extend({}, $this.data(), _jquery2.default.isPlainObject(option) && option);
	      $this.data(NAMESPACE, data = new _distpicker2.default(this, options));
	    }

	    if (typeof option === 'string') {
	      var fn = data[option];

	      if (_jquery2.default.isFunction(fn)) {
	        result = fn.apply(data, args);
	      }
	    }
	  });

	  return typeof result !== 'undefined' ? result : this;
	};

	_jquery2.default.fn.distpicker.Constructor = _distpicker2.default;
	_jquery2.default.fn.distpicker.setDefaults = _distpicker2.default.setDefaults;

	_jquery2.default.fn.distpicker.noConflict = function noConflict() {
	  _jquery2.default.fn.distpicker = OtherDistpicker;
	  return this;
	};

	(0, _jquery2.default)(function () {
	  (0, _jquery2.default)('[data-toggle="distpicker"]').distpicker();
	});

/***/ },
/* 1 */
/***/ function(module, exports) {

	module.exports = window.jQuery;

/***/ },
/* 2 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

	var _jquery = __webpack_require__(1);

	var _jquery2 = _interopRequireDefault(_jquery);

	var _defaults = __webpack_require__(3);

	var _defaults2 = _interopRequireDefault(_defaults);

	var _districts = __webpack_require__(4);

	var _districts2 = _interopRequireDefault(_districts);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

	var NAMESPACE = 'distpicker';
	var EVENT_CHANGE = 'change.' + NAMESPACE;
	var DEFAULT_CODE = 100000;
	var PROVINCE = 'province';
	var CITY = 'city';
	var DISTRICT = 'district';

	var Distpicker = function () {
	  function Distpicker(element, options) {
	    _classCallCheck(this, Distpicker);

	    var self = this;

	    self.$element = (0, _jquery2.default)(element);
	    self.options = _jquery2.default.extend({}, _defaults2.default, _jquery2.default.isPlainObject(options) && options);
	    self.placeholders = _jquery2.default.extend({}, _defaults2.default);
	    self.ready = false;
	    self.init();
	  }

	  _createClass(Distpicker, [{
	    key: 'init',
	    value: function init() {
	      var self = this;
	      var options = self.options;
	      var $selects = self.$element.find('select');
	      var length = $selects.length;
	      var data = {};

	      $selects.each(function (i, select) {
	        return _jquery2.default.extend(data, (0, _jquery2.default)(select).data());
	      });

	      _jquery2.default.each([PROVINCE, CITY, DISTRICT], function (i, type) {
	        if (data[type]) {
	          options[type] = data[type];
	          self['$' + type] = $selects.filter('[data-' + type + ']');
	        } else {
	          self['$' + type] = length > i ? $selects.eq(i) : null;
	        }
	      });

	      self.bind();

	      // Reset all the selects (after event binding)
	      self.reset();
	      self.ready = true;
	    }
	  }, {
	    key: 'bind',
	    value: function bind() {
	      var self = this;

	      if (self.$province) {
	        self.$province.on(EVENT_CHANGE, self.onChangeProvince = _jquery2.default.proxy(function () {
	          self.output(CITY);
	          self.output(DISTRICT);
	        }, self));
	      }

	      if (self.$city) {
	        self.$city.on(EVENT_CHANGE, self.onChangeCity = _jquery2.default.proxy(function () {
	          return self.output(DISTRICT);
	        }, self));
	      }
	    }
	  }, {
	    key: 'unbind',
	    value: function unbind() {
	      var self = this;

	      if (self.$province) {
	        self.$province.off(EVENT_CHANGE, self.onChangeProvince);
	      }

	      if (self.$city) {
	        self.$city.off(EVENT_CHANGE, self.onChangeCity);
	      }
	    }
	  }, {
	    key: 'output',
	    value: function output(type) {
	      var self = this;
	      var options = self.options;
	      var placeholders = self.placeholders;
	      var $select = self['$' + type];

	      if (!$select || !$select.length) {
	        return;
	      }

	      var code = void 0;

	      switch (type) {
	        case PROVINCE:
	          code = DEFAULT_CODE;
	          break;

	        case CITY:
	          code = self.$province && (self.$province.find(':selected').data('code') || '');
	          break;

	        case DISTRICT:
	          code = self.$city && (self.$city.find(':selected').data('code') || '');
	          break;
	      }

	      var districts = self.getDistricts(code);
	      var value = options[type];
	      var data = [];
	      var matched = false;

	      if (_jquery2.default.isPlainObject(districts)) {
	        _jquery2.default.each(districts, function (i, name) {
	          var selected = name === value;

	          if (options.valueType === 'code') {
	            selected = i === String(value);
	          }

	          if (selected) {
	            matched = true;
	          }

	          data.push({
	            code: i,
	            name: name,
	            selected: selected
	          });
	        });
	      }

	      if (!matched) {
	        var autoselect = options.autoselect || options.autoSelect;

	        if (data.length && (type === PROVINCE && autoselect > 0 || type === CITY && autoselect > 1 || type === DISTRICT && autoselect > 2)) {
	          data[0].selected = true;
	        }

	        // Save the unmatched value as a placeholder at the first output
	        if (!self.ready && value) {
	          placeholders[type] = value;
	        }
	      }

	      // Add placeholder option
	      if (options.placeholder) {
	        data.unshift({
	          code: '',
	          name: placeholders[type],
	          selected: false
	        });
	      }

	      if (data.length) {
	        $select.html(self.getList(data));
	      } else {
	        $select.empty();
	      }
	    }
	  }, {
	    key: 'getList',
	    value: function getList(data) {
	      var options = this.options;
	      var list = [];

	      _jquery2.default.each(data, function (i, n) {
	        // Check permission
	        var hasPermission = true;
	        if (typeof checkPermission !== 'undefined' && !checkPermission(n.code)) {
	          hasPermission = false;
	        }

	        if (hasPermission) {
	          var attrs = ['data-code="' + n.code + '"', 'data-text="' + n.name + '"', 'value="' + (options.valueType === 'name' && n.code ? n.name : n.code) + '"'];

	          if (n.selected) {
	            attrs.push('selected');
	          }

	          list.push('<option ' + attrs.join(' ') + '>' + n.name + '</option>');
	        }
	      });

	      return list.join('');
	    }

	    // eslint-disable-next-line class-methods-use-this

	  }, {
	    key: 'getDistricts',
	    value: function getDistricts() {
	      var code = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : DEFAULT_CODE;

	      return _districts2.default[code] || null;
	    }
	  }, {
	    key: 'reset',
	    value: function reset(deep) {
	      var self = this;

	      if (!deep) {
	        self.output(PROVINCE);
	        self.output(CITY);
	        self.output(DISTRICT);
	      } else if (self.$province) {
	        self.$province.find(':first').prop('selected', true).trigger(EVENT_CHANGE);
	      }
	    }
	  }, {
	    key: 'destroy',
	    value: function destroy() {
	      var self = this;

	      self.unbind();
	      self.$element.removeData(NAMESPACE);
	    }
	  }], [{
	    key: 'setDefaults',
	    value: function setDefaults(options) {
	      _jquery2.default.extend(_defaults2.default, _jquery2.default.isPlainObject(options) && options);
	    }
	  }]);

	  return Distpicker;
	}();

	exports.default = Distpicker;

/***/ },
/* 3 */
/***/ function(module, exports) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	exports.default = {
	  // Selects the districts automatically.
	  // 0 -> Disable autoselect
	  // 1 -> Autoselect province only
	  // 2 -> Autoselect province and city only
	  // 3 -> Autoselect all (province, city and district)
	  autoselect: 0,

	  // Show placeholder.
	  placeholder: true,

	  // Select value. Options: 'name' and 'code'
	  valueType: 'name',

	  // Defines the initial value of province.
	  province: '省　　',

	  // Defines the initial value of city.
	  city: '市　　',

	  // Defines the initial value of district.
	  district: '区/县　'
	};

/***/ },
/* 4 */
/***/ function(module, exports) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	$.ajax({
		url: "/dict/area_code",
		async: false
	})
	.done(function(data) {
		eval(data);
	});
	exports.default = typeof(dict_area_code) !== 'undefined' ? dict_area_code : {};

/***/ }
/******/ ]);
//# sourceMappingURL=distpicker.js.map