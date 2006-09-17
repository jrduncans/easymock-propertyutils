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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.easymock.IArgumentMatcher;

/**
 * Matches based on a bean property.
 * 
 * Class created on Aug 16, 2006.
 * 
 * @author duncans (Stephen C. Duncan Jr. &lt;stephen.duncan@gmail.com&gt;)
 * @since 1.0
 */
public class BeanProperty implements IArgumentMatcher
{
	/** Map of property names to values to match against. */
	private Map<String, ?> expectedProperties;

	/**
	 * Creates a new matcher for the given property name and value.
	 * 
	 * @param expectedPropertyValue
	 * @param propertyName
	 */
	public BeanProperty(final String propertyName, final Object expectedPropertyValue)
	{
		super();
		final Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(propertyName, expectedPropertyValue);
		this.expectedProperties = properties;
	}

	/**
	 * Creates a new match for the given map of property names to values.
	 * 
	 * @param expectedProperties
	 * @since 1.1
	 */
	public BeanProperty(final Map<String, ?> expectedProperties)
	{
		super();
		this.expectedProperties = expectedProperties;
	}

	/**
	 * @see org.easymock.IArgumentMatcher#matches(java.lang.Object)
	 */
	public boolean matches(final Object actual)
	{
		for (final Entry<String, ?> entry : this.expectedProperties.entrySet())
		{
			try
			{
				final Object actualValue = PropertyUtils.getProperty(actual, entry.getKey());
				if (!(entry.getValue() == actualValue || entry.getValue().equals(actualValue)))
				{
					return false;
				}
			}
			catch (final Exception e)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * @see org.easymock.IArgumentMatcher#appendTo(java.lang.StringBuffer)
	 */
	public void appendTo(final StringBuffer buffer)
	{
		buffer.append("propertyEq(");

		for (final Entry<String, ?> entry : this.expectedProperties.entrySet())
		{
			buffer.append(entry.getKey());
			buffer.append("=");
			buffer.append(entry.getValue());
			buffer.append(")");
		}
	}
}