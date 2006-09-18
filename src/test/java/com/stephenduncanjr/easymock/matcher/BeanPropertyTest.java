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
package com.stephenduncanjr.easymock.matcher;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

/**
 * Tests the BeanProperty matcher.
 * 
 * Class created on Aug 17, 2006. TestClass
 * 
 * @author stephen.duncan (Stephen C. Duncan Jr. &lt;stephen.duncan@gmail.com&gt;)
 * @since 1.0
 */
public class BeanPropertyTest
{
	/**
	 * TestClass class.
	 */
	public static class TestClass
	{
		/** Integer value. */
		private int intValue;

		/** Value. */
		private String value;

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
		 * Creates a new test object.
		 * 
		 * @param value
		 */
		TestClass(final String value)
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
		 * @return Value of property value.
		 */
		public String getValue()
		{
			return this.value;
		}

		/**
		 * @param intValue
		 *        New value of property intValue.
		 */
		public void setIntValue(final int intValue)
		{
			this.intValue = intValue;
		}

		/**
		 * @param value
		 *        New value of property value.
		 */
		public void setValue(final String value)
		{
			this.value = value;
		}
	}

	/**
	 * Tests the appender.
	 */
	@Test(groups = "unit")
	public void testAppender()
	{
		final String propertyName = "value";
		final String value = "testValue";
		final BeanProperty beanProperty = new BeanProperty(propertyName, value);

		final StringBuffer buffer = new StringBuffer();
		beanProperty.appendTo(buffer);

		assertTrue(buffer.toString().contains(propertyName), "Start value should occur in append.");
		assertTrue(buffer.toString().contains(value), "End value should occur in append.");
	}

	/**
	 * TestClass for mulitple properties provided as a map.
	 */
	@Test(groups = "unit")
	public void testBeanProperties()
	{
		final String propertyName1 = "value";
		final String value1 = "value1";

		final String propertyName2 = "intValue";
		final int value2 = 2;

		final TestClass matchTest = new TestClass(value1);
		matchTest.setIntValue(value2);

		final TestClass noMatchTest1 = new TestClass("badTestValue");
		noMatchTest1.setIntValue(value2);

		final TestClass noMatchTest2 = new TestClass(value1);
		noMatchTest2.setIntValue(1);

		final TestClass noMatchTest3 = new TestClass("badTestValue");
		noMatchTest3.setIntValue(1);

		final Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(propertyName1, value1);
		properties.put(propertyName2, value2);

		final BeanProperty beanProperty = new BeanProperty(properties);

		assertTrue(beanProperty.matches(matchTest), "All equal property values should match.");
		assertFalse(beanProperty.matches(noMatchTest1), "Not equal property value should not match.");
		assertFalse(beanProperty.matches(noMatchTest2), "Not equal property value should not match.");
		assertFalse(beanProperty.matches(noMatchTest3), "Not equal property value should not match.");
		assertFalse(beanProperty.matches(new Object()), "Object that does not have property should not match.");
		assertFalse(new BeanProperty("bad", value1).matches(matchTest), "Invalid property should not match.");
	}

	/**
	 * Tests the bean property matcher.
	 */
	@Test(groups = "unit")
	public void testBeanProperty()
	{
		final String propertyName = "value";
		final String value = "testValue";

		TestClass matchTest = new TestClass("testValue");
		TestClass noMatchTest = new TestClass("badTestValue");

		BeanProperty beanProperty = new BeanProperty(propertyName, value);

		assertTrue(beanProperty.matches(matchTest), "Equal property value should match.");
		assertFalse(beanProperty.matches(noMatchTest), "Not equal property value should not match.");
		assertFalse(beanProperty.matches(new Object()), "Object that does not have property should not match.");
		assertFalse(new BeanProperty("bad", value).matches(matchTest), "Invalid property should not match.");

		// TestClass integer property
		matchTest = new TestClass(5);
		noMatchTest = new TestClass(4);

		beanProperty = new BeanProperty("intValue", 5);

		assertTrue(beanProperty.matches(matchTest), "Equal integer property value should match.");
		assertFalse(beanProperty.matches(noMatchTest), "Not equal integer property value should not match.");
	}

	/**
	 * Tests null handling.
	 */
	@Test(groups = "unit")
	public void testNulls()
	{
		final TestClass test = new TestClass("value");
		final TestClass nullTest = new TestClass(null);

		assertFalse(new BeanProperty(null, null).matches(null), "Null should not match.");
		assertFalse(new BeanProperty(null, null).matches(test), "Null should not match non-null.");
		assertFalse(new BeanProperty(null, test).matches(null), "Non-null should not match null.");
		assertTrue(new BeanProperty("value", null).matches(nullTest), "Null values should match.");
	}
}