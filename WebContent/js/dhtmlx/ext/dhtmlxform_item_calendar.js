//dhtmlxForm v.3.0 beta build 110215

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
dhtmlXForm.prototype.items.calendar = {
	
	// events not added to body yet
	ev: false,
	
	// last clicked input id to prevent automatical hiding
	inp: null,
	
	// calendar instances
	calendar: {},
	
	// calendar containers
	cz: {},
	
	// formats
	f: {},
	
	render: function(item, data) {
		
		var t = this;
		
		item._type = "calendar";
		item._enabled = true;
		
		this.doAddLabel(item, data);
		this.doAddInput(item, data, "INPUT", "TEXT", true, true, "dhxlist_txt_textarea");
		
		this.f[item._idd] = (data.dateFormat||"%d-%m-%Y");
		
		item._value = (data.value && data.value instanceof Date ? data.value : "");
		item.childNodes[1].childNodes[0].value = this.getFValue(item, item._value);
		
		this.cz[item._idd] = document.createElement("DIV");
		this.cz[item._idd].style.position = "absolute";
		this.cz[item._idd].style.top = "0px";
		document.body.insertBefore(this.cz[item._idd], document.body.firstChild);
		
		this.calendar[item._idd] = new dhtmlXCalendarObject(this.cz[item._idd], true, data.options||{});
		this.calendar[item._idd].setSkin(data.skin||"dhx_skyblue");
		this.calendar[item._idd].hide();
		
		this.calendar[item._idd]._itemIdd = item._idd;
		this.calendar[item._idd].attachEvent("onClick", function(d) {
			if (this.time) {
				var d2 = this.getTime();
				d.setHours(d2.getHours());
				d.setMinutes(d2.getMinutes());
				d.setSeconds(d2.getSeconds());
			}
			if (item._value != d) {
				// call some events
				if (item.checkEvent("onBeforeChange")) {
					if (item.callEvent("onBeforeChange",[item._idd, item._value, d]) !== true) {
						// do not allow set new date
						this.hide();
						return;
					}
				}
				// accepted
				item._value = d;
				t.setValue(item, d);
				item.callEvent("onChange", [this._itemIdd, item._value]);
			}
			this.hide();
		});
		
		item.childNodes[1].childNodes[0]._idd = item._idd;
		item.childNodes[1].childNodes[0].onclick = function(){
			if (t.calendar[this._idd].isVisible()) {
				t.calendar[this._idd].hide();
				t.inp = null;
			} else {
				t.calendar[this._idd].show();
				t.calendar[this._idd].setPosition(getAbsoluteTop(this)+this.offsetHeight-1, getAbsoluteLeft(this));
				t.calendar[this._idd].setDate(item._value instanceof Date?item._value:new Date());
				if (t.calendar[this._idd].time) t.calendar[this._idd].setTime(item._value instanceof Date?item._value:new Date());
				t.inp = this._idd;
			}
		}
		item.childNodes[1].childNodes[0].onkeypress = function(e) {
			e = e||event;
			if (e.keyCode == 27 && t.calendar[this._idd].isVisible()) t.calendar[this._idd].hide();
		}
	
		if (!this.ev) {
			if (_isIE) document.body.attachEvent("onclick",this.clickEvent); else window.addEventListener("click",this.clickEvent,false);
			this.ev = true;
		}
		
		return this;
		
	},
	
	clickEvent: function() {
		dhtmlXForm.prototype.items.calendar.hideAllCalendars();
	},
	
	hideAllCalendars: function() {
		for (var a in this.calendar) if (a != this.inp) this.calendar[a].hide();
		this.inp = null;
	},
	
	getCalendar: function(item) {
		return this.calendar[item._idd];
	},
	
	getFValue: function(item, val) {
		
		if (val instanceof Date) {
			
			var z = function(t) {
				return (String(t).length==1?"0"+String(t):t);
			}
			var k = function(t) {
				switch(t) {
					case "%Y": return val.getFullYear();
					case "%m": return z(val.getMonth()+1);
					case "%n": return date.getMonth()+1;
					case "%d": return z(val.getDate());
					case "%j": return val.getDate();
					case "%y": return z(val.getYear()%100);
					case "%D": return ({0:"Su",1:"Mo",2:"Tu",3:"We",4:"Th",5:"Fr",6:"Sa"})[val.getDay()];
					case "%l": return ({0:"Sunday",1:"Monday",2:"Tuesday",3:"Wednesday",4:"Thursday",5:"Friday",6:"Saturday"})[val.getDay()];
					case "%M": return ({0:"Jan",1:"Feb",2:"Mar",3:"Apr",4:"May",5:"Jun",6:"Jul",7:"Aug",8:"Sep",9:"Oct",10:"Nov",11:"Dec"})[val.getMonth()];
					case "%F": return ({0:"January",1:"February",2:"March",3:"April",4:"May",5:"June",6:"July",7:"August",8:"September",9:"October",10:"November",11:"December"})[val.getMonth()];
					case "%H": return z(val.getHours());
					case "%h": return z((val.getHours()+11)%12+1);
					case "%i": return z(val.getMinutes());
					case "%s": return z(val.getSeconds());
					case "%a": return (val.getHours()>11?"pm":"am");
					case "%A": return (val.getHours()>11?"PM":"AM");
					default: return t;
				}
			}
			var t = String(this.f[item._idd]).replace(/%[a-zA-Z]/g, k);
		}
		return (t||String(val));
	},
	
	setValue: function(item, value) {
		item._value = (value instanceof Date ? value : "");
		item.childNodes[1].childNodes[0].value = this.getFValue(item, item._value);
	},
	
	getValue: function(item) {
		return item._value;
	},
	
	destruct: function(item) {
		
		// unload calendar instance
		this.calendar[item._idd].unload();
		this.calendar[item._idd] = null;
		try {delete this.calendar[item._idd];} catch(e){}
		
		this.f[item._idd] = null;
		try {delete this.f[item._idd];} catch(e){}
		
		this.cz[item._idd].parentNode.removeChild(this.cz[item._idd]);
		this.cz[item._idd] = null;
		try {delete this.cz[item._idd];} catch(e){}
		
		// remove body events if no more colopicker instances left
		var k = 0;
		for (var a in this.calendar) k++;
		if (k == 0) {
			if (_isIE) document.body.detachEvent("onclick",this.clickEvent); else window.removeEventListener("click",this.clickEvent,false);
			this.ev = false;
		}
		
		// remove custom events/objects
		item.childNodes[1].childNodes[0]._idd = null;
		item.childNodes[1].childNodes[0].onkeypress = null;
		
		// unload item
		this.d2(item);
		item = null;
	}
	
};

