TestLNoteRendering : UnitTest {
	test_notes_render_without_articulations {
		this.assertEquals(LNote(60,0.5).render, "c'8", "LNote: half beat eq eigth note");
		this.assertEquals(LNote(48, 4).render, "c1", "Lnote: 4 beats is a whole note");
	}

	test_notes_single_articulation {
		this.assertEquals(LNote(60,0.5, LStaccato).render, "c'8-.", "LNote: staccato");

	}

	test_notes_multiple_articulations {
		this.assertEquals(LNote(60, 0.5, [LStaccato, LSlurStart]).render, "c'8-.\\(", "LNote multi");
	}

	test_notes_rest_render {
		this.assertEquals(LNote(\rest, 0.5).render, "r8");
	}

}

TestLNoteEvents : UnitTest {
	test_notes_event_without_articulations {
		this.assertEquals(LNote(60,0.5).asEvent, (midinote: 60, dur: 0.5), "LNote event no articulations");
	}

	test_notes_event_single_articulation {
		this.assertEquals(LNote(60,0.5,LAccent).asEvent, (midinote: 60, dur: 0.5, ampPlus: 0.5), "Lnote event single articulation");
	}

	test_notes_event_multiple_articulations {
		var ev = LNote(60,0.5,[LAccent,LStaccato]).asEvent;

		this.assertEquals(ev.midinote, 60, "LNote Multi Articulation: midinote");
		this.assertEquals(ev.dur, 0.5, "LNote Multi Articulation: dur");
		this.assert(ev.legato.fuzzyEqual(0.45,0.1) >= 0.5, "LNote Multi Articulation: legato");
		this.assertEquals(ev.ampPlus, 0.5, "LNote Multi Articulation: ampplus");
	}

	test_notes_prototype_event {
		this.assertEquals(LNote(60,0.5).asEvent((test:1)), (midinote: 60, dur:0.5, test: 1), "Lnote.event prototype");
	}



	test_notes_rest_event {
		this.assertEquals(LNote(\rest, 0.5).asEvent,(midinote: \rest, dur: 0.5));
	}
}
