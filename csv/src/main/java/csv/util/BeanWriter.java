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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import csv.CsvException;
import csv.TableWriter;

/**
 * Writes beans to an underlying table writer.
 * The attributes written are either determined by inspection of first bean to be written
 * or by explicitely setting them through special methods.
 * 
 * @param <T> Type of bean to be written
 * 
 * @author ralph
 *
 */
public class BeanWriter<T> {

	private TableWriter writer = null;
	private List<String> attributes = null;
	private Map<String, Method> methods = null;
	private boolean writeHeaderRow = true;
	private boolean headerRowWritten = false;
	
	/**
	 * Constructor.
	 * @param writer the underlying writer
	 * @param writeHeaderRow whether a header row with attribute names shall be written
	 */
	public BeanWriter(TableWriter writer, boolean writeHeaderRow) {
		setWriter(writer);
		setWriteHeaderRow(writeHeaderRow);
	}

	/**
	 * Returns true when a header row with attribute names shall be written.
	 * @return the writeHeaderRow
	 */
	public boolean isWriteHeaderRow() {
		return writeHeaderRow;
	}

	
	/**
	 * Sets whether a header row with attribute names shall be written.
	 * @param writeHeaderRow true if header row shall be written.
	 */
	protected void setWriteHeaderRow(boolean writeHeaderRow) {
		this.writeHeaderRow = writeHeaderRow;
	}

	/**
	 * Writes the bean to the underlying table writer.
	 * @param bean the bean to be written
	 * @throws IOException if bean cannot be written
	 * @see TableWriter#printRow(Object[])
	 * @see #convertToColumns(Object)
	 */
	@SuppressWarnings("unchecked")
	public void writeBean(T bean) throws IOException {
		if (attributes == null) createAttributeList((Class<T>)bean.getClass());
		if (isWriteHeaderRow() && !headerRowWritten) {
			writeHeaderRow();
		}
		getWriter().printRow(convertToColumns(bean));
	}
	
	/**
	 * Closes the underlying writer.
	 */
	public void close() {
		getWriter().close();
	}
	
	/**
	 * Returns the current writer.
	 * @return the writer
	 */
	public TableWriter getWriter() {
		return writer;
	}
	
	/**
	 * Sets the writer.
	 * @param writer the writer
	 */
	protected void setWriter(TableWriter writer) {
		this.writer = writer;
	}
	
	/**
	 * Converts the given bean to an object array.
	 * @param bean bean to be converted
	 * @return object array with attribute values
	 */
	@SuppressWarnings("unchecked")
	public Object[] convertToColumns(T bean) {
		if (attributes == null) createAttributeList((Class<T>)bean.getClass());
		Object rc[] = new Object[attributes.size()];
		for (int i=0; i<rc.length; i++) {
			rc[i] = getAttribute(i, bean);
		}
		return rc;
	}
	
	/**
	 * Returns the value for given bean at given column index.
	 * @param index column index
	 * @param bean bean object
	 * @return value in column
	 */
	@SuppressWarnings("unchecked")
	protected Object getAttribute(int index, T bean) {
		if (attributes == null) createAttributeList((Class<T>)bean.getClass());
		if ((index < 0) || (index >= attributes.size())) return null;
		String attribute = attributes.get(index);
		return getAttribute(attribute, bean);
	}
	
	/**
	 * Returns the attribute value for given bean.
	 * @param attribute name of attribute
	 * @param bean bean object
	 * @return value of attribute
	 */
	@SuppressWarnings("unchecked")
	protected Object getAttribute(String attribute, T bean) {
		if (attribute == null) return null;
		if (attributes == null) createAttributeList((Class<T>)bean.getClass());
		Method method = methods.get(attribute);
		return getAttribute(method, bean);
	}
	
	/**
	 * Returns the attribute value for given bean.
	 * @param method method that will deliver the value
	 * @param bean bean object
	 * @return value of attribute
	 */
	protected Object getAttribute(Method method, T bean) {
		if (bean == null) return null;
		if (method == null) return null;
		try {
			return method.invoke(bean);
		} catch (InvocationTargetException e) {
			throw new CsvException("Cannot call method", e);
		} catch (IllegalAccessException e) {
			throw new CsvException("Cannot call method", e);
		}
	}
	
	/**
	 * Creates the attribute list from given list.
	 * All JavaBean getter methods will be taken over.
	 * @param clazz Class to introspect
	 * @param attributes list of attributes 
	 */
	protected void createAttributeList(Class<T> clazz, List<String> attributes) {
		for (String attr : attributes) addGetter(clazz, attr);
	}
	
