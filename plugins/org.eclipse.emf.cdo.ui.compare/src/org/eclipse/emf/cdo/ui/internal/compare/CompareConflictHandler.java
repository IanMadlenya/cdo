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
package org.eclipse.emf.cdo.ui.internal.compare;

import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.ui.compare.CDOCompareEditorUtil;
import org.eclipse.emf.cdo.view.CDOView;

import org.eclipse.emf.internal.cdo.transaction.CDOHandlingConflictResolver.ConflictHandler;

import org.eclipse.net4j.util.factory.ProductCreationException;

import org.eclipse.emf.spi.cdo.CDOMergingConflictResolver;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class CompareConflictHandler implements ConflictHandler
{
  public CompareConflictHandler()
  {
  }

  public String getLabel()
  {
    return "Merge";
  }

  public int getPriority()
  {
    return DEFAULT_PRIORITY - 100;
  }

  public boolean canHandleConflict(CDOMergingConflictResolver conflictResolver, long lastNonConflictTimeStamp)
  {
    CDOTransaction transaction = conflictResolver.getTransaction();
    return transaction.getSession().getRepositoryInfo().isSupportingAudits();
  }

  public boolean handleConflict(CDOMergingConflictResolver conflictResolver, long lastNonConflictTimeStamp)
  {
    CDOTransaction transaction = conflictResolver.getTransaction();
    CDOView remoteView = transaction.getSession().openView(transaction);

    try
    {
      CDOCompareEditorUtil.setSuppressCommit(true);
      return CDOCompareEditorUtil.openDialog(remoteView, transaction, (CDOView[])null);
    }
    finally
    {
      remoteView.close();
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Factory extends ConflictHandler.Factory
  {
    public static final String TYPE = "merge";

    public Factory()
    {
      super(TYPE);
    }

    @Override
    public ConflictHandler create(String description) throws ProductCreationException
    {
      return new CompareConflictHandler();
    }
  }
}
