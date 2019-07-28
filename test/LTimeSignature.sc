TestLTimeSignature : UnitTest {
	test_timesignature_renders {
		this.assertEquals(LTimeSignature(3,8).render, "\\time 3/8");
	}

	test_timesignature_nilevent {
		this.assertEquals(LTimeSignature(4,4).asEvent, nil);

	}

	test_timesignature_numeric {
		this.assertEquals(LTimeSignature(4,4,\numeric).render, "\\numericTimeSignature \\time 4/4");
	}

	test_accurate_duration {
		this.assertEquals(LTimeSignature(3,8).duration,1.5);
	}

	test_equality {
		this.assertEquals(LTimeSignature(4,4)==LTimeSignature(4,4), true);
	}
}

TestLCompoundMeter : UnitTest {
	test_compound_renders {
		var testString = LCompoundMeter([[3,4],[4,4]]).render;
		this.assertEquals(testString, "\\compoundMeter #'((3 4) (4 4))")
	}

	test_blank_event {
		this.assert(LCompoundMeter([[3,4],[4,4]]).asEvent.isNil);
	}

	test_accurate_duration {
		this.assertEquals(LCompoundMeter([[3,4],[4,4]]).duration, 7);
	}

	test_equality {
		var a = LCompoundMeter([[3,4],[4,4]]);
		var b = LCompoundMeter([[3,4],[4,4]]);

		this.assert(a == b);
	}
}