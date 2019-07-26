TestLColl : UnitTest {
	test_lcoll_render {
		var res = LColl([LNote(60,2),LNote(60,2)]).render;
		this.assertEquals(res, "c'2 c'2", "lcoll render");
	}

	test_lcoll_render_recurs {
		var res = LColl([LColl([LNote(60,2)]),LNote(60,2)]).render;
		this.assertEquals(res, "c'2 c'2", "lcoll render recurs");

	}

	test_lcoll_simple_eventlist {
		var res = LColl([LNote(60,2),LNote(60,2)]).asEventList((), false, false);
		this.assertEquals(res, [
			( midinote: 60, dur:2),
			( midinote: 60, dur:2),
		], "lcoll simple event list");

	}

	test_lcoll_simple_eventlist_recurse {
		var res = LColl([LColl([LNote(60,2)]),LNote(60,2)]).asEventList((), false, false);
		this.assertEquals(res, [
			( midinote: 60, dur:2),
			( midinote: 60, dur:2),
		], "lcoll simple event list recurse");

	}

	test_lcoll_event_proto {
		var res = LColl([LNote(60,2),LNote(60,2)]).asEventList((test: 1), false, false);
		this.assertEquals(res, [
			( midinote: 60, dur:2, test: 1),
			( midinote: 60, dur:2, test: 1),
		], "lcoll simple event list proto");

	}

	test_lcoll_event_apply_slurs_no_slurs {
		var res = LColl([LColl([LNote(60,2)]),LNote(60,2)]).asEventList((), false, true);
		this.assertEquals(res, [
			( midinote: 60, dur:2),
			( midinote: 60, dur:2),
		], "lcoll event apply slurs no slurs");

	}

	test_lcoll_apply_slurs {
		var res = LColl([LColl([LNote(60,2, LSlurStart)]),LNote(60,2, LSlurEnd)]).asEventList((), false, true);
		this.assertEquals(res, [
			( midinote: 60, dur:2, legato: 1.0),
			( midinote: 60, dur:2),
		], "lcoll event apply slurs");
	}

	test_lcoll_eventlist_render_accents {
		var res = LColl([LNote(60,2,[LAccent,LDmf])]).asEventList(renderAmps: true);
		this.assertEquals(res, [
			(midinote: 60, dur: 2, amp: 1.0)
		], "lcoll event accent");
	}

	test_lcoll_eventlist_render_cresc {
		var coll = LColl(Array.fill(3, { LNote(60,2) }));
		coll[0].addArticulations([LDp,LCresc]);
		coll[2].addArticulations(LDff);

		this.assertEquals(coll.asEventList[1], (dur: 2, amp: 0.625, midinote: 60), "render cresc");

	}

	test_lcoll_render_dim {
		var coll = LColl(Array.fill(3, { LNote(60,2) }));
		coll[0].addArticulations([LDff,LDimin]);
		coll[2].addArticulations(LDp);

		this.assertEquals(coll.asEventList[1], (dur: 2, amp: 0.625, midinote: 60), "render cresc");
	}

	test_lcoll_concatenate {
		var testColl = LColl(Array.fill(2, { LNote(60,1) }));
		testColl = testColl ++ testColl;

		this.assertEquals(testColl.render, "c'4 c'4 c'4 c'4");
	}

}

// TODO: Test pitches
// TODO: Test durations