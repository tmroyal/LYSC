// TODO: bring in lynote

LDecoration {
	classvar symbol;

	*render {
		arg prefix, symbol;
		^prefix++symbol;
	}

	*event {
		arg incoming = ();
		^incoming;
	}
}


LDynamic : LDecoration {
	classvar prefix = "\\";

	*render {
		arg symbol;
		^super.render(prefix, symbol);
	}

	*event {
		arg incoming=(), amp;
		incoming.amp = amp;
		^incoming;
	}
}


LDfff  : LDynamic {
	*render {
		^super.render("fff");
	}

	*event {
		arg incoming=();

		^super.event(incoming, 1);

	}
}

LDff : LDynamic {
	*render {
		^super.render("ff");
	}

	*event {
		arg incoming=();

		^super.event(incoming, 7/8.0);
	}
}

LDf : LDynamic {
	*render {
		^super.render("f");
	}

	*event {
		arg incoming=();

		^super.event(incoming, 3/4.0);
	}
}

LDmf  : LDynamic {
	*render {
		^super.render("mf");
	}

	*event {
		arg incoming=();

		^super.event(incoming, 5/8.0);

	}
}

// TODO
// LTie

LDmp  : LDynamic {
	*render {
		^super.render("mp");
	}

	*event {
		arg incoming=();

		^super.event(incoming, 1/2.0);

	}
}

LDp  : LDynamic {
	*render {
		^super.render("p");
	}

	*event {
		arg incoming=();

		^super.event(incoming, 3/8.0);

	}
}

LDpp  : LDynamic {
	*render {
		^super.render("pp");
	}
	*event {
		arg incoming=();

		^super.event(incoming, 1/4.0);

	}
}

LDppp  : LDynamic {
	*render {
		^super.render("ppp");
	}

	*event {
		arg incoming=();

		^super.event(incoming, 1/8.0);
	}
}


LArticulation : LDecoration {
	classvar prefix = "-";

	*render {
		arg symbol;
		^super.render(prefix, symbol);
	}

	*decorateIncoming {
		arg event, key, range;

		var value = range[0].rrand(range[1]);

		if (event.includesKey(key), {
			event[key] = (event[key] + value)/2.0;
		},{
			event[key] = value;
		})

		^event;
	}
}

LStaccato : LArticulation {
	*render {
		^super.render(".");
	}

	*event {
		arg incoming=();

		^super.decorateIncoming(incoming, \legato, [0.4,0.5]);
	}
}

LStaccatissimo : LArticulation {
	*render {
		^super.render("!");
	}

	*event {
		arg incoming=();

		^super.decorateIncoming(incoming, \legato, [0.15,0.2]);
	}
}

LPortato : LArticulation {
	*render {
		^super.render("_");
	}

	*event {
		arg incoming=();

		^super.decorateIncoming(incoming, \legato, [0.7,0.8]);
	}
}

// TODO: with these articulations
// we could redo the chan
// by specifically allowing a chan to be
// passed in, which would change the chan just for that event
// NOTE: THIS IS FOR NEXT PIECE

// BETTER: CUSTOM ARTICULATION, WORKS WITH EVERY SAMPLE LIBRARY

LTenuto : LArticulation {
	*render {
		^super.render("-");
	}

	*event {
		arg incoming=();

		^super.decorateIncoming(incoming, \legato, [0.9,0.95]);
	}
}


LAccent : LArticulation {
	*render {
		^super.render(">");
	}

	*event {
		arg incoming=();
		// TODO: maybe should be amp times
		^super.decorateIncoming(incoming, \ampPlus, [0.5,0.5]);
	}
}

LMarcato : LArticulation {
	*render {
		^super.render("^");
	}

	*event {
		arg incoming=();

		incoming = super.decorateIncoming(incoming, \ampPlus, [0.5,0.5]);
		^super.decorateIncoming(incoming, \legato, [0.2, 0.4]);
	}

}


