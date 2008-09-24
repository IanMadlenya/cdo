/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.internal.cdo.protocol;

import org.eclipse.emf.cdo.common.CDODataInput;
import org.eclipse.emf.cdo.common.CDODataOutput;
import org.eclipse.emf.cdo.common.CDOProtocolConstants;

import org.eclipse.emf.internal.cdo.InternalCDOObject;
import org.eclipse.emf.internal.cdo.bundle.OM;

import org.eclipse.net4j.channel.IChannel;
import org.eclipse.net4j.util.om.trace.ContextTracer;

import java.io.IOException;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class SetAuditRequest extends CDOClientRequest<boolean[]>
{
  private static final ContextTracer PROTOCOL_TRACER = new ContextTracer(OM.DEBUG_PROTOCOL, SetAuditRequest.class);

  private int viewID;

  private long timeStamp;

  private List<InternalCDOObject> invalidObjects;

  public SetAuditRequest(IChannel channel, int viewID, long timeStamp, List<InternalCDOObject> invalidObjects)
  {
    super(channel);
    this.viewID = viewID;
    this.timeStamp = timeStamp;
    this.invalidObjects = invalidObjects;
  }

  @Override
  protected short getSignalID()
  {
    return CDOProtocolConstants.SIGNAL_SET_AUDIT;
  }

  @Override
  protected void requesting(CDODataOutput out) throws IOException
  {
    if (PROTOCOL_TRACER.isEnabled())
    {
      PROTOCOL_TRACER.format("Writing viewID: {0}", viewID);
    }

    out.writeInt(viewID);
    if (PROTOCOL_TRACER.isEnabled())
    {
      PROTOCOL_TRACER.format("Writing timeStamp: {0,date} {0,time}", timeStamp);
    }

    out.writeLong(timeStamp);

    int size = invalidObjects.size();
    if (PROTOCOL_TRACER.isEnabled())
    {
      PROTOCOL_TRACER.format("Writing {0} IDs", size);
    }

    out.writeInt(size);
    for (InternalCDOObject object : invalidObjects)
    {
      if (PROTOCOL_TRACER.isEnabled())
      {
        PROTOCOL_TRACER.format("Writing ID: {0}", object.cdoID());
      }

      out.writeCDOID(object.cdoID());
    }
  }

  @Override
  protected boolean[] confirming(CDODataInput in) throws IOException
  {
    int size = in.readInt();
    if (PROTOCOL_TRACER.isEnabled())
    {
      PROTOCOL_TRACER.format("Reading {0} existanceFlags", size);
    }

    boolean[] existanceFlags = new boolean[size];
    for (int i = 0; i < size; i++)
    {
      boolean existanceFlag = in.readBoolean();
      existanceFlags[i] = existanceFlag;
      if (PROTOCOL_TRACER.isEnabled())
      {
        PROTOCOL_TRACER.format("Read existanceFlag: {0}", existanceFlag);
      }
    }

    return existanceFlags;
  }
}
