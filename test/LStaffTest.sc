TestLStaff : UnitTest {
	test_render {
		var staff = LStaff(
			[LNote(60,1),LNote(60,1)],
			LClef("treble"),
			LTimeSignature(4,4));

		this.assertEquals(
			staff.render,
			"\t\\new Staff \\absolute { \\time 4/4 \\clef treble c'4 c'4 }"
		);

	}

	test_event_list {
		var staff = LStaff(
			[LNote(60,1),LNote(60,1)],
			LClef("treble"),
			LTimeSignature(4,4));


		this.assertEquals(
			staff.asEventList((),false,false),
			[(midinote: 60, dur:1), (midinote: 60, dur: 1)]
		);
	}

	test_concat_no_ts_or_clef {
		var staves = LStaff([LNote(60,1),LNote(60,1)]);
		staves = staves ++ staves;

		this.assertEquals(
			staves.render,
			"\t\\new Staff \\absolute { c'4 c'4 c'4 c'4 }"
		);

	}

	test_concat_ts_clef_orignal_only {
		var staff = LStaff(
			[LNote(60,1),LNote(60,1)],
			LClef("treble"),
			LTimeSignature(4,4)
		) ++ LStaff([LNote(60,1)]);

		this.assertEquals(
			staff.render,
			"\t\\new Staff \\absolute { \\time 4/4 \\clef treble c'4 c'4 c'4 }"
		);

	}

	test_concat_clef_same {
		var staff = LStaff(
			[LNote(60,1),LNote(60,1)],
			LClef("treble"),
			LTimeSignature(4,4)
		) ++ LStaff([LNote(60,1)], LClef("treble"));

		this.assertEquals(
			staff.render,
			"\t\\new Staff \\absolute { \\time 4/4 \\clef treble c'4 c'4 c'4 }"
		);
	}

	test_concat_clef_diff {
		var staff = LStaff(
			[LNote(60,1),LNote(60,1)],
			LClef("treble"),
			LTimeSignature(4,4)
		) ++ LStaff([LNote(60,1)], LClef("bass"));

		this.assertEquals(
			staff.render,
			"\t\\new Staff \\absolute { \\time 4/4 \\clef treble c'4 c'4 \\clef bass c'4 }"
		);
	}


	test_concat_ts_same {
		var staff = LStaff(
			[LNote(60,1),LNote(60,1)],
			LClef("treble"),
			LTimeSignature(2,4)
		) ++ LStaff([LNote(60,1)], timeSignature: LTimeSignature(2,4));

		this.assertEquals(
			staff.render,
			"\t\\new Staff \\absolute { \\time 2/4 \\clef treble c'4 c'4 c'4 }"
		);
	}

	test_concat_ts_diff {
		var staff = LStaff(
			[LNote(60,1),LNote(60,1)],
			LClef("treble"),
			LTimeSignature(2,4)
		) ++ LStaff([LNote(60,1)], timeSignature: LTimeSignature(4,4));

		this.assertEquals(
			staff.render,
			"\t\\new Staff \\absolute { \\time 2/4 \\clef treble c'4 c'4 \\time 4/4 c'4 }"
		);
	}

	test_duration {
		var staff = LStaff(
			[LNote(60,1),LNote(60,1)],
			LClef("treble"),
			LTimeSignature(4,4));

		this.assertEquals(staff.duration, 2);
	}

}