/*
 * Copyright (c) 2007, 2010-2012 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.internal.util.om.pref;

/**
 * @author Eike Stepper
 */
public final class StringPreference extends Preference<String>
{
  public StringPreference(Preferences preferences, String name, String defaultValue)
  {
    super(preferences, name, defaultValue);
  }

  @Override
  protected String getString()
  {
    return getValue();
  }

  @Override
  protected String convert(String value)
  {
    return value;
  }

  public Type getType()
  {
    return Type.STRING;
  }
}