	/**
	 * Creates the attribute list from given list.
	 * All JavaBean getter methods will be taken over.
	 * @param clazz Class to introspect
	 * @param attributes list of attributes 
	 */
	protected void createAttributeList(Class<T> clazz, String attributes[]) {
		for (String attr : attributes) addGetter(clazz, attr);
	}
	
	/**
	 * Creates the attribute list by introspection.
	 * All JavaBean getter methods will be taken over.
	 * @param clazz Class to introspect
	 */
	protected void createAttributeList(Class<T> clazz) {
		Method methods[] = clazz.getMethods();
		for (Method m : methods) {
			if (!isValidGetterMethod(m)) continue;
			
			// Get the attribute Name
			String attributeName = getAttributeName(m);
			
			// Add the attribute
			addGetter(attributeName, m);
		}
	}
	
	/**
	 * Adds a getter method to the methods being used for retrieveing column values.
	 * The metthod must have a return type and no arguments.
	 * @param clazz Class to introspect
	 * @param attribute name of attribute
	 */
	protected void addGetter(Class<T> clazz, String attribute) {
		// What is the name of the getter?
		String mName = "get"+attribute.substring(0, 1).toUpperCase()+attribute.substring(1);
		try {
			Method m = clazz.getMethod(mName, (Class<?>[])null);
			if (!isValidGetterMethod(m)) throw new CsvException("No JavaBean attribute: "+attribute);
			addGetter(attribute, m);
		} catch (NoSuchMethodException e) {
			throw new CsvException("No such attribute", e);
		}
	}
	
	/**
	 * Returns true if method conforms to JavaBean style of a Getter.
	 * @param method method
	 * @return true if method is a getter.
	 */
	protected boolean isValidGetterMethod(Method method) {
		// Has method return type?
		Class<?> returnType = method.getReturnType();
		if (returnType == null) return false;
		
		// Is method public?
		if (!Modifier.isPublic(method.getModifiers())) return false;
		
		// Has method arguments?
		if (method.getParameterTypes().length > 0) return false;
		
		// Method is getClass()?
		if (method.getName().equals("getClass")) return false;
		
		// Special case: boolean getters may start with "is"
		if (returnType.equals(Boolean.class) || returnType.equals(Boolean.TYPE)) {
			if (method.getName().startsWith("is")) return true; 
		}
		
		// Method is a getter?
		if (!method.getName().startsWith("get")) return false;

		return true;
	}
	
	/**
	 * Adds a getter method to the methods being used for retrieveing column values.
	 * The metthod must have a return type and no arguments.
	 * @param attributeName name of attribute
	 * @param method method name.
	 */
	protected void addGetter(String attributeName, Method method) {
		if (attributes == null) {
			attributes = new ArrayList<String>();
			methods = new HashMap<String, Method>();
		}
		attributes.add(attributeName);
		methods.put(attributeName, method);
	}
	
	/**
	 * Writes the header row to the underlying table writer.
	 */
	protected void writeHeaderRow() {
		String row[] = new String[attributes.size()];
		attributes.toArray(row);
		try {
			getWriter().printRow(row);
		} catch (IOException e) {
			throw new CsvException("Header row failed:", e);
		}
		headerRowWritten = true;
	}
	
	/**
	 * Returns the attribute name derived from method name
	 * @param m method
	 * @return attribute name
	 */
	protected String getAttributeName(Method m) {
		String rc = m.getName();
		rc = rc.startsWith("is") ? rc.substring(2) : rc.substring(3);
		rc = rc.substring(0, 1).toLowerCase() + rc.substring(1);
		return rc;
	}
	
	/**
	 * Copies the beans from the collection to this bean writer.
	 * @param collection collection that contains JavaBeans
	 * @return number of rows written
	 * @throws IOException when there is a problem with the writer.
	 */
	public int writeBeans(Collection<? extends T> collection) throws IOException {
		return writeBeans(collection.iterator());
	}

	/**
	 * Copies the beans from the collection to this bean writer.
	 * @param i iterator that delivers JavaBeans
	 * @return number of rows written
	 * @throws IOException when there is a problem with the writer.
	 */
	public int writeBeans(Iterator<? extends T> i) throws IOException {
		int rc = 0;
		while (i.hasNext()) {
			writeBean(i.next());
			rc++;
		}
		return rc;
	}
	
	/**
	 * Copies the beans from the collection to this bean writer.
	 * @param arr array with beans
	 * @return number of rows written
	 * @throws IOException when there is a problem with the writer.
	 */
	@SuppressWarnings("unchecked")
	public int writeBeans(Object arr[]) throws IOException {
		int rc = 0;
		for (Object bean : arr) {
			writeBean((T)bean);
			rc++;
		}
		return rc;
	}
	

}
