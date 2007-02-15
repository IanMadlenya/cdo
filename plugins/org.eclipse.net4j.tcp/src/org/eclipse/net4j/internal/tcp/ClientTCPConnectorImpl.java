/***************************************************************************
 * Copyright (c) 2004-2007 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.net4j.internal.tcp;

import org.eclipse.net4j.transport.ConnectorLocation;

import org.eclipse.internal.net4j.bundle.Net4j;
import org.eclipse.internal.net4j.transport.DescriptionUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;

/**
 * @author Eike Stepper
 */
public class ClientTCPConnectorImpl extends AbstractTCPConnector
{
  public ClientTCPConnectorImpl()
  {
    try
    {
      SocketChannel socketChannel = SocketChannel.open();
      socketChannel.configureBlocking(false);
      setSocketChannel(socketChannel);
    }
    catch (IOException ex)
    {
      Net4j.LOG.error(ex);
    }
  }

  public ConnectorLocation getLocation()
  {
    return ConnectorLocation.CLIENT;
  }

  @Override
  public String toString()
  {
    return MessageFormat.format("ClientTCPConnector[{0}]", getDescription()); //$NON-NLS-1$ 
  }

  @Override
  protected void onAboutToActivate() throws Exception
  {
    super.onAboutToActivate();
    if (getDescription() == null)
    {
      throw new IllegalStateException("getDescription() == null"); //$NON-NLS-1$
    }
  }

  @Override
  protected void onActivate() throws Exception
  {
    super.onActivate();

    String[] elements = DescriptionUtil.getElements(getDescription());
    String host = elements[1];
    int port = Integer.parseInt(elements[2]);

    InetAddress addr = InetAddress.getByName(host);
    InetSocketAddress sAddr = new InetSocketAddress(addr, port);
    getSocketChannel().connect(sAddr);
  }
}
