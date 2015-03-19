/*
 * Copyright (c) 2010-2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.explorer.repositories;

import org.eclipse.emf.cdo.common.CDOCommonRepository.IDGenerationLocation;
import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.explorer.CDOExplorerElement;
import org.eclipse.emf.cdo.explorer.checkouts.CDOCheckout;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.session.CDOSessionProvider;
import org.eclipse.emf.cdo.transaction.CDOTransactionOpener;
import org.eclipse.emf.cdo.view.CDOViewOpener;

import org.eclipse.net4j.util.container.IContainer;

/**
 * A CDO server independent representation of a repository.
 *
 * @author Eike Stepper
 * @since 4.4
 */
public interface CDORepository extends CDOExplorerElement, IContainer<CDOBranch>, CDOSessionProvider, CDOViewOpener,
    CDOTransactionOpener
{
  public static final String TYPE_REMOTE = "remote";

  public static final String TYPE_CLONE = "clone";

  public static final String TYPE_LOCAL = "local";

  public boolean isRemote();

  public boolean isClone();

  public boolean isLocal();

  public String getConnectorType();

  public String getConnectorDescription();

  public String getName();

  public String getURI();

  public VersioningMode getVersioningMode();

  public IDGeneration getIDGeneration();

  public State getState();

  public boolean isConnected();

  public void connect();

  public void disconnect();

  public CDOCheckout[] getCheckouts();

  public CDOSession getSession();

  public CDOSession acquireSession();

  public void releaseSession();

  /**
   * @author Eike Stepper
   */
  public enum VersioningMode
  {
    Normal(false, false), Auditing(true, false), Branching(true, true);

    private boolean supportingAudits;

    private boolean supportingBranches;

    private VersioningMode(boolean supportingAudits, boolean supportingBranches)
    {
      this.supportingAudits = supportingAudits;
      this.supportingBranches = supportingBranches;
    }

    public boolean isSupportingAudits()
    {
      return supportingAudits;
    }

    public boolean isSupportingBranches()
    {
      return supportingBranches;
    }
  }

  /**
   * @author Eike Stepper
   */
  public enum IDGeneration
  {
    Counter(IDGenerationLocation.STORE), UUID(IDGenerationLocation.CLIENT);

    private IDGenerationLocation location;

    private IDGeneration(IDGenerationLocation location)
    {
      this.location = location;
    }

    public final IDGenerationLocation getLocation()
    {
      return location;
    }
  }

  /**
   * @author Eike Stepper
   */
  public enum State
  {
    Connecting, Connected, Disconnecting, Disconnected
  }
}