(function(){
	for (var a in {doAddLabel:1,doAddInput:1,doUnloadNestedLists:1,setText:1,getText:1,enable:1,disable:1,setWidth:1,setReadonly:1,isReadonly:1})
		dhtmlXForm.prototype.items.calendar[a] = dhtmlXForm.prototype.items.input[a];
})();

dhtmlXForm.prototype.items.calendar.d2 = dhtmlXForm.prototype.items.input.destruct;

dhtmlXForm.prototype.getCalendar = function(name) {
	return this.doWithItem(name, "getCalendar");
};

dhtmlXCalendarObject.prototype.unload = function() {
	
	this._c = null;
	
	this.planeYear._c = null;
	this.planeYear.onclick = null;
	if (this.planeYear.parentNode) this.planeYear.parentNode.removeChild(this.planeYear);
	this.planeYear = null;
	
	this.planeMonth._c = null;
	this.planeMonth.onclick = null;
	if (this.planeMonth.parentNode) this.planeMonth.parentNode.removeChild(this.planeMonth);
	this.planeMonth = null;
	
	if (this.editorMonth) {
		
		this.editorMonth._c = null;
		
		if (this.editorMonth.con.parentNode) this.editorMonth.con.parentNode.removeChild(this.editorMonth.con);
		this.editorMonth.con = null;
		
		this.editorMonth.show = null;
		this.editorMonth.hide = null;
		this.editorMonth.onBlur = null;
		this.editorMonth.onFocus = null;
		this.editorMonth.onKeyUp = null;
		this.editorMonth.onSelect = null;
		this.editorMonth.valueList = null;
		this.editorMonth.titleList = null;
		
		this.editorMonth.editor._s = null;
		this.editorMonth.editor.onblur = null;
		this.editorMonth.editor.onfocus = null;
		this.editorMonth.editor.onkeyup = null;
		if (this.editorMonth.editor.parentNode) this.editorMonth.editor.parentNode.removeChild(this.editorMonth.editor);
		this.editorMonth.editor = null; 
		
		this.editorMonth.selector._s = null;
		this.editorMonth.selector.onblur = null;
		this.editorMonth.selector.onclick = null;
		this.editorMonth.selector.onfocus = null;
		if (this.editorMonth.selector.parentNode) this.editorMonth.selector.parentNode.removeChild(this.editorMonth.selector);
		this.editorMonth.selector = null;
		
		this.editorMonth.nodeBefore = null;
		this.editorMonth = null;
		
	}
	
	if (this.editorYear) {
		
		this.editorYear._c = null;
		
		if (this.editorYear.con.parentNode) this.editorYear.con.parentNode.removeChild(this.editorYear.con);
		this.editorYear.con = null;
		
		this.editorYear.show = null;
		this.editorYear.hide = null;
		this.editorYear.onBlur = null;
		this.editorYear.onFocus = null;
		this.editorYear.onKeyUp = null;
		this.editorYear.onSelect = null;
		this.editorYear.valueList = null;
		this.editorYear.titleList = null;
		
		this.editorYear.editor._s = null;
		this.editorYear.editor.onblur = null;
		this.editorYear.editor.onfocus = null;
		this.editorYear.editor.onkeyup = null;
		if (this.editorYear.editor.parentNode) this.editorYear.editor.parentNode.removeChild(this.editorYear.editor);
		this.editorYear.editor = null; 
		
		this.editorYear.selector._s = null;
		this.editorYear.selector.onblur = null;
		this.editorYear.selector.onclick = null;
		this.editorYear.selector.onfocus = null;
		if (this.editorYear.selector.parentNode) this.editorYear.selector.parentNode.removeChild(this.editorYear.selector);
		this.editorYear.selector = null;
		
		this.editorYear.nodeBefore = null;
		this.editorYear = null;
		
	}
	
	this.daysPan.onmousemove = null;
	this.daysPan.onmouseout = null;
	for (var q=0; q<this.daysPan.childNodes[0].childNodes.length; q++) {
		for (var w=0; w<this.daysPan.childNodes[0].childNodes[q].childNodes.length; w++) {
			this.daysPan.childNodes[0].childNodes[q].childNodes[w].onclick = null;
			this.daysPan.childNodes[0].childNodes[q].childNodes[w].thisdate = null;
		}
	}
	this.daysPan.parentNode.removeChild(this.daysPan);
	this.daysPan = null;
	
	for (var q=0; q<this.monthPan.childNodes[0].childNodes[0].childNodes.length; q++) {
		this.monthPan.childNodes[0].childNodes[0].childNodes[q].onclick = null;
		this.monthPan.childNodes[0].childNodes[0].childNodes[q].onselectstart = null;
	}
	this.monthPan.parentNode.removeChild(this.monthPan);
	this.monthPan = null;
	
	this.dlabelPan.parentNode.removeChild(this.dlabelPan);
	this.dlabelPan = null;
	
	for (var q=0; q<this.con.length; q++) this.con[q] = null;
	this.con = null;
	
	for (var q=0; q<this.date.length; q++) this.date[q] = null;
	this.date = null;
	
	for (var q=0; q<this.selDate.length; q++) this.selDate[q] = null;
	this.selDate = null;
	
	for (var a in this.daysCells) {
		for (var b in this.daysCells[a]) this.daysCells[a][b] = null;
		this.daysCells[a] = null;
	}
	this.daysCells = null;
	
	for (var a in this.weekCells) {
		if (this.weekCells[a].parentNode) this.weekCells[a].parentNode.removeChild(this.weekCells[a]);
		this.weekCells[a] = null;
	}
	this.weekCells = null;
	
	if (this.tp) {
		
		this.tp._bcNames = null;
		this.tp._tf = null;
		this.tp._time = null;
		
		this.tp.doOnClick = null;
		this.tp.setOnClickHandler = null;
		this.tp.create = null;
		this.tp.reset = null;
		this.tp.show = null;
		this.tp.hide = null;
		this.tp.setPosition = null;
		this.tp.setTime = null;
		this.tp.setFormatedTime = null;
		this.tp.setTimeFormat = null;
		this.tp.getTime = null;
		this.tp.getFormatedTime = null;
		this.tp.hideSeconds = null;
		this.tp._changeTime = null;
		this.tp._setBCValue = null;
		
		for (var a in this.tp._bc) this.tp._bc[a] = null;
		this.tp._bc = null;
		
		this.tp.entBox.onclick = null;
		while (this.tp.entBox.childNodes.length > 0) {
			for (var q=0; q<this.tp.entBox.childNodes[0].childNodes.length; q++) {
				if (this.tp.entBox.childNodes[0].childNodes[q].tagName) {
					var t = String(this.tp.entBox.childNodes[0].childNodes[q].tagName).toLowerCase();
					if (t == "div" || t == "select") {
						this.tp.entBox.childNodes[0].childNodes[q].onblur = null;
						this.tp.entBox.childNodes[0].childNodes[q].onclick = null;
						this.tp.entBox.childNodes[0].childNodes[q].onmousedown = null;
					}                    
				}
			}
			this.tp.entBox.childNodes[0].onclick = null;
			this.tp.entBox.removeChild(this.tp.entBox.childNodes[0]);
		}
		if (this.tp.entBox.parentNode) this.tp.entBox.parentNode.removeChild(this.tp.entBox);
		this.tp.entBox = null;
		
		this.tp = null;
		
	}
	
	this.entBox.parentNode.removeChild(this.entBox);
	this.entBox = null;
	
	this.entObj.onclick = null;
	this.entObj.parentNode.removeChild(this.entObj);
	this.entObj = null;
	
	this.activeCell = null;
	this.activeCon = null;
	this.activeConInd = null;
	this.contId = null;
	this.conInd = null;
	this.curDate = null;
	this.dragging = null;
	this.scriptName = null;
	this.skinName = null;
	this.style = null;
	this.uid = null;
	this.isAutoDraw = null;
	this.minimized = null;
	this.time = null;
	this.useIframe = null;
	this.userPosition = null;
	
	this.options = null;
	this.allYears = null;
	
	this._itemIdd = null;
	
	this.parent = null;
	
	this.detachAllEvents();
	
	var cInd = -1;
	for (var q=0; q<dhtmlxCalendarObjects.length; q++) if (dhtmlxCalendarObjects[q] == this) cInd = q;
	if (cInd >= 0) (dhtmlxCalendarObjects.splice(cInd, 1))[0] = null;
	
	this.attachEvent = null;
	this.callEvent = null;
	this.checkEvent = null;
	this.eventCatcher = null;
	this.detachEvent = null;
	this.detachAllEvents = null;
	this.createStructure = null;
	this.drawHeader = null;
	this.drawMonth = null;
	this.drawDayLabels = null;
	this.drawDays = null;
	this.draw = null;
	this.loadUserLanguage = null;
	this.loadUserLanguageCallback = null;
	this.loadLanguageModule = null;
	this.show = null;
	this.hide = null;
	this.setDateFormat = null;
	this.cutTime = null;
	this.setParent = null;
	this.setDate = null;
	this.addClass = null;
	this.resetClass = null;
	this.resetHotClass = null;
	this.setSkin = null;
	this.getDate = null;
	this.nextMonth = null;
	this.prevMonth = null;
	this.setOnClickHandler = null;
	this.getFormatedDate = null;
	this.setFormatedDate = null;
	this.isWeekend = null;
	this.getDayName = null;
	this.isVisible = null;
	this.doHotKeys = null;
	this.endHotKeys = null;
	this.setHeader = null;
	this.setYearsRange = null;
	this.startDrag = null;
	this.onDrag = null;
	this.stopDrag = null;
	this.minimize = null;
	this.onYearSelect = null;
	this.onMonthSelect = null;
	this.setPosition = null;
	this.close = null;
	this.getPosition = null;
	this.setSensitive = null;
	this.setHolidays = null;
	this.onChangeMonth = null;
	this.setInsensitiveDates = null;
	this.enableTime = null;
	this.setHeaderText = null;
	this.disableIESelectFix = null;
	this.reset = null;
	this.create = null;
	this.setTime = null;
	this.setFormatedTime = null;
	this.setTimeFormat = null;
	this.getTime = null;
	this.getFormatedTime = null;
	this.hideSeconds = null;
	this._setBCValue = null;
	this._changeTime = null;
	this._activeConInd = null;
	
	this.unload = null;
	
}
