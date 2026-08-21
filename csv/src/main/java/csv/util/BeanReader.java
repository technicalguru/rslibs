/*
 * This file is part of CSV package.
 *
 *  CSV is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  CSV is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with CSV.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package csv.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import csv.CsvException;
import csv.TableReader;
import rs.baselib.lang.ReflectionUtils;

/**
 * Reads beans from the underlying table stream.
 * <p>
 * Please notice that you need to explicitely pass the bean class to the constructor when you create a 
 * parameterized BeanReader inline:
 * </p>
 * <pre>
 *   // Error: Invalid usage
 *   BeanReader&lt;TestBean&gt; beanReader = new BeanReader&lt;TestBean&gt;(tableReader);
 *   
 *   // Correct usage
 *   BeanReader&lt;TestBean&gt; beanReader = new BeanReader&lt;TestBean&gt;(<b>TestBean.class,</b> tableReader);
 * </pre>
 * <p>
 * You can omit the class argument in constructors when your BeanReader was explicitely defined as a class:
 * </p>
 * <pre>
 * public class MyBeanReader extends BeanReader&lt;TestBean&gt; {
 *    ...
 * }
 * 
 * // Ok here 
 * BeanReader&lt;TestBean&gt; = new MyBeanReader(tableReader);
 * </pre>
 * <p>
 * The reason for this is the lack of class parameter inspection for in-line parameters at runtime.
 * </p>
 * @param <T> Type of bean to be read
 * 
 * @author ralph
 *
 */
public class BeanReader<T> implements Iterator<T> {

	private TableReader reader;
	private boolean evaluateHeaderRow = false;
	private String attributes[] = null;
	private Map<String, Method> methods = new HashMap<String, Method>();
	private Class<T> beanClass;
	
	/**
	 * Constructor.
	 * Use this constructor when underlying reader will deliver the attribute names in
	 * first record.
	 * @param beanClass class of bean - reflection does not guarantee to find out the correct class.
	 * @param reader the underlying reader to read bean properties from
	 */
	public BeanReader(Class<T> beanClass, TableReader reader) {
		this(beanClass, reader, true, null);
	}

	/**
	 * Constructor.
	 * Use this constructor when underlying reader will deliver the attribute names in
	 * first record and you created a specific parameterized class for the reader.
	 * @param reader the underlying reader to read bean properties from
	 */
	public BeanReader(TableReader reader) {
		this(null, reader, true, null);
	}

	/**
	 * Constructor.
	 * Use this constructor if underlying reader does NOT deliver attribute names and you created
	 * a specific parameterized class for the reader.
	 * @param reader the underlying reader to read bean properties from
	 * @param attributes list of attribute names that will be used to create the beans
	 */
	public BeanReader(TableReader reader, String attributes[]) {
		this(null, reader, false, attributes);
	}
	/**
	 * Constructor.
	 * Use this constructor if underlying reader does NOT deliver attribute names.
	 * @param beanClass class of bean - reflection does not guarantee to find out the correct class.
	 * @param reader the underlying reader to read bean properties from
	 * @param attributes list of attribute names that will be used to create the beans
	 */
	public BeanReader(Class<T> beanClass, TableReader reader, String attributes[]) {
		this(beanClass, reader, false, attributes);
	}

	/**
	 * Internal Constructor.
	 * @param beanClass class of bean - reflection does not guarantee to find out the correct class.
	 * @param reader the underlying reader to read bean properties from
	 * @param attributes list of attribute names that will be used to create the beans
	 * @param evaluateHeaderRow whether header row will be delivered by reader
	 */
	@SuppressWarnings("unchecked")
	protected BeanReader(Class<T> beanClass, TableReader reader, boolean evaluateHeaderRow, String attributes[]) {
		this.reader = reader;
		this.evaluateHeaderRow = evaluateHeaderRow;
		if (!evaluateHeaderRow) setAttributes(attributes);
		if (beanClass == null) {
			// try to find the parameter class
			beanClass = (Class<T>)ReflectionUtils.getTypeArguments(BeanReader.class, getClass()).get(0);
		}
		if (beanClass == null) {
			throw new CsvException("The parameter class is unknown. See http://download.ralph-schuster.eu/eu.ralph-schuster.csv/STABLE/apidocs/csv/util/BeanReader.html");
		}
		this.beanClass = beanClass;
	}

	/**
	 * Returns true when there are more beans to be returned.
	 * @see java.util.Iterator#hasNext()
	 * @see csv.TableReader#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if (isEvaluateHeaderRow() && (attributes == null)) {
			// read header row first
			readHeaderRow();
		}
		return reader.hasNext();
	}

	/**
	 * Returns the next bean from the table reader.
	 * @see java.util.Iterator#next()
	 * @see csv.TableReader#next()
	 * @see #convertToBean(Object[])
	 */
	@Override
	public T next() {
		if (!hasNext()) throw new CsvException("End of stream");
		return convertToBean(reader.next());
	}

