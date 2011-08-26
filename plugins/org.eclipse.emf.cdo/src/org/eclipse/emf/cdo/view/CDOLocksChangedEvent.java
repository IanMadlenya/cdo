/**
 * Copyright (c) 2004 - 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Caspar De Groot - initial API and implementation
 */
package org.eclipse.emf.cdo.view;

import org.eclipse.emf.cdo.common.lock.CDOLockChangeInfo;

/**
 * A {@link CDOViewEvent view event} fired when lock notifications are being received from a repository.
 * {@link CDOView.Options#setLockNotificationEnabled(boolean)} must be enabled for this event to be fired.
 * 
 * @author Caspar De Groot
 * @since 4.1
 */
public interface CDOLocksChangedEvent extends CDOViewEvent, CDOLockChangeInfo
{
}