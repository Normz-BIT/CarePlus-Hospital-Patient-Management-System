package com.careplus.server;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


/*
 * The project's only JUnit test.
 *
 * It is an integration check rather than a unit test: it needs a running MySQL
 * instance with the CarePlus schema present, so it fails on any machine where the
 * database is not set up even though nothing is wrong with the code. That
 * environmental dependency is worth knowing before treating a red result as a
 * regression.
 */
class TestClass {

	@Test
	void test() {

		/*
		 * The commented out line refers to an EchoServerTest that no longer exists in
		 * the codebase, left from an earlier socket experiment. It is dead and should be
		 * deleted rather than restored.
		 */
		//assertEquals(1,EchoServerTest.test());
		/*
		 * 1 is the helper's success sentinel and 0 its failure code, so this asserts the
		 * connection check completed rather than asserting anything about the data.
		 */
		assertEquals(1,DBConnectionTest.test());
	}

}


