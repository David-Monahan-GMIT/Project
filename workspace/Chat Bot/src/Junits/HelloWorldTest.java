package Junits;

import static org.junit.Assert.*;

import org.junit.Test;

public class HelloWorldTest extends HelloWorld{

	@Test
	public void test() {
		assertTrue(helloWorld() == "Hello World.");
	}

}
