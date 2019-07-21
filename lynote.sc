+ SimpleNumber {
	lynote {
		arg dur, sign=\sharp;

		var fsyms = ['c','des','d','ees','e','f','ges','g','aes','a','bes','b'];
		var ssyms = ['c','cis','d','dis','e','f','fis','g','gis','a','ais','b'];

		var pc = this.round(1.0) % 12;

		var base = if (fsyms == \flat, {
			fsyms[pc]
		},{
			ssyms[pc]
		});

		var oct = (this/12).floor-4;


		var mod = "";

		if (oct > 0, {
			mod = "".catArgs(*oct.asInt.collect("'"));
		});

		if (oct < 0, {
			mod = "".catArgs(*oct.abs.asInt.collect(","));
		});

		if (dur.notNil, {
			^(base++mod++dur);
		},{
			^(base++mod);
		});

	}

}