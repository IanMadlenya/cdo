/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.explorer;

import org.eclipse.net4j.util.event.INotifier;

import org.eclipse.emf.common.notify.Adapter;

import org.eclipse.core.runtime.IAdaptable;

import java.io.File;

/**
 * A CDO server independent representation of a repository.
 *
 * @author Eike Stepper
 * @since 4.4
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface CDOExplorerElement extends INotifier, IAdaptable, Adapter, Comparable<CDOExplorerElement>
{
  public String getID();

  public String getType();

  public String getLabel();

  public void setLabel(String label);

  public String getDescription();

  public void setDescription(String description);

  /**
   * @since 4.5
   */
  public File getStateFolder(String path);

  public void delete(boolean deleteContents);
}
