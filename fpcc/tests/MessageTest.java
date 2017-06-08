package fpcc.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fpcc.protocol.Message;

public class MessageTest {
	Message m;
	@Before
	public void setUp() throws Exception {
		m = new Message(1, 512);
	}

	@Test
	public void test() {
		byte[] packet = m.getBytes();
		assertEquals(5, packet.length);
		assertTrue(m.getOpCode() == packet[0]);
		Message anotherMessage = new Message(packet);
		assertEquals(512, anotherMessage.getValue());
	}

}
