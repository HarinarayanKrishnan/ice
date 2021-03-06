/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.junit.Test;

/**
 * <p>
 * A class that tests the BasicEntryContentProvider.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class BasicEntryContentProviderTester {
	/**
	 * <p>
	 * Checks the constructor.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {
		// Local Declarations
		BasicEntryContentProvider contentProvider = new BasicEntryContentProvider();

		// Check basic information
		assertEquals("ICE Object", contentProvider.getName());
		assertEquals("ICE Object", contentProvider.getDescription());
		assertEquals(1, contentProvider.getId());

		// Check default data on provider
		assertEquals(0, contentProvider.getAllowedValues().size());
		assertEquals("orphan", contentProvider.getParent());
		assertEquals(null, contentProvider.getTag());
		assertEquals("", contentProvider.getDefaultValue());
		assertEquals(AllowedValueType.Undefined,
				contentProvider.getAllowedValueType());

	}

	/**
	 * <p>
	 * Checks the valid and invalid use of getters and setters.
	 * </p>
	 * 
	 */
	@Test
	public void checkGettersAndSetters() {
		// Local Declarations
		BasicEntryContentProvider contentProvider = new BasicEntryContentProvider();
		ArrayList<String> goodValues = new ArrayList<String>();
		ArrayList<String> emptyValues = new ArrayList<String>();

		// Check basic information
		assertEquals("ICE Object", contentProvider.getName());
		assertEquals("ICE Object", contentProvider.getDescription());
		assertEquals(1, contentProvider.getId());

		// Check default data on provider
		assertEquals(0, contentProvider.getAllowedValues().size());
		assertEquals("orphan", contentProvider.getParent());
		assertEquals(null, contentProvider.getTag());
		assertEquals("", contentProvider.getDefaultValue());
		assertEquals(AllowedValueType.Undefined,
				contentProvider.getAllowedValueType());

		// Now, try to set some data
		// Valid and invalid allowedValues
		goodValues.add("2");
		goodValues.add("5");
		contentProvider.setAllowedValues(goodValues);
		assertEquals(goodValues, contentProvider.getAllowedValues());

		// Pass null - values do not change
		contentProvider.setAllowedValues(null);
		assertEquals(goodValues, contentProvider.getAllowedValues());

		// Pass the empty string - works
		contentProvider.setAllowedValues(emptyValues);
		assertEquals(emptyValues, contentProvider.getAllowedValues());

		// Parent
		contentProvider.setParent("nonOrphan!");
		assertEquals("nonOrphan!", contentProvider.getParent());

		// Cannot be set to null
		contentProvider.setParent(null);
		assertEquals("nonOrphan!", contentProvider.getParent());

		// Can be set back to orphan - default parentless Entry
		contentProvider.setParent("orphan");
		assertEquals("orphan", contentProvider.getParent());

		// Tag
		contentProvider.setTag("tagoramma");
		assertEquals("tagoramma", contentProvider.getTag());

		// Can be set to null
		contentProvider.setTag(null);
		assertEquals(null, contentProvider.getTag());

		// AllowedValueType
		// Notice that the allowedValueType CAN CHANGE without the hinderance.
		// So note that undefined entries can have allowedvalues, which is
		// invalid.
		// Also note that continuous and discrete values can have zero
		// allowedValues,
		// which is invalid (or continuous with allowedValues != 2 or correctly
		// paired).
		contentProvider.setAllowedValueType(AllowedValueType.Discrete);
		assertEquals(AllowedValueType.Discrete,
				contentProvider.getAllowedValueType());

		contentProvider.setAllowedValueType(AllowedValueType.Continuous);
		assertEquals(AllowedValueType.Continuous,
				contentProvider.getAllowedValueType());

		contentProvider.setAllowedValueType(AllowedValueType.Undefined);
		assertEquals(AllowedValueType.Undefined,
				contentProvider.getAllowedValueType());

		// Try to set it to null - does not change
		contentProvider.setAllowedValueType(null);
		assertEquals(AllowedValueType.Undefined,
				contentProvider.getAllowedValueType());

	}

	/**
	 * <p>
	 * Checks the equality (equals and hashCode) operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {
		// Local Declarations
		BasicEntryContentProvider contentProvider, equalContentProvider, unEqualContentProvider, transitiveContentProvider = null;
		ArrayList<String> allowedValues = new ArrayList<String>();

		allowedValues.add("3");
		allowedValues.add("5");

		contentProvider = new BasicEntryContentProvider();
		equalContentProvider = new BasicEntryContentProvider();
		transitiveContentProvider = new BasicEntryContentProvider();
		unEqualContentProvider = new BasicEntryContentProvider();

		// Set type
		contentProvider.setAllowedValueType(AllowedValueType.Continuous);
		transitiveContentProvider
				.setAllowedValueType(AllowedValueType.Continuous);
		equalContentProvider.setAllowedValueType(AllowedValueType.Continuous);
		unEqualContentProvider.setAllowedValueType(AllowedValueType.Discrete);

		// Set AllowedValues
		contentProvider.setAllowedValues(allowedValues);
		equalContentProvider.setAllowedValues(allowedValues);
		transitiveContentProvider.setAllowedValues(allowedValues);
		unEqualContentProvider.setAllowedValues(new ArrayList<String>()); // Discrete
																			// and
																			// empty
																			// allowedValues

		// Assert two equal providers return true
		assertTrue(contentProvider.equals(equalContentProvider));

		// Assert two unequal providers return false
		assertFalse(contentProvider.equals(unEqualContentProvider));

		// Assert equals() is reflexive
		assertTrue(contentProvider.equals(contentProvider));

		// Assert the equals() is Symmetric
		assertTrue(contentProvider.equals(equalContentProvider)
				&& equalContentProvider.equals(contentProvider));

		// Assert equals() is transitive
		if (contentProvider.equals(equalContentProvider)
				&& equalContentProvider.equals(transitiveContentProvider)) {
			assertTrue(contentProvider.equals(transitiveContentProvider));
		} else {
			fail();
		}

		// Assert equals is consistent
		assertTrue(contentProvider.equals(equalContentProvider)
				&& contentProvider.equals(equalContentProvider)
				&& contentProvider.equals(equalContentProvider));
		assertTrue(!contentProvider.equals(unEqualContentProvider)
				&& !contentProvider.equals(unEqualContentProvider)
				&& !contentProvider.equals(unEqualContentProvider));

		// Assert checking equality with null is false
		assertFalse(contentProvider==null);

		// Assert that two equal objects return same hashcode
		assertTrue(contentProvider.equals(equalContentProvider)
				&& contentProvider.hashCode() == equalContentProvider
						.hashCode());

		// Assert that hashcode is consistent
		assertTrue(contentProvider.hashCode() == contentProvider.hashCode());

		// Assert that hashcodes from unequal objects are different
		assertTrue(contentProvider.hashCode() != unEqualContentProvider
				.hashCode());

	}

	/**
	 * <p>
	 * Checks the copy and clone operations().
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {
		// Local Declarations
		BasicEntryContentProvider contentProvider, contentProviderCopy, contentProviderClone = null;
		ArrayList<String> allowedValues = new ArrayList<String>();

		allowedValues.add("3");
		allowedValues.add("5");

		contentProviderCopy = new BasicEntryContentProvider();
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setAllowedValues(allowedValues);
		contentProvider.setAllowedValueType(AllowedValueType.Continuous);

		contentProviderCopy.copy(contentProvider);

		// Check contents
		assertTrue(contentProvider.equals(contentProviderCopy));

		// Test to show valid usage of clone

		// Run clone operation
		contentProviderClone = (BasicEntryContentProvider) contentProvider
				.clone();

		// Check contents
		assertTrue(contentProvider.equals(contentProviderClone));

		// Call copy with null, which should not change anything
		contentProvider.copy(null);

		// Check contents - nothing has changed
		assertTrue(contentProvider.equals(contentProviderCopy));

	}

	/**
	 * Checks the xml persistence (load/persist) operations.
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 */
	@Test
	public void checkXMLPersistence() throws NullPointerException, JAXBException, IOException {

		// Local Declarations
		BasicEntryContentProvider persistProvider, loadedProvider = null;
		ArrayList<String> allowedValues = new ArrayList<String>();
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(BasicEntryContentProvider.class);

		allowedValues.add("3");
		allowedValues.add("5");

		persistProvider = new BasicEntryContentProvider();
		persistProvider.setAllowedValues(allowedValues);
		persistProvider.setAllowedValueType(AllowedValueType.Continuous);
		persistProvider.setTag("tagorama");

		// Demonstrate a basic "write" to file. Should not fail

		// persist to an output stream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(persistProvider, classList, outputStream);

		// Demonstrate a basic read in. Create file in memory and convert to an
		// inputstream.
		InputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Initialize object and pass inputStream to read()
		loadedProvider = new BasicEntryContentProvider();
		loadedProvider = (BasicEntryContentProvider) xmlHandler.read(classList, inputStream);

		// Check contents
		assertTrue(persistProvider.equals(loadedProvider));

	}
}