LDynamicSpan : LDecoration {
	classvar prefix = "\\";

	*render {
		arg symbol;
		^super.render(prefix, symbol);
	}

	*event {
		arg incoming = ();
		incoming.ampInterp = true;
		^incoming;
	}
}

LCresc : LDynamicSpan {
	*render {
		^super.render("<");
	}
}

LDimin : LDynamicSpan {
	*render {
		^super.render(">");
	}
}

LSlur : LDecoration {
	classvar prefix = "\\";

	*render {
		arg symbol;
		^super.render(prefix, symbol);
	}
}

LSlurStart : LSlur {
	*render {
		^super.render("(");
	}

	*event {
		arg incoming=();
		incoming.slur = \start;
		^incoming;
	}

}

LSlurEnd : LSlur {
	*render {
		^super.render(")");
	}

	*event {
		arg incoming=();
		incoming.slur = \end;
		^incoming;
	}
}

LGlissando : LDecoration {
	*render {
		^super.render("\\", "glissando");
	}
}


LNote {
	var <>pitch, <>duration, <>articulations, sign=\sharp;

	*new {
		arg pitch, duration, articulations=[], sign;

		if (articulations.isArray.not, {
			articulations = [articulations];
		});

		^super.newCopyArgs(pitch, duration, articulations, sign);
	}

	render {
		var durString = ((1/duration)*4).asInt.asString;

		var articulationString = if (articulations.notNil, {

			articulations.inject("",{
				|a,b|
				a ++ b.render;
			});

		},{
			"";
		});


		var pitchString = if (pitch != \rest, {
			pitch.lynote(sign: sign);
		},{
			"r";
		});

		^pitchString++durString++articulationString;
	}

	asEvent {
		arg additions;

		var result = (
			midinote: pitch,
			dur: duration
		);

		if (articulations.notNil, {
			articulations.do({
				|articulation|

				result = articulation.event(result);
			});
		});

		if (additions.notNil, { result = result.merge(additions) });

		^result;
	}

	addArticulations {
		arg newArticulations;
		articulations = articulations ++ newArticulations;
	}

	play {
		arg additions;
		var ev = this.asEvent(additions);
		if (ev.includesKey(\amp) && ev.includesKey(\ampPlus), {
			ev.amp = (ev.amp+ev.ampplus).clip(0,1.0);
		});
		^ev.play;
	}

}

