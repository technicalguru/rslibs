/*
 * This file is part of RS Library (Base Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.baselib.lang;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reflection utilities.
 * @author ralph
 * @since 1.2.9
 */
public class ReflectionUtils {

	/**
	 * Replacement for "instanceof" operator when it cannot be guaranteed that
	 * the class is available in classpath at runtime.
	 * @param className the complete class name
	 * @return when the object is of that class
	 */
	public static boolean isInstanceOf(Object o, String className) {
		if (o == null) return false;
		Class<?> clazz = o.getClass();
		return isInstanceOf(clazz, className);
	}
	
	/**
	 * Replacement for {@link Class#isAssignableFrom(Class)} when it cannot be guaranteed that
	 * the class is available in classpath at runtime.
	 * @param inspectedClass the class to be checked
	 * @param className the complete class name that should be implemented or a superclass of the inspected class
	 * @return when the inspected class implements or derived from the class with given name
	 */
	public static boolean isInstanceOf(Class<?> inspectedClass, String className) {
		// Check type of class
		if (inspectedClass.getName().equals(className)) return true;
		
		// Check all interfaces
		for (Class<?> i : inspectedClass.getInterfaces()) {
			boolean rc = isInstanceOf(i, className);
			if (rc) return true;
		}
		
		// check superclass
		Class<?> parent = inspectedClass.getSuperclass();
		if (parent != null) return isInstanceOf(parent, className);
		
		return false;
	}

	/**
	 * Get the underlying class for a type, or null if the type is a variable type.
	 * @param type the type
	 * @return the underlying class
	 */
	public static Class<?> getClass(Type type) {
		if (type instanceof Class) {
			return (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			return getClass(((ParameterizedType) type).getRawType());
		} else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) type).getGenericComponentType();
			Class<?> componentClass = getClass(componentType);
			if (componentClass != null ) {
				return Array.newInstance(componentClass, 0).getClass();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Get the actual type arguments a child class has used to extend a generic base class.
	 *
	 * @param baseClass the base class
	 * @param childClass the child class
	 * @return a list of the raw classes for the actual type arguments.
	 */
	public static <T> List<Class<?>> getTypeArguments(Class<T> baseClass, Class<? extends T> childClass) {
		Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
		Type type = childClass;
		
		// start walking up the inheritance hierarchy until we hit baseClass
		Class<?> typeClass = getClass(type);
		while ((typeClass != null) && !typeClass.equals(baseClass)) {
			if (type instanceof Class) {
				// there is no useful information for us in raw types, so just keep going.
				type = ((Class<?>) type).getGenericSuperclass();
			} else {
				ParameterizedType parameterizedType = (ParameterizedType) type;
				Class<?> rawType = (Class<?>) parameterizedType.getRawType();

				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
				for (int i = 0; i < actualTypeArguments.length; i++) {
					resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
				}

				if (!rawType.equals(baseClass)) {
					type = rawType.getGenericSuperclass();
				}
			}
			typeClass = getClass(type);
		}

		// finally, for each actual type argument provided to baseClass, determine (if possible)
		// the raw class for that type argument.
		Type[] actualTypeArguments;
		if (type instanceof Class) {
			actualTypeArguments = ((Class<?>) type).getTypeParameters();
		} else {
			actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
		}
		List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
		// resolve types by chasing down type variables.
		for (Type baseType: actualTypeArguments) {
			while (resolvedTypes.containsKey(baseType)) {
				baseType = resolvedTypes.get(baseType);
			}
			typeArgumentsAsClasses.add(getClass(baseType));
		}
		return typeArgumentsAsClasses;
	}

	/**
	 * Returns whether member is declared public.
	 * @param member member to be inspected
	 * @return <code>true</code> when member was declared public
	 */
	public static boolean isPublic(Member member) {
		return (member.getModifiers() & Modifier.PUBLIC) != 0;
	}

	/**
	 * Returns whether member is declared protected.
	 * @param member member to be inspected
	 * @return <code>true</code> when member was declared protected
	 */
	public static boolean isProtected(Member member) {
		return (member.getModifiers() & Modifier.PROTECTED) != 0;
	}
	
	/**
	 * Returns whether member is declared private.
	 * @param member member to be inspected
	 * @return <code>true</code> when member was declared private
	 */
	public static boolean isPrivate(Member member) {
		return (member.getModifiers() & Modifier.PRIVATE) != 0;
	}

	/**
	 * Returns whether member is declared static.
	 * @param member member to be inspected
	 * @return <code>true</code> when member was declared static
	 */
	public static boolean isStatic(Member member) {
		return (member.getModifiers() & Modifier.STATIC) != 0;
	}

	/**
	 * Returns whether member is declared final.
	 * @param member member to be inspected
	 * @return <code>true</code> when member was declared final
	 */
	public static boolean isFinal(Member member) {
		return (member.getModifiers() & Modifier.FINAL) != 0;
	}

	/**
	 * Returns whether member is declared volatile.
	 * @param member member to be inspected
	 * @return <code>true</code> when member was declared volatile
	 */
	public static boolean isVolatile(Member member) {
		return (member.getModifiers() & Modifier.VOLATILE) != 0;
	}

	/**
	 * Returns whether member is declared synchronized.
	 * @param member member to be inspected
	 * @return <code>true</code> when member was declared synchronized
	 */
	public static boolean isSynchronized(Member member) {
		return (member.getModifiers() & Modifier.SYNCHRONIZED) != 0;
	}

	/**
	 * Returns whether member is declared abstract.
	 * @param member member to be inspected
	 * @return <code>true</code> when member was declared abstract
	 */
	public static boolean isAbstract(Member member) {
		return (member.getModifiers() & Modifier.ABSTRACT) != 0;
	}
	
	/**
	 * Returns whether member is declared native.
	 * @param member member to be inspected
	 * @return <code>true</code> when member was declared native
	 */
	public static boolean isNative(Member member) {
		return (member.getModifiers() & Modifier.NATIVE) != 0;
	}
	
	/**
	 * Returns whether member is declared transient.
	 * @param member member to be inspected
	 * @return <code>true</code> when member was declared transient
	 */
	public static boolean isTransient(Member member) {
		return (member.getModifiers() & Modifier.TRANSIENT) != 0;
	}
	
	/**
	 * Returns whether member is declared strict.
	 * @param member member to be inspected
	 * @return <code>true</code> when member was declared strict
	 */
	public static boolean isStrict(Member member) {
		return (member.getModifiers() & Modifier.STRICT) != 0;
	}
}
