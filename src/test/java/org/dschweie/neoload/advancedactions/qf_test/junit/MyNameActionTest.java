package org.dschweie.neoload.advancedactions.qf_test.junit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.dschweie.neoload.advancedactions.qf_test.daemon.QFTestPingAction;

public class MyNameActionTest {
	//@Test
	public void shouldReturnType() {
		final QFTestPingAction action = new QFTestPingAction();
		assertEquals("QF-Test - ping daemon", action.getType());
	}

}