LColl {
	var <lnotes;

	*new {
		arg notes;
		^super.newCopyArgs(notes);
	}

	++ {
		arg other;
		var res = switch (other.class,
			LColl, {
				LColl(this.lnotes ++ other.lnotes);
			},
			LTuplet, {
				LColl(this.lnotes ++ [other]);
			},
			{
				LCollConcatError("Cannot concat LColl with non LColl/LTuplet").throw;
			}
		);

		^res
	}

	at {
		arg ind;
		^lnotes[ind];
	}

	collect {
		arg f;
		^lnotes.collect(f);
	}

	render {
		^lnotes.collect(_.render).inject("", _+_).subStr(1);
	}

	pitch {
		^this.pitches;
	}

	pitches {
		^lnotes.collect(_.pitch).flatten;
	}

	duration {
		^this.durations;
	}

	durations {
		^lnotes.collect(_.duration).flatten;
	}

	asEvent {
		^this.asEventList;
	}

	prApplySlurs {
		arg events;
		var slurOn = false;

		events.do({
			|ev|

			if ((ev.slur == \start) && slurOn.not,{
				slurOn = true;
			});

			if ((ev.slur == \end) && slurOn, {
				slurOn = false;
			});

			if (slurOn, { ev.legato = 1.0; });

			ev.removeAt(\slur);
		});

		^events;
	}

	prApplyDynamics {
		arg events;

		var curAmp = 0;
		var ampIncr = 0;

		events.do({
			|ev, i|

			if (ev.includesKey(\amp), {
				curAmp = ev.amp;
				ampIncr = 0;
			},{
				curAmp = curAmp+ampIncr; //ampIncr is often zero
				ev.amp = curAmp;
			});

			if (ev.includesKey(\ampInterp),{

				var nextAmpInd = events.detectIndex({
					|ev,j|
					(j > i) && ev.includesKey(\amp)
				});

				if (nextAmpInd.notNil, {
					var nextAmp = events[nextAmpInd].amp;
					"% % % %".format(nextAmp, curAmp, nextAmpInd, i);
					ampIncr = (nextAmp-curAmp)/(nextAmpInd-i);
				});

				ev.removeAt(\ampInterp);

			});

			if (ev.includesKey(\ampPlus),{
				ev.amp = (ev.amp + ev.ampPlus).clip(0.0,1.0);
				ev.removeAt(\ampPlus);
			});

		});

		^events;
	}

	asEventList {
		arg additions, renderAmps=true, renderSlurs=true;
		var previousAmp = 0.5;

		// we call false here incase a note is an lcoll
		var events = lnotes.collect(
			_.asEvent(additions, false, false)
		).flatten.select(_.notNil);


		if (renderSlurs, {
			events = this.prApplySlurs(events);
		});

		if (renderAmps, {
			events = this.prApplyDynamics(events);
		});

		^events;
	}

	// TODO: Implement me Should be easy ;-)
	prMakeEnvelope {
		arg events, envChan, envCtl;
		// create envelope
		[[],[]]
	}

	prStripAmps {
		arg events;

		events.do({ events.removeAt(\amp); });

		^events;
	}

	play {
		arg additions, envChan, envCtl, stripAmps=false;

		if (envChan.isNil || envCtl.isNil, {
			^Pseq(this.asEventList(additions)).play; // finalize slurs and amps
		},{

			var events = this.asEventList(additions, renderAmps: false, renderSlurs: true);
			var env = this.prMakeEnvelope(events, envChan, envCtl);
			var envPattern = Pseg(env[0],env[1]);

			"LColl.play: envChan/envCtl not implemented yes".warn;
			if (stripAmps, { events = this.prStripAmps(events); });
			^Ppar([Pseq(events),envPattern]).play;
		});
	}
}

LCollConcatError : Error {
	*new {
		arg what;
		^super.newCopyArgs(what);
	}
}

LTuplet : LColl {
	var <numerator, <denominator;


	*new {
		arg notes, numerator, denominator;
		^super.newCopyArgs(notes, numerator.asInt, denominator.asInt);
	}

	++ {
		arg other;

		if ((other.class != LColl) && (other.class != LTuplet), {
			LCollConcatError("Cannot concat LColl with non LColl/LTuplet").throw;
		});

		^LColl([this, other]);
	}

	render {
		^"\\tuplet %/% { % }".format(numerator, denominator, super.render);
	}

	durations {
		^super.durations * (denominator/numerator);
	}

	asEventList {
		arg additions, renderAmps=true, renderSlurs=true;

		var events = super.asEventList(additions, renderAmps, renderSlurs);

		events.do({
			|e| e.dur = e.dur * (denominator/numerator);
		});
		^events;
	}
}


LTimeSignature {
	var <>numerator, <>denominator, numericString;

	*new {
		arg numerator, denominator, style = \none;

		var numericString = this.getNumericString(style);

		^super.newCopyArgs(numerator, denominator, numericString);
	}

	== {
		arg other;
		^(numerator == other.numerator) && (denominator == other.denominator)
	}

	*getNumericString {
		arg style;
		^switch(
			style,
			\none, {""},
			\default, {"\\defaultTimeSignature\n"},
			\numeric, {"\\numericTimeSignature\n"},
			{""}
		);
	}

	duration {
		^numerator*(4.0/denominator);
	}

	render {
		^numericString++"\\time %/%".format(numerator, denominator);
	}

	asEvent { ^nil }
}

