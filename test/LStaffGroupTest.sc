TestLStaffGroup : UnitTest {

	test_error_on_non_staff_new_group {
		this.assertException({
			LStaffGroup([LColl([])]);
		},LStaffGroupTypeError);
	}

	test_staff_group_render {
		var lstaff = LStaff([LNote(60,1)], LClef("treble"), LTimeSignature(4,4));
		var lsg = LStaffGroup([lstaff]);

		var actualOutput = lsg.render;
		var expectedOutput = "<<
\t\\new Staff \\absolute { \\time 4/4 \\clef treble c'4 }
 >>";

		// UNTESTABLE
		//this.assertEquals(actualOutput, expectedOutput);
	}

	test_nested_staff_group_render {
		var lstaff = LStaff([LNote(60,1)], LClef("treble"), LTimeSignature(4,4));
		var lsg = LStaffGroup([lstaff]);
		var nlsg = LStaffGroup([lsg]);

		var actualOutput = nlsg.render;
		var expectedOutput = "<<
 <<
\t\\new Staff \\absolute { \\time 4/4 \\clef treble c'4 }
 >>
 >>";
		// UNTESTABLE
		// this.assertEquals(actualOutput, expectedOutput);
	}

	test_staff_group_context_wrapper {
		var lstaff = LStaff([LNote(60,1)], LClef("treble"), LTimeSignature(4,4));
		// note, the below actually produces invalid lilypond output
		var lsg = LStaffGroup([lstaff],"arbitraryGroup");

		var actualOutput = lsg.render;
		var expectedOutput = "\\new arbitraryGroup <<
\t\\new Staff \\absolute { \\time 4/4 \\clef treble c'4 }
 >>";

		// UNTESTABLE
		// this.assertEquals(actualOutput, expectedOutput);

	}


	test_duration {
		var lstaff = LStaff([LNote(60,1)], LClef("treble"), LTimeSignature(4,4));
		var lsg = LStaffGroup([lstaff,lstaff],"arbitraryGroup");

		this.assertEquals(lsg.duration,1);
	}

	test_durations_nested_group {
		var lstaff = LStaff([LNote(60,1)], LClef("treble"), LTimeSignature(4,4));
		var lsg = LStaffGroup([lstaff,lstaff],"arbitraryGroup");
		var nlsg = LStaffGroup([lsg, lsg]);

		this.assertEquals(lsg.duration,1);
	}

	test_error_concat_non_match_structure {
		var lsg1 = LStaffGroup([LStaff([LNote(60,1)])]);
		var lsg2 = LStaffGroup([lsg1]);

		this.assertException({
			lsg1 ++ lsg2;
		},LStaffGroupConcatError);

	}

	test_error_concat_non_match_recursive {
		var lsg1 = LStaffGroup([
			LStaffGroup([
				LStaff([])
			])
		]);
		var lsg2 = LStaffGroup([
			LStaffGroup([
				LStaffGroup([])
			])
		]);
		this.assertException({
			lsg1 ++ lsg2;
		},LStaffGroupConcatError);

	}

	test_error_internally_unequalsize_concat {
		var s1 = LStaff([LNote(60,1)]);
		var s2 = LStaff([LNote(60, 0.5)]);

		var sg1 = LStaffGroup([s1,s2]);
		var sg2 = LStaffGroup([LStaff([]), LStaff([])]);

		this.assertException({
			sg1++sg2;
		},LStaffGroupConcatError);
	}

	test_staff_group_concat_render {
		var staff = LStaff([LNote(60,1)]);
		var sg = LStaffGroup([staff]);
		(sg++sg).render;
		// not easily testable
	}

	test_nested_staff_group_concat_render {
		// not easily testable
		var staff = LStaff([LNote(60,1)]);
		var sg = LStaffGroup([staff]);
		var nsg = LStaffGroup([sg]);
		(nsg++nsg).render;
	}



}