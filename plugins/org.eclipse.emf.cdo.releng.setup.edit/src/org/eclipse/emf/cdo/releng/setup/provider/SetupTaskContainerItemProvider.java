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
package org.eclipse.emf.cdo.releng.setup.provider;

import org.eclipse.emf.cdo.releng.setup.SetupFactory;
import org.eclipse.emf.cdo.releng.setup.SetupPackage;
import org.eclipse.emf.cdo.releng.setup.SetupTaskContainer;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.cdo.releng.setup.SetupTaskContainer} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SetupTaskContainerItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider,
    IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupTaskContainerItemProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  /**
   * This returns the property descriptors for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
  {
    if (itemPropertyDescriptors == null)
    {
      super.getPropertyDescriptors(object);

    }
    return itemPropertyDescriptors;
  }

  /**
   * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
   * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
   * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
  {
    if (childrenFeatures == null)
    {
      super.getChildrenFeatures(object);
      childrenFeatures.add(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS);
    }
    return childrenFeatures;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EStructuralFeature getChildFeature(Object object, Object child)
  {
    // Check the type of the specified child object and return the proper feature to use for
    // adding (see {@link AddCommand}) it as a child.

    return super.getChildFeature(object, child);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean hasChildren(Object object)
  {
    return hasChildren(object, true);
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getText(Object object)
  {
    return getString("_UI_SetupTaskContainer_type");
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void notifyChanged(Notification notification)
  {
    updateChildren(notification);

    switch (notification.getFeatureID(SetupTaskContainer.class))
    {
    case SetupPackage.SETUP_TASK_CONTAINER__SETUP_TASKS:
      fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
      return;
    }
    super.notifyChanged(notification);
  }

  /**
   * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
   * that can be created under this object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createCompoundSetupTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createEclipseIniTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createLinkLocationTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createP2Task()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createBuckminsterImportTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createApiBaselineTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createGitCloneTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createEclipsePreferenceTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createStringVariableTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createWorkingSetTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createResourceCopyTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createTextModifyTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS,
        SetupFactory.eINSTANCE.createKeyBindingTask()));
  }

  /**
   * Return the resource locator for this item provider's resources.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourceLocator getResourceLocator()
  {
    return ((IChildCreationExtender)adapterFactory).getResourceLocator();
  }

}