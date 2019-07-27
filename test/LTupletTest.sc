TestLTuplet : UnitTest {
	test_ltuplet_render {
		var tup = LTuplet.new(Array.fill(3,{LNote(60,0.5)}),3,2.0);
		this.assertEquals(tup.render, "\\tuplet 3/2 { c'8 c'8 c'8 }");
	}

	test_ltuplet_events {
		var tup = LTuplet.new(Array.fill(3,{LNote(60,0.5)}),3,2.0);
		this.assertEquals(
			tup.asEventList,
			Array.fill(3, { (amp: 0, midinote: 60, dur: 0.5*2/3.0) })
		);
	}

	test_ltuplet_from_array {
		var tup = LTuplet.new(Array.fill(3,{LNote(60,0.5)}),3,2.0);
		this.assertEquals(tup.render, "\\tuplet 3/2 { c'8 c'8 c'8 }");
	}

	test_ltuplet_from_lcoll {
		var lcoll = LColl(Array.fill(3, { LNote(60,0.5) }));
		var tup = LTuplet.new(lcoll,3,2.0);
		this.assertEquals(tup.render, "\\tuplet 3/2 { c'8 c'8 c'8 }");
	}

	test_ltuplet_nesting_render {
		var tup = LTuplet([
			LNote(60, 0.5),
			LNote(60,0.5),
			LTuplet([
				LNote(60, 0.25), LNote(60, 0.25), LNote(60, 0.25)
			], 3,2.0)
		], 3,2.0);
		this.assertEquals(tup.render, "\\tuplet 3/2 { c'8 c'8 \\tuplet 3/2 { c'16 c'16 c'16 } }");
	}

	test_ltuplet_nesting_event_durations {
		var tup = LTuplet([
			LNote(60, 0.5),
			LNote(60,0.5),
			LTuplet([
				LNote(60, 0.25), LNote(60, 0.25), LNote(60, 0.25)
			], 3,2.0)
		], 3,2.0);
		var durs = tup.asEvent.collect(_.dur);
		var expected_durs = [1/3.0,1/3.0,1/9.0,1/9.0,1/9.0];

		durs.do({
			|d,i|
			this.assertEquals(d, expected_durs[i]);
		});

	}

	test_ltuplet_concatenate {
		var testTup = LTuplet(Array.fill(3,{LNote(60,0.5)}), 3, 2);
		var testCol = LColl([LNote(60,1)]);

		this.assertEquals((testTup++testTup).render, "\\tuplet 3/2 { c'8 c'8 c'8 } \\tuplet 3/2 { c'8 c'8 c'8 }");
		this.assertEquals((testCol++testTup).render, "c'4 \\tuplet 3/2 { c'8 c'8 c'8 }");
	}

	test_ltupelet_concat_error {
		this.assertException({
			LTuplet([],3,2) ++ 1;
		}, LCollConcatError);
	}

}