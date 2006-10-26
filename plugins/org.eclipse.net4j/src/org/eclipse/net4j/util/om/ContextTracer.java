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
public class ContextTracer
{
  private OMTracer delegate;

  private Class context;

  public ContextTracer(OMTracer delegate, Class context)
  {
    this.delegate = delegate;
    this.context = context;
  }

  public OMBundle getBundle()
  {
    return delegate.getBundle();
  }

  public String getFullName()
  {
    return delegate.getFullName();
  }

  public String getName()
  {
    return delegate.getName();
  }

  public OMTracer getParent()
  {
    return delegate.getParent();
  }

  public boolean isEnabled()
  {
    return delegate.isEnabled();
  }

  public void setEnabled(boolean enabled)
  {
    delegate.setEnabled(enabled);
  }

  public void trace(String msg, Throwable t)
  {
    delegate.trace(context, msg, t);
  }

  public void trace(String msg)
  {
    delegate.trace(context, msg);
  }

  public void trace(Throwable t)
  {
    delegate.trace(context, t);
  }
}
