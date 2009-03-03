/**
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.common.model;

import org.eclipse.emf.cdo.common.io.CDODataInput;
import org.eclipse.emf.cdo.common.io.CDODataOutput;
import org.eclipse.emf.cdo.common.model.EMFUtil;
import org.eclipse.emf.cdo.internal.common.bundle.OM;
import org.eclipse.emf.cdo.spi.common.model.InternalCDOPackageInfo;
import org.eclipse.emf.cdo.spi.common.model.InternalCDOPackageRegistry;
import org.eclipse.emf.cdo.spi.common.model.InternalCDOPackageUnit;

import org.eclipse.net4j.util.om.trace.ContextTracer;

import org.eclipse.emf.ecore.EPackage;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class CDOPackageUnitImpl implements InternalCDOPackageUnit
{
  private static final ContextTracer TRACER = new ContextTracer(OM.DEBUG, CDOPackageUnitImpl.class);

  private InternalCDOPackageRegistry packageRegistry;

  private State state = State.NOT_LOADED;

  private boolean dynamic;

  private boolean legacy;

  private long timeStamp;

  private InternalCDOPackageInfo[] packageInfos;

  public CDOPackageUnitImpl()
  {
  }

  public InternalCDOPackageRegistry getPackageRegistry()
  {
    return packageRegistry;
  }

  public void setPackageRegistry(InternalCDOPackageRegistry packageRegistry)
  {
    this.packageRegistry = packageRegistry;
  }

  public String getID()
  {
    return packageInfos == null ? "" : packageInfos[0].getPackageURI();
  }

  public long getTimeStamp()
  {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp)
  {
    this.timeStamp = timeStamp;
  }

  public boolean isDynamic()
  {
    checkLoaded();
    return dynamic;
  }

  public boolean isLegacy()
  {
    checkLoaded();
    return legacy;
  }

  public boolean isSystem()
  {
    return packageInfos == null ? false : packageInfos[0].isSystemPackage();
  }

  public InternalCDOPackageInfo getPackageInfo(String packageURI)
  {
    for (InternalCDOPackageInfo packageInfo : packageInfos)
    {
      if (packageInfo.getPackageURI().equals(packageURI))
      {
        return packageInfo;
      }
    }

    return null;
  }

  public InternalCDOPackageInfo[] getPackageInfos()
  {
    return packageInfos;
  }

  public State getState()
  {
    return state;
  }

  public void setState(State state)
  {
    this.state = state;
  }

  public void init(EPackage ePackage)
  {
    EPackage topLevelPackage = EMFUtil.getTopLevelPackage(ePackage);
    List<InternalCDOPackageInfo> result = new ArrayList<InternalCDOPackageInfo>();
    initPackageInfos(topLevelPackage, result);
    packageInfos = result.toArray(new InternalCDOPackageInfo[result.size()]);

    state = State.NEW;
    dynamic = EMFUtil.isDynamicEPackage(topLevelPackage);
    legacy = false;
  }

  public synchronized void load()
  {
    if (state == State.NOT_LOADED)
    {
      EPackage[] ePackages = loadPackagesFromGlobalRegistry();
      if (ePackages == null)
      {
        ePackages = packageRegistry.getPackageLoader().loadPackages(this);
      }

      for (EPackage ePackage : ePackages)
      {
        String packageURI = ePackage.getNsURI();
        InternalCDOPackageInfo packageInfo = getPackageInfo(packageURI);
        EMFUtil.addAdapter(ePackage, packageInfo);
      }

      state = State.LOADED;
      dynamic = EMFUtil.isDynamicEPackage(ePackages[0]);
      legacy = false;
    }
  }

  public void write(CDODataOutput out, boolean withPackages) throws IOException
  {
    if (TRACER.isEnabled())
    {
      TRACER.format("Writing {0}", this);
    }

    out.writeBoolean(withPackages);
    if (withPackages)
    {
      EMFUtil.writePackage(out, packageInfos[0].getEPackage(), true);
    }

    out.writeLong(timeStamp);
    out.writeInt(packageInfos.length);
    for (InternalCDOPackageInfo packageInfo : packageInfos)
    {
      out.writeCDOPackageInfo(packageInfo);
    }
  }

  public void read(CDODataInput in) throws IOException
  {
    EPackage ePackage = null;
    boolean withPackages = in.readBoolean();
    if (withPackages)
    {
      ePackage = EMFUtil.readPackage(in);
      state = State.LOADED;
    }

    timeStamp = in.readLong();
    packageInfos = new InternalCDOPackageInfo[in.readInt()];
    for (int i = 0; i < packageInfos.length; i++)
    {
      packageInfos[i] = (InternalCDOPackageInfo)in.readCDOPackageInfo();
      packageInfos[i].setPackageUnit(this);
    }

    if (ePackage != null)
    {
      attachPackageInfos(ePackage);
    }

    if (TRACER.isEnabled())
    {
      TRACER.format("Read {0}", this);
    }
  }

  @Override
  public String toString()
  {
    return MessageFormat.format("CDOPackageUnit[id={0}, state={1}, timeStamp={2,date} {2,time}]", getID(), getState(),
        timeStamp);
  }

  private void initPackageInfos(EPackage ePackage, List<InternalCDOPackageInfo> result)
  {
    CDOPackageInfoImpl packageInfo = new CDOPackageInfoImpl();
    packageInfo.setPackageUnit(this);
    packageInfo.setPackageURI(ePackage.getNsURI());
    packageInfo.setParentURI(ePackage.getESuperPackage() == null ? null : ePackage.getESuperPackage().getNsURI());

    result.add(packageInfo);
    EMFUtil.addAdapter(ePackage, packageInfo);
    packageRegistry.basicPut(ePackage.getNsURI(), ePackage);

    for (EPackage subPackage : ePackage.getESubpackages())
    {
      initPackageInfos(subPackage, result);
    }
  }

  private void attachPackageInfos(EPackage ePackage)
  {
    InternalCDOPackageInfo packageInfo = getPackageInfo(ePackage.getNsURI());
    if (packageInfo != null)
    {
      EMFUtil.addAdapter(ePackage, packageInfo);
    }

    for (EPackage subPackage : ePackage.getESubpackages())
    {
      attachPackageInfos(subPackage);
    }
  }

  private EPackage[] loadPackagesFromGlobalRegistry()
  {
    EPackage[] ePackages = new EPackage[packageInfos.length];
    for (int i = 0; i < ePackages.length; i++)
    {
      ePackages[i] = EPackage.Registry.INSTANCE.getEPackage(packageInfos[i].getPackageURI());
      if (ePackages[i] == null)
      {
        return null;
      }
    }

    return ePackages;
  }

  private void checkLoaded()
  {
    if (state == State.NOT_LOADED)
    {
      throw new IllegalStateException("Package unit not loaded: " + this);
    }
  }
}
