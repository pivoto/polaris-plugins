package io.polaris.core.lang;

import io.polaris.core.TestConsole;
import io.polaris.core.os.OS;
import org.junit.jupiter.api.Test;

class OSTest {

	@Test
	void test01() {
		OS.getAllIps().forEach(TestConsole::println);
	}
}