LCompoundMeter  {
	var <>timeSignatures, <>numericString;

	*new {
		arg signaturesArray, style = \none;

		var numericString = this.getNumericString(style);

		var timeSignatures = signaturesArray.collect({
			|signature|
			LTimeSignature(signature[0], signature[1]);
		});

		^super.newCopyArgs(timeSignatures, numericString);
	}

	*getNumericString {
		arg style;
		^switch(
			style,
			\none, {""},
			\default, {"\\defaultTimeSignature\n"},
			\numeric, {"\\numericTimeSignature\n"},
			{""}
		);
	}

	duration {
		^timeSignatures.collect(_.duration).sum;
	}

	== {
		arg other;
		^if (other.timeSignatures.size == this.timeSignatures.size, {
			other.timeSignatures.every({
				|otherTs, i|
				otherTs == this.timeSignatures[i];
			});
		},{
			false;
		})
	}

	render {
		var sigs = timeSignatures.inject("",{
			|res, signature, i|
			var space = if (i > 0, {" "}, {""});
			res++("%(% %)".format(space, signature.numerator, signature.denominator));
		});

		^numericString++"\\compoundMeter #'(%)".format(sigs);
	}

	asEvent { ^nil }
}

LMeasure : LColl {
	var <>timeSignature, <>renderTimeSignature, <>comment;

	*new {
		arg notes, timeSignature=nil, renderTimeSignature = false, comment = "";
		if (timeSignature.isNil, {
			timeSignature = LTimeSignature(4,4);
		});
		if (comment.size > 0, {
			comment = " % "+comment;
		});
		^super.newCopyArgs(notes, timeSignature, renderTimeSignature, comment);
	}

	++ {
		LCollConcatError("Measure concatenation is not supported").throw;
	}


	exceedsBounds {
		^(super.durations.sum > timeSignature.duration);
	}

	render {
		^"\t\t"++timeSignature.render + super.render + "|" ++comment++"\n";
	}
}

LClef {
	var <clefname;

	*new {
		arg clefname;
		^super.newCopyArgs(clefname);
	}

	asEvent { ^nil }

	render {
		^"\\clef "++clefname;
	}

	== {
		arg other;
		^other.clefname == this.clefname;
	}
}

LStaff : LColl {
	var <clef, <timeSignature;

	*new {
		arg notes, clef, timeSignature;

		^super.newCopyArgs(notes, clef, timeSignature);
	}

	prAsArray {
		arg lstaff, addTimeSignature=true, addClef=true;
		var res = [];
		if (lstaff.timeSignature.notNil && addTimeSignature, {
			res = res ++ [lstaff.timeSignature]
		});
		if (lstaff.clef.notNil && addClef, {
			res = res ++ [lstaff.clef]
		});

		^(res++lstaff.lnotes);
	}

	++ {
		arg other;

		if (other.class != LStaff, {
			LCollConcatError(
				"Cannot concat LStaff with class %".format(other.class.asString)
			).throw;
		});

		^if (other.timeSignature.isNil && other.clef.isNil, {
			LStaff(this.lnotes++other.lnotes, clef, timeSignature);
		},{
			var addTimeSignature = (other.timeSignature != this.timeSignature);
			var addClef = (other.clef != this.clef);
			LStaff(
				this.prAsArray(this) ++
				this.prAsArray(other, addTimeSignature, addClef)
			);
		});

	}

	render {
		var tsString = if (timeSignature.notNil, {
			timeSignature.render;
		},{
			""
		});

		var clefString = if (clef.notNil, {
			clef.render;
		},{
			""
		});

		var items = "% % %".format(tsString, clefString, super.render).stripWhiteSpace;

		^"\\new Staff \\absolute { % }".format(items);
	}
}

LStaffGroup {
	var <staves, type;

	*new {
		arg staves, type="";
		^super.newCopyArgs(staves, type);
	}

	contextWrapper {
		var prefix = if (type.size > 0, {"\\new %".format(type) }, {""});
		^prefix++" <<\n % >>";
	}

	render {
		var rendered = staves.inject("", {
			|res, staff|
			res ++ "\t" ++ staff.render ++ "\n";
		});
		^this.contextWrapper.format(rendered);
	}

	play {
		^staves.do(_.play);
	}
}

LUtil {
	*doNothing {
		^nil;
	}
	// consolidateRests {}
	// consolidateTiedNotes {}
}
