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

import static org.easymock.EasyMock.reportMatcher;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stephenduncanjr.easymock.matcher.BeanProperty;

/**
 * Utilities for using EasyMock.
 * 
 * @author stephen.duncan (Stephen C. Duncan Jr.
 *         &lt;stephen.duncan@gmail.com&gt;)
 * @since 1.0
 */
public class EasyMockPropertyUtils
{
	/**
	 * Disables object creation.
	 */
	private EasyMockPropertyUtils()
	{
		// Hide constructor
	}

	/**
	 * EasyMock matcher for the properties on the object to be matched being
	 * equal to the mapped value.
	 * 
	 * @param <T>
	 *        The type of object to match.
	 * @param inClass
	 *        The type of the object to match.
	 * @param properties
	 *        The map of property names to property values.
	 * @return fake return value for EasyMock use.
	 */
	public static <T> T propertiesEq(@SuppressWarnings("unused")
	final Class<T> inClass, final Map<String, ?> properties)
	{
		reportMatcher(new BeanProperty(properties));
		return null;
	}

	/**
	 * EasyMock matcher for the properties on the argument to match the
	 * properties on the given object.
	 * 
	 * @param <T>
	 *        The type of object to match.
	 * @param inClass
	 *        The type of the object to match.
	 * @param valuesObject
	 *        the object to match values against.
	 * @return fake return value for EasyMock use.
	 * @since 1.1
	 */
	public static <T> T propertiesEq(@SuppressWarnings("unused")
	final Class<T> inClass, final Object valuesObject)
	{
		reportMatcher(new BeanProperty(retrieveAndFilterProperties(valuesObject, null)));
		return null;
	}

	/**
	 * EasyMock matcher for the properties on the argument to match the
	 * properties on the given object, ignoring the properties named in the
	 * given list.
	 * 
	 * @param <T>
	 *        The type of object to match.
	 * @param inClass
	 *        The type of the object to match.
	 * @param valuesObject
	 *        the object to match values against.
	 * @param ignored
	 *        the list of property names to ignore.
	 * @return fake return value for EasyMock use.
	 * @since 1.1
	 */
	public static <T> T propertiesEq(@SuppressWarnings("unused")
	final Class<T> inClass, final Object valuesObject, final List<String> ignored)
	{
		reportMatcher(new BeanProperty(retrieveAndFilterProperties(valuesObject, ignored)));
		return null;
	}

	/**
	 * EasyMock matcher for the property on the object to be matched being equal
	 * to the given value.
	 * 
	 * @param <T>
	 *        The type of object to match.
	 * @param inClass
	 *        The type of the object to match.
	 * @param property
	 *        the name of the property.
	 * @param value
	 *        The value of the property to ccompare against.
	 * @return fake return value for EasyMock use.
	 */
	public static <T> T propertyEq(@SuppressWarnings("unused")
	final Class<T> inClass, final String property, final Object value)
	{
		reportMatcher(new BeanProperty(property, value));
		return null;
	}

	/**
	 * Gets the properties and values from the object as a map, ignoring the
	 * properties specified.
	 * 
	 * @param bean
	 * @param ignoredProperties
	 * @return map of properties names to values.
	 */
	private static Map<String, Object> retrieveAndFilterProperties(final Object bean, final List<String> ignoredProperties)
	{
		final Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			BeanInfo info;
			info = Introspector.getBeanInfo(bean.getClass());

			for(final PropertyDescriptor p : info.getPropertyDescriptors())
			{
				final Method readMethod = p.getReadMethod();
				final Object value = readMethod.invoke(bean);
				map.put(p.getName(), value);
			}

			// Must remove the class property, as it should be ignored.
			map.remove("class");
		}
		catch(final IntrospectionException e)
		{
			throw new IllegalArgumentException(e);
		}
		catch(final IllegalAccessException e)
		{
			throw new IllegalArgumentException(e);
		}
		catch(final InvocationTargetException e)
		{
			throw new IllegalArgumentException(e);
		}

		if(ignoredProperties != null)
		{
			for(final String property : ignoredProperties)
				map.remove(property);
		}

		return map;
	}
}