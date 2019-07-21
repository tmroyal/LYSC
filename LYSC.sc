LDecoration {
	var <>symbol, <>event;

	*new {
		arg symbol, event;
		^super.newCopyArgs(symbol, event);
	}

	render {
		^symbol;
	}
}

LDynamic : LDecoration {
	render {
		^"\\"++symbol;
	}
}

LArticulation : LDecoration {
	render {
		^"-"++symbol;
	}
}


LNote {
	var <>pitch, <>duration, <>articulations, dynamic;

	*new {
		arg pitch, duration, articulations, dynamic;
		^super.newCopyArgs(pitch, duration, articulations, dynamic);
	}

	render {
		var durString = (1/duration)*4;
		// todo: handle single and multiple articulations
		var articulationString = if (articulations.notNil, {
			articulations.inject("",{
				|a,b|
				a ++ b.render;
			});
		},{
			"";
		});
		var dynamicString = if (dynamic.notNil, { dynamic.render }, {""});
		^pitch.lynote++durString++articulationString++dynamicString;
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
				// s/merge/blend/g?
				result = result.blend(articulation.event);
			});
		});

		if (dynamic.notNil, { result = result.blend(dynamic.event) });
		if (additions.notNil, { result = result.blend(additions) });

		^result;
	}

	play {
		arg additions;
		^this.asEvent(additions).play;
	}

}

LColl {
	var <lnotes;

	*newWithPitchesAndDurs {
		arg pitches, durations;
		var notes = pitches.size.collect({
			|i|
			LNote(pitches[i], durations.wrapAt(i));
		});
		^super.newCopyArgs(notes);
	}

	*new {
		arg notes;
		^super.newCopyArgs(notes);
	}

	render {
		^lnotes.collect(_.render).inject("", _+_);
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

	asEventList {
		arg additions;
		var previousAmp = 0.5;

		^lnotes.collect(_.asEvent(additions)
		).flatten.collect({
			|e|
			if (e.includesKey(\amp).not, {
				e.amp = previousAmp;
			},{
				previousAmp = e.amp;
			});
			e;
		});

	}

	play {
		arg additions;
		^Pseq(this.asEventList(additions)).play;
	}
}


LTuplet : LColl {
	var <multiplier = 1;

	*new {
		arg notes, multiplier;
		^super.newCopyArgs(notes, multiplier);
	}

	*newFromLColl {
		arg coll, multiplier;
		^super.newCopyArgs(coll.lnotes, multiplier);
	}

	render {
		var frac = multiplier.asFraction;

		^"\\tuplet %/% { % }".format(frac[0], frac[1], super.render);
	}

	durations {
		^super.durations * multiplier.reciprocal;
	}

	asEventList {
		arg additions;

		var events = lnotes.collect(_.asEvent(additions)).flatten;
		events.do({
			|e| e.dur = e.dur * multiplier.reciprocal;
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
}

LCompoundMeter  {
	var <>timeSignatures, <>numericString;

	*new {
		arg signaturesArray, style = \none;

		var numericString = this.getNumericString(style);

		var timeSignatures = signaturesArray.collect({
			|signature|
			LTimeSignature(signature[0], signature[1]);
		}).postln;

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

	render {
		var sigs = timeSignatures.inject("",{
			|res, signature, i|
			var space = if (i > 0, {" "}, {""});
			res++("%(% %)".format(space, signature.numerator, signature.denominator));
		});

		^numericString++"\\compoundMeter #'(%)".format(sigs);

	}
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

	exceedsBounds {
		^(super.durations.sum > timeSignature.duration);
	}

	render {
		^"\t\t"++timeSignature.render + super.render + "|" ++comment++"\n";
	}
}

LSpan {
}

// todo: reconceive dynamics as spans

LSpanEnvelope : LSpan {
}

LSpanEventEffector : LSpan {
}


LUtil {
	*doNothing {
		^nil;
	}
}
