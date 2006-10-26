/***************************************************************************
 * Copyright (c) 2004, 2005, 2006 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.net4j.util.om;

/**
 * @author Eike Stepper
 */
public interface OMTracer
{
  public OMBundle getBundle();

  public OMTracer getParent();

  public String getName();

  public String getFullName();

  public boolean isEnabled();

  public void setEnabled(boolean enabled);

  public void trace(Class context, String pattern, Object... args);

  public void trace(Class context, String pattern, Throwable t, Object... args);

  public void trace(Class context, String msg, Throwable t);

  public void trace(Class context, String msg);

  public void trace(Class context, Throwable t);

  public OMTracer tracer(String name);

}