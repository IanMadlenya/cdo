/*
 * Copyright (c) 2013 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.releng.workingsets.util;

import org.eclipse.emf.cdo.releng.workingsets.NamePredicate;
import org.eclipse.emf.cdo.releng.workingsets.Predicate;
import org.eclipse.emf.cdo.releng.workingsets.WorkingSet;
import org.eclipse.emf.cdo.releng.workingsets.WorkingSetGroup;
import org.eclipse.emf.cdo.releng.workingsets.WorkingSetsPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.cdo.releng.workingsets.WorkingSetsPackage
 * @generated
 */
public class WorkingSetsSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static WorkingSetsPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WorkingSetsSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = WorkingSetsPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @parameter ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
    case WorkingSetsPackage.WORKING_SET:
    {
      WorkingSet workingSet = (WorkingSet)theEObject;
      T result = caseWorkingSet(workingSet);
      if (result == null)
        result = defaultCase(theEObject);
      return result;
    }
    case WorkingSetsPackage.WORKING_SET_GROUP:
    {
      WorkingSetGroup workingSetGroup = (WorkingSetGroup)theEObject;
      T result = caseWorkingSetGroup(workingSetGroup);
      if (result == null)
        result = defaultCase(theEObject);
      return result;
    }
    case WorkingSetsPackage.PREDICATE:
    {
      Predicate predicate = (Predicate)theEObject;
      T result = casePredicate(predicate);
      if (result == null)
        result = defaultCase(theEObject);
      return result;
    }
    case WorkingSetsPackage.NAME_PREDICATE:
    {
      NamePredicate namePredicate = (NamePredicate)theEObject;
      T result = caseNamePredicate(namePredicate);
      if (result == null)
        result = casePredicate(namePredicate);
      if (result == null)
        result = defaultCase(theEObject);
      return result;
    }
    default:
      return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Working Set</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Working Set</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseWorkingSet(WorkingSet object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Working Set Group</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Working Set Group</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseWorkingSetGroup(WorkingSetGroup object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePredicate(Predicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Name Predicate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Name Predicate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNamePredicate(NamePredicate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} // WorkingSetsSwitch