	/**
	 * Resets the reader.
	 */
	public void reset() {
		reader.reset();
	}
	
	/**
	 * Closes the reader.
	 */
	public void close() {
		reader.close();
	}
	
	/**
	 * Constructs new bean from values in array. 
	 * @param columns attribute values
	 * @return new bean
	 */
	public T convertToBean(Object columns[]) {
		// Create new bean
		String attribute = null;
		try {
			T rc = beanClass.getConstructor().newInstance();
			for (int i=0; i<columns.length; i++) {
				attribute = getAttributeName(i);
				// Ignore attribute if not known
				if (attribute == null) continue;
				
				// Get the method and set attribute
				Method m = getMethod(attribute);
				m.invoke(rc, columns[i]);
			}
			
			return rc;
		} catch (NoSuchMethodException e) {
			throw new CsvException("Cannot find constructor for bean", e);
		} catch (InvocationTargetException e) {
			throw new CsvException("Cannot set attribute: "+attribute, e);
		} catch (InstantiationException e) {
			throw new CsvException("Cannot create JavaBean", e);
		} catch (IllegalAccessException e) {
			throw new CsvException("Cannot create JavaBean", e);
		}
	}
	
	/**
	 * Method not supported.
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new CsvException("Remove not supported");
	}

	/**
	 * Returns true if attribute names will be evaluated from header row.
	 * @return true if attribute names will be evaluated
	 */
	public boolean isEvaluateHeaderRow() {
		return evaluateHeaderRow;
	}

	/**
	 * Returns the attributes that will be used for each column index.
	 * @return the attributes array where attribute name stands at respective index.
	 */
	public String[] getAttributes() {
		return attributes;
	}

	/**
	 * Sets the attribute names to be set for each column.
	 * @param attributes attribute names to set
	 */
	protected void setAttributes(String attributes[]) {
		if (attributes == null) {
			throw new CsvException("attribute names cannot be null");
		}
		this.attributes = attributes;
	}
	
	/**
	 * Reads the next row from stream and sets the attribute names.
	 */
	public void readHeaderRow() {
		if (reader.hasHeaderRow()) {
	        setAttributes(CSVUtils.convertArray(reader.getHeaderRow(), 0));
		}
	}
	
	/**
	 * Returns the attribute name of specified column
	 * @param columnIndex index of column
	 * @return name or null if it was not set
	 */
	protected String getAttributeName(int columnIndex) {
		String attr[] = getAttributes();
		if (attr == null) return null;
		if (attr.length <= columnIndex) return null;
		return attr[columnIndex];
	}
	
	/**
	 * Returns the correct setter method object for the given attribute.
	 * The method will be found by inspection of the JavaBean class.
	 * @param attribute attribute to be set
	 * @return setter method
	 */
	protected Method getMethod(String attribute) {
		// Check if method already found
		Method rc = methods.get(attribute);
		if (rc == null) {
			String mName = getMethodName(attribute);

			// Use first method that match the setter
			// This is half-perfect as it would be better
			// to have the parameter class. However, underlying
			// streams may return null values in columns
			Method arr[] = beanClass.getMethods();
			for (Method m : arr) {
				if (m.getName().equals(mName) && isValidSetterMethod(m)) {
					rc = m;
					break;
				}
			}
			
			if (rc == null) {
				throw new CsvException("No setter found for: "+attribute);
			}
			
			methods.put(attribute, rc);
		}
		return rc;
	}
	
	/**
	 * Returns true if method conforms to JavaBean style of a Setter.
	 * @param method method
	 * @return true if method is a setter.
	 */
	protected boolean isValidSetterMethod(Method method) {
		// Has method return type?
		Class<?> returnType = method.getReturnType();
		if (!returnType.equals(Void.TYPE)) return false;
		
		// Is method public?
		if (!Modifier.isPublic(method.getModifiers())) return false;
		
		// Has method arguments?
		if (method.getParameterTypes().length != 1) return false;
		
		// Method is a setter?
		if (!method.getName().startsWith("set")) return false;

		return true;
	}
	
	/**
	 * This implementation returns the name of setter method for the given attribute
	 * @param attribute attribute
	 * @return the name of the setter method for this attribute
	 */
	protected String getMethodName(String attribute) {
		return "set"+attribute.substring(0, 1).toUpperCase() + attribute.substring(1);		
	}
}
