TestDynamics : UnitTest {
	test_ldynamics_rendering {
		this.assertEquals(LDfff.render, "\\fff", "Render fff");
		this.assertEquals(LDff.render, "\\ff", "Render ff");
		this.assertEquals(LDf.render, "\\f", "Render f");
		this.assertEquals(LDmf.render, "\\mf", "Render mf");
		this.assertEquals(LDmp.render, "\\mp", "Render mp");
		this.assertEquals(LDp.render, "\\p", "Render p");
		this.assertEquals(LDpp.render, "\\pp", "Render pp");
		this.assertEquals(LDppp.render, "\\ppp", "Render ppp");
	}

	test_ldynamics_blankevent {

		this.assertEquals(LDfff.event, (amp: 1.0), "fff blank event");
		this.assertEquals(LDff.event, (amp: 7/8.0), "ff blank event");
		this.assertEquals(LDf.event, (amp: 6/8.0), "f blank event");
		this.assertEquals(LDmf.event, (amp: 5/8.0), "mf blank event");
		this.assertEquals(LDmp.event, (amp: 4/8.0), "mp blank event");
		this.assertEquals(LDp.event, (amp: 3/8.0), "p blank event");
		this.assertEquals(LDpp.event, (amp: 2/8.0), "pp blank event");
		this.assertEquals(LDppp.event, (amp: 1/8.0), "ppp blank event");
	}

	test_ldynamics_baseevent {
		var prot = (testVal: 1);

		this.assertEquals(LDfff.event(prot), (testVal: 1, amp: 1.0), "fff base event(prot)");
		this.assertEquals(LDff.event(prot), (testVal: 1, amp: 7/8.0), "ff base event(prot)");
		this.assertEquals(LDf.event(prot), (testVal: 1, amp: 6/8.0), "f base event(prot)");
		this.assertEquals(LDmf.event(prot), (testVal: 1, amp: 5/8.0), "mf base event(prot)");
		this.assertEquals(LDmp.event(prot), (testVal: 1, amp: 4/8.0), "mp base event(prot)");
		this.assertEquals(LDp.event(prot), (testVal: 1, amp: 3/8.0), "p base event(prot)");
		this.assertEquals(LDpp.event(prot), (testVal: 1, amp: 2/8.0), "pp base event(prot)");
		this.assertEquals(LDppp.event(prot), (testVal: 1, amp: 1/8.0), "ppp base event(prot)");
	}

}

TestArticulations : UnitTest {
	assertArticulationEventCorrectKey {
		|event, desiredKey, desiredRange, string|
		this.assert(
			if (event.includesKey(desiredKey).not, {
				false;
			},{
				var val = event[desiredKey];
				(val >= desiredRange[0]) && (val <= desiredRange[1])
			}),
			string
		);
	}

	test_larticulation_rendering {
		this.assertEquals(LStaccato.render, "-.", "LStaccato Render");
		this.assertEquals(LStaccatissimo.render, "-!", "LStaccatissimo Render");
		this.assertEquals(LPortato.render, "-_", "LPortato Render");
		this.assertEquals(LTenuto.render, "--", "LTenuto Render");
		this.assertEquals(LAccent.render, "->", "LAccent Render");
		this.assertEquals(LMarcato.render, "-^", "LMarcato Render");

	}

	test_larticulation_blankevent {
		this.assertArticulationEventCorrectKey(
			LStaccato.event, \legato, [0.4,0.5],"LStaccato Blank Event");
		this.assertArticulationEventCorrectKey(
			LStaccatissimo.event, \legato, [0.15,0.2],"LStaccatissimo Blank Event");
		this.assertArticulationEventCorrectKey(
			LPortato.event, \legato, [0.7,0.8],"LPortato Blank Event");
		this.assertArticulationEventCorrectKey(
			LTenuto.event, \legato, [0.9,0.95],"LTenuto Blank Event");
		this.assertEquals(LAccent.event, (ampPlus:0.5),"LAccent Blank Event");
		this.assertArticulationEventCorrectKey(
			LMarcato.event, \legato, [0.2,0.4],"LMarcato Blank Event legato");
		this.assertArticulationEventCorrectKey(
			LMarcato.event, \ampPlus, [0.5,0.5],"LMarcato Blank Event amp");
	}

	test_larticulation_baseevent {
		var prot = (testVal: 1);

		this.assertArticulationEventCorrectKey(
			LStaccato.event(prot), \legato, [0.4,0.5],"LStaccato base Event");
		this.assertArticulationEventCorrectKey(
			LStaccato.event(prot), \testVal, [1.0,1.0],"LStaccato base Event");
		// more coming soon

	}
}

TestMiscSpans : UnitTest {
	test_cresc {
		this.assertEquals(LCresc.render, "\\<", "LCresc.render");
		this.assertEquals(LCresc.event, (ampInterp: true), "LCresc event");
		this.assertEquals(LCresc.event((t:1)), (ampInterp: true, t:1), "LCresc event args");
	}

	test_dim {
		this.assertEquals(LDimin.render, "\\>", "Ldimin.render");
		this.assertEquals(LDimin.event, (ampInterp: true), "Ldimin event");
		this.assertEquals(LDimin.event((t:1)), (ampInterp: true, t:1), "Ldimin event args");
	}

	test_slur_start {
		this.assertEquals(LSlurStart.render, "\\(", "LSlurStart.render");
		this.assertEquals(LSlurStart.event, (slur: \start), "LSlurStart event");
		this.assertEquals(LSlurStart.event((t:1)), (slur: \start, t:1), "LSlurStart event args");
	}

	test_slur_end {
		this.assertEquals(LSlurEnd.render, "\\)", "LSlurEnd.render");
		this.assertEquals(LSlurEnd.event, (slur: \end), "LSlurEnd event");
		this.assertEquals(LSlurEnd.event((t:1)), (slur: \end, t:1), "LSlurEnd event args");
	}

	test_gliss {
		this.assertEquals(LGlissando.render, "\\glissando", "render glissando");
	}



}