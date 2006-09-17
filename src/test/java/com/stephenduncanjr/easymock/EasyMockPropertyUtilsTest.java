/*
 * Copyright 2006 Stephen Duncan Jr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.stephenduncanjr.easymock;

import static com.stephenduncanjr.easymock.EasyMockPropertyUtils.propertiesEq;
import static com.stephenduncanjr.easymock.EasyMockPropertyUtils.propertyEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests EasyMockPropertyUtils.
 * 
 * Class created on Aug 17, 2006.
 * 
 * @author duncans (Stephen C. Duncan Jr. &lt;stephen.duncan@gmail.com&gt;)
 * @since 1.0
 */
public class EasyMockPropertyUtilsTest
{
	/**
	 * TestClass interface.
	 */
	public static interface ITest
	{
		/**
		 * Do something with a date.
		 * 
		 * @param date
		 */
		public void doSomething(Date date);

		/**
		 * Do something with a <code>TestClass</code>.
		 * 
		 * @param test
		 */
		public void doSomething(TestClass test);
	}

	/**
	 * TestClass class.
	 */
	public static class TestClass
	{
		/** Value. */
		private String value;

		/** Integer value. */
		private int intValue;

		/**
		 * Creates a new test object.
		 * 
		 * @param value
		 */
		TestClass(final String value)
		{
			this.value = value;
		}

		/**
		 * Creates a new test object.
		 * 
		 * @param intValue
		 */
		TestClass(final int intValue)
		{
			this.intValue = intValue;
		}

		/**
		 * @return Value of property value.
		 */
		public String getValue()
		{
			return this.value;
		}

		/**
		 * @param value
		 *        New value of property value.
		 */
		public void setValue(final String value)
		{
			this.value = value;
		}

		/**
		 * @return Value of property intValue.
		 */
		public int getIntValue()
		{
			return this.intValue;
		}

		/**
		 * @param intValue
		 *        New value of property intValue.
		 */
		public void setIntValue(final int intValue)
		{
			this.intValue = intValue;
		}
	}

	/** TestClass property */
	private static final String PROPERTY = "value";

	/** TestClass property */
	private static final String VALUE = "testValue";

	/** Integer TestClass property */
	private static final String INT_PROPERTY = "intValue";

	/** Integer TestClass property */
	private static final int INT_VALUE = 5;

	/** Matching test. */
	private TestClass matchTest;

	/** Matches both values test. */
	private TestClass matchBothTest;

	/** Non-matching test. */
	private TestClass failTest;

	/** Matching integer test. */
	private TestClass intMatchTest;

	/** Non-matching integer test. */
	private TestClass intFailTest;

	/** ITest interface to mock. */
	private ITest iTest;

	/**
	 * Sets up for the tests.
	 */
	@BeforeMethod(alwaysRun = true)
	public void setUp()
	{
		this.matchTest = new TestClass(VALUE);
		this.failTest = new TestClass("badValue");
		this.intMatchTest = new TestClass(INT_VALUE);
		this.intFailTest = new TestClass(7);
		this.iTest = createMock(ITest.class);
		this.matchBothTest = new TestClass(VALUE);
		this.matchBothTest.setIntValue(INT_VALUE);
	}

	/**
	 * Tests the propertyEq method.
	 */
	@Test(groups = "integration")
	public void testPropertyEq()
	{
		// TestClass that property succeeds
		this.iTest.doSomething(propertyEq(TestClass.class, PROPERTY, VALUE));
		replay(this.iTest);
		this.iTest.doSomething(this.matchTest);
		verify(this.iTest);

		// TestClass that non-matching property fails
		reset(this.iTest);
		this.iTest.doSomething(propertyEq(TestClass.class, PROPERTY, VALUE));
		replay(this.iTest);
		try
		{
			this.iTest.doSomething(this.failTest);
			verify(this.iTest);

			fail("Non-matching property value should not match.");
		}
		catch (final Throwable t)
		{
			assertTrue(t instanceof AssertionError);
		}

		// TestClass that property succeeds
		reset(this.iTest);
		this.iTest.doSomething(propertyEq(TestClass.class, INT_PROPERTY, INT_VALUE));
		replay(this.iTest);
		this.iTest.doSomething(this.intMatchTest);
		verify(this.iTest);

		// TestClass that non-matching property fails
		reset(this.iTest);
		this.iTest.doSomething(propertyEq(TestClass.class, INT_PROPERTY, INT_VALUE));
		replay(this.iTest);
		try
		{
			this.iTest.doSomething(this.intFailTest);
			verify(this.iTest);

			fail("Non-matching integer property value should not match.");
		}
		catch (final Throwable t)
		{
			assertTrue(t instanceof AssertionError);
		}
	}

	/**
	 * Tests the propertiesEq method.
	 */
	@Test(groups = "integration")
	public void testPropertiesEq()
	{
		final Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PROPERTY, VALUE);
		properties.put(INT_PROPERTY, INT_VALUE);

		// TestClass that property succeeds
		this.iTest.doSomething(propertiesEq(TestClass.class, properties));
		replay(this.iTest);
		this.iTest.doSomething(this.matchBothTest);
		verify(this.iTest);

		// TestClass that non-matching property fails
		reset(this.iTest);
		this.iTest.doSomething(propertiesEq(TestClass.class, properties));
		replay(this.iTest);
		try
		{
			this.iTest.doSomething(this.failTest);
			verify(this.iTest);

			fail("Non-matching property value should not match.");
		}
		catch (final Throwable t)
		{
			assertTrue(t instanceof AssertionError);
		}
	}
}