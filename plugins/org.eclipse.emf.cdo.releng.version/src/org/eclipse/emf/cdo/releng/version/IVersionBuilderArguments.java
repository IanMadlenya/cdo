/*
 * Copyright (c) 2004 - 2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.releng.version;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import java.util.Map;

/**
 * @author Eike Stepper
 */
public interface IVersionBuilderArguments extends Map<String, String>
{
  public static final String DEFAULT_VALIDATOR_CLASS_NAME = "org.eclipse.emf.cdo.releng.version.digest.DigestValidator$BuildModel";

  public String getReleasePath();

  public boolean isIgnoreMissingDependencyRanges();

  public boolean isIgnoreMissingExportVersions();

  public boolean isIgnoreFeatureContentRedundancy();

  public boolean isIgnoreFeatureContentChanges();

  public void applyTo(IProject project) throws CoreException;
}