/**
 * Copyright (c) 2004 - 2009 Andre Dietisheim (Bern, Switzerland) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Dietisheim - initial API and implementation
 */
package org.eclipse.emf.cdo.tests.revisioncache;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Andre Dietisheim
 */
public class AllCDORevisionCacheTest
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Tests for CDORevisionCache"); //$NON-NLS-1$

    // $JUnit-BEGIN$
    suite.addTestSuite(DerbyDBRevisionCacheTest.class);
    suite.addTestSuite(H2DBRevisionCacheTest.class);
    suite.addTestSuite(MEMRevisionCacheTest.class);
    suite.addTestSuite(LRURevisionCacheTest.class);
    suite.addTestSuite(DefaultRevisionCacheTest.class);
    // $JUnit-END$

    return suite;
  }
}
