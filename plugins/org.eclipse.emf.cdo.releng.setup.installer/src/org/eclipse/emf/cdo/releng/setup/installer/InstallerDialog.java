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
package org.eclipse.emf.cdo.releng.setup.installer;

import org.eclipse.emf.cdo.releng.internal.setup.SetupTaskMigrator;
import org.eclipse.emf.cdo.releng.internal.setup.SetupTaskPerformer;
import org.eclipse.emf.cdo.releng.internal.setup.ui.AbstractSetupDialog;
import org.eclipse.emf.cdo.releng.internal.setup.ui.ErrorDialog;
import org.eclipse.emf.cdo.releng.internal.setup.ui.ProgressLogDialog;
import org.eclipse.emf.cdo.releng.setup.Branch;
import org.eclipse.emf.cdo.releng.setup.Configuration;
import org.eclipse.emf.cdo.releng.setup.Eclipse;
import org.eclipse.emf.cdo.releng.setup.Preferences;
import org.eclipse.emf.cdo.releng.setup.Project;
import org.eclipse.emf.cdo.releng.setup.Setup;
import org.eclipse.emf.cdo.releng.setup.SetupFactory;
import org.eclipse.emf.cdo.releng.setup.SetupPackage;
import org.eclipse.emf.cdo.releng.setup.provider.BranchItemProvider;
import org.eclipse.emf.cdo.releng.setup.provider.ConfigurationItemProvider;
import org.eclipse.emf.cdo.releng.setup.provider.ProjectItemProvider;
import org.eclipse.emf.cdo.releng.setup.provider.SetupItemProviderAdapterFactory;
import org.eclipse.emf.cdo.releng.setup.util.EMFUtil;
import org.eclipse.emf.cdo.releng.setup.util.ServiceUtil;
import org.eclipse.emf.cdo.releng.setup.util.SetupResource;
import org.eclipse.emf.cdo.releng.setup.util.log.ProgressLog;
import org.eclipse.emf.cdo.releng.setup.util.log.ProgressLogRunnable;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eike Stepper
 */
public class InstallerDialog extends AbstractSetupDialog
{
  public static final int RETURN_WORKBENCH = -2;

  public static final int RETURN_RESTART = -3;

  private static final String ECLIPSE_VERSION_COLUMN = "eclipse";

  private Map<Branch, Setup> setups;

  private AdapterFactory adapterFactory;

  private ResourceSet resourceSet;

  private Preferences preferences;

  private Configuration configuration;

  private CheckboxTreeViewer viewer;

  private Text installFolderText;

  private Text bundlePoolFolderText;

  private ComboBoxViewerCellEditor cellEditor;

  public InstallerDialog(Shell parentShell)
  {
    super(parentShell);
    setHelpAvailable(true);

    resourceSet = EMFUtil.createResourceSet();
    adapterFactory = new SetupDialogAdapterFactory();
  }

  @Override
  protected Point getInitialSize()
  {
    return new Point(500, 700);
  }

  @Override
  protected String getDefaultMessage()
  {
    return null;
  }

  @Override
  protected void createUI(Composite parent)
  {
    viewer = new CheckboxTreeViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
    Tree tree = viewer.getTree();
    tree.setLinesVisible(true);
    tree.setHeaderVisible(true);
    tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

    final AdapterFactoryContentProvider contentProvider = new AdapterFactoryContentProvider(adapterFactory)
    {
      private Map<Object, Object> parentMap = new HashMap<Object, Object>();

      @Override
      public boolean hasChildren(Object object)
      {
        if (object instanceof Project)
        {
          return true;
        }

        return super.hasChildren(object);
      }

      @Override
      public Object getParent(Object object)
      {
        Object parent = parentMap.get(object);
        if (parent != null)
        {
          return parent;
        }

        if (object instanceof Project)
        {
          return viewer.getInput();
        }

        return super.getParent(object);
      }

      @Override
      public Object[] getChildren(Object object)
      {
        if (object instanceof Project)
        {
          final InternalEObject eObject = (InternalEObject)object;
          URI eProxyURI = eObject.eProxyURI();
          if (eProxyURI != null)
          {
            try
            {
              URI resourceURI = eProxyURI.trimFragment();
              Resource resource = resourceSet.getResource(resourceURI, false);
              if (resource == null)
              {
                resource = loadResourceSafely(resourceURI);
                object = resource.getEObject(eProxyURI.fragment());

                // Force proxy reference from the configuration to resolve too.
                EList<Project> projects = configuration.getProjects();
                projects.get(projects.indexOf(eObject));

                final Project project = (Project)object;
                Object[] children = super.getChildren(project);
                for (Object child : children)
                {
                  parentMap.put(child, eObject);
                }

                viewer.getControl().getDisplay().asyncExec(new Runnable()
                {
                  public void run()
                  {
                    String label = project.getLabel();
                    if (label == null)
                    {
                      label = project.getName();
                    }

                    ((Project)eObject).setLabel(label);
                    InstallerDialog.this.viewer.update(project, null);
                  }
                });

                return children;
              }
              object = resource.getEObject(eProxyURI.fragment());
            }
            catch (UpdatingException ex)
            {
              // Ignore
            }
          }
        }

        return super.getChildren(object);
      }
    };
    viewer.setContentProvider(contentProvider);

    SetupDialogLabelProvider labelProvider = new SetupDialogLabelProvider(adapterFactory, viewer);
    viewer.setLabelProvider(labelProvider);
    viewer.setCellModifier(new ICellModifier()
    {
      public boolean canModify(Object element, String property)
      {
        return !isInstalled(element);
      }

      public Object getValue(Object element, String property)
      {
        if (element instanceof Branch && ECLIPSE_VERSION_COLUMN.equals(property))
        {
          Branch branch = (Branch)element;
          Setup setup = getSetup(branch);
          if (setup != null)
          {
            return setup.getEclipseVersion();
          }
        }

        return null;
      }

      public void modify(Object element, String property, Object value)
      {
        TreeItem item = (TreeItem)element;
        Object modelElement = item.getData();
        if (modelElement instanceof Branch && ECLIPSE_VERSION_COLUMN.equals(property))
        {
          Branch branch = (Branch)modelElement;
          Setup setup = getSetup(branch);
          if (setup != null)
          {
            setup.setEclipseVersion((Eclipse)value);
            viewer.update(modelElement, new String[] { property });
          }
        }
      }
    });
    viewer.setColumnProperties(new String[] { "project", ECLIPSE_VERSION_COLUMN });

    cellEditor = new ComboBoxViewerCellEditor(viewer.getTree());
    cellEditor.setContentProvider(new IStructuredContentProvider()
    {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public void dispose()
      {
      }

      public Object[] getElements(Object inputElement)
      {
        EList<Eclipse> eclipses = configuration.getEclipseVersions();
        return eclipses.toArray(new Eclipse[eclipses.size()]);
      }
    });

    viewer.setCellEditors(new CellEditor[] { null, cellEditor });
    cellEditor.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));

    viewer.addCheckStateListener(new ICheckStateListener()
    {
      public void checkStateChanged(CheckStateChangedEvent event)
      {
        boolean checked = event.getChecked();
        final Object element = event.getElement();
        if (element instanceof Project)
        {
          Project project = (Project)element;
          for (Object branch : contentProvider.getChildren(project))
          {
            viewer.setChecked(branch, checked);
          }

          viewer.expandToLevel(project, 1);
        }
        else if (element instanceof Branch)
        {
          if (checked)
          {
            viewer.getControl().getDisplay().asyncExec(new Runnable()
            {
              public void run()
              {
                viewer.editElement(element, 1);
              }
            });
          }
          else
          {
            Branch branch = (Branch)element;
            viewer.setChecked(branch.getProject(), false);
          }
        }

        validate();
      }
    });

    TreeViewerColumn treeViewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
    treeViewerColumn.setLabelProvider(new DecoratingColumLabelProvider(labelProvider, new SetupDialogLabelDecorator()));
    TreeColumn trclmnProjectBranch = treeViewerColumn.getColumn();
    trclmnProjectBranch.setWidth(300);
    trclmnProjectBranch.setText("Project / Branch");

    TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(viewer, SWT.NONE);
    treeViewerColumn_1
        .setLabelProvider(new DecoratingColumLabelProvider(labelProvider, new SetupDialogLabelDecorator())
        {
          @Override
          public String getText(Object element)
          {
            if (element instanceof Branch)
            {
              Branch branch = (Branch)element;
              Setup setup = getSetup(branch);
              if (setup != null)
              {
                Eclipse eclipse = setup.getEclipseVersion();
                return labelProvider.getText(eclipse);
              }
            }

            return "";
          }

          @Override
          public Image getImage(Object element)
          {
            return null;
          }
        });

    TreeColumn trclmnEclipseVersion = treeViewerColumn_1.getColumn();
    trclmnEclipseVersion.setWidth(150);
    trclmnEclipseVersion.setText("Eclipse Version");

    Group grpPreferences = new Group(parent, SWT.NONE);
    grpPreferences.setLayout(new GridLayout(3, false));
    grpPreferences.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    // grpPreferences.setText("Preferences");
    grpPreferences.setBounds(0, 0, 70, 82);

    // Label userNameLabel = new Label(grpPreferences, SWT.NONE);
    // userNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    // userNameLabel.setBounds(0, 0, 55, 15);
    // userNameLabel.setText("Git/Gerrit ID:");
    //
    // userNameText = new Text(grpPreferences, SWT.BORDER);
    // userNameText
    // .setToolTipText("Must match your account on Git/Gerrit.\nDon't forget to upload your public key to that account!");
    // GridData gd_userNameText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    // gd_userNameText.widthHint = 165;
    // userNameText.setLayoutData(gd_userNameText);
    // userNameText.setBounds(0, 0, 76, 21);
    // userNameText.addModifyListener(new ModifyListener()
    // {
    // public void modifyText(ModifyEvent e)
    // {
    // preferences.setUserName(userNameText.getText());
    // saveEObject(preferences);
    // validate();
    // }
    // });

    // Label empty = new Label(grpPreferences, SWT.NONE);
    // empty.setBounds(0, 0, 55, 15);
    // TODO
    // Need to move this to the bottom.
    // Also want current tool version
    // Button editButton = new Button(grpPreferences, SWT.NONE);
    // editButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    // editButton.setBounds(0, 0, 75, 25);
    // editButton.setText("Preferences...");
    // editButton.addSelectionListener(new SelectionAdapter()
    // {
    // @Override
    // public void widgetSelected(SelectionEvent e)
    // {
    // close();
    // setReturnCode(RETURN_WORKBENCH);
    // }
    // });

    Label installFolderLabel = new Label(grpPreferences, SWT.NONE);
    installFolderLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    installFolderLabel.setBounds(0, 0, 55, 15);
    installFolderLabel.setText("Install Folder:");

    installFolderText = new Text(grpPreferences, SWT.BORDER);
    installFolderText.setToolTipText("Points to the folder where the setup tool will create the project folders.");
    installFolderText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    installFolderText.addModifyListener(new ModifyListener()
    {
      private String previousText;

      public void modifyText(ModifyEvent e)
      {
        setups = null;

        String text = installFolderText.getText();
        preferences.setInstallFolder(text);
        String defaultBundlePoolFolder = text + File.separator + ".p2pool-ide";
        if (previousText == null
            || bundlePoolFolderText.getText().equals(previousText + File.separator + ".p2pool-ide"))
        {
          bundlePoolFolderText.setText(defaultBundlePoolFolder);
        }

        previousText = text;

        saveEObject(preferences);
        validate();
      }
    });

    Button installFolderButton = new Button(grpPreferences, SWT.NONE);
    installFolderButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    installFolderButton.setBounds(0, 0, 75, 25);
    installFolderButton.setText("Browse...");
    installFolderButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        DirectoryDialog dlg = new DirectoryDialog(getShell());
        dlg.setText("Select Install Folder");
        dlg.setMessage("Select a folder");
        String dir = dlg.open();
        if (dir != null)
        {
          installFolderText.setText(dir);
        }
      }
    });

    Label bundlePoolFolderLabel = new Label(grpPreferences, SWT.NONE);
    bundlePoolFolderLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    bundlePoolFolderLabel.setText("Bundle Pool Folder:");

    bundlePoolFolderText = new Text(grpPreferences, SWT.BORDER);
    bundlePoolFolderText
        .setToolTipText("Points to your native Git installation in order to reuse the 'etc/gitconfig' file.");
    bundlePoolFolderText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    bundlePoolFolderText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        preferences.setBundlePoolFolder(bundlePoolFolderText.getText());
        saveEObject(preferences);
        validate();
      }
    });

    Button bundlePoolFolderButton = new Button(grpPreferences, SWT.NONE);
    bundlePoolFolderButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    bundlePoolFolderButton.setBounds(0, 0, 75, 25);
    bundlePoolFolderButton.setText("Browse...");
    bundlePoolFolderButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        DirectoryDialog dlg = new DirectoryDialog(getShell());
        dlg.setText("Select IDE Bundle Pool Folder");
        dlg.setMessage("Select a folder");
        String dir = dlg.open();
        if (dir != null)
        {
          bundlePoolFolderText.setText(dir);
        }
      }
    });

    parent.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        init();
      }
    });
  }

  /**
   * Create contents of the button bar.
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, "Install", true);
    validate();
  }

  @Override
  protected Control createHelpControl(Composite parent)
  {
    ToolBar toolBar = (ToolBar)super.createHelpControl(parent);

    {
      ImageDescriptor imageDescriptor = Activator.getImageDescriptor("icons/dialog_prefs.gif");
      final Image image = imageDescriptor.createImage(toolBar.getDisplay());

      ToolItem button = new ToolItem(toolBar, SWT.PUSH);
      button.setImage(image);
      button.setToolTipText("Preferences");
      button.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          close();
          setReturnCode(RETURN_WORKBENCH);
        }
      });

      button.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          image.dispose();
        }
      });
    }

    {
      ImageDescriptor imageDescriptor = Activator.getImageDescriptor("icons/dialog_update.gif");
      final Image image = imageDescriptor.createImage(toolBar.getDisplay());

      ToolItem button = new ToolItem(toolBar, SWT.PUSH);
      button.setImage(image);
      button.setToolTipText("Update");
      button.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          update(false);
        }
      });

      button.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          image.dispose();
        }
      });
    }

    return toolBar;
  }

  @Override
  protected void okPressed()
  {
    try
    {
      install();
    }
    catch (Throwable ex)
    {
      handleException(ex);
    }

    super.okPressed();
  }

  protected boolean update(final boolean needsEarlyConfirmation)
  {
    if (needsEarlyConfirmation)
    {
      final AtomicBoolean result = new AtomicBoolean();
      InstallerDialog.this.getShell().getDisplay().syncExec(new Runnable()
      {
        public void run()
        {
          boolean confirmation = MessageDialog
              .openQuestion(
                  null,
                  "Update",
                  "Updates are needed to process the configuration, and then a restart is required. "
                      + "It might be possible for the tool to process the configuration with an older version of the tool, but that's not recommended.\n\n"
                      + "Do you wish to update?");
          result.set(confirmation);
        }
      });

      if (!result.get())
      {
        return false;
      }
    }

    try
    {
      final IRunnableWithProgress runnable = new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          IProvisioningAgent agent = ServiceUtil.getService(IProvisioningAgent.class);

          try
          {
            SubMonitor sub = SubMonitor.convert(monitor, needsEarlyConfirmation ? "Updating..."
                : "Checking for updates...", 1000);
            IStatus updateStatus = checkForUpdates(agent, sub);
            if (updateStatus.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE)
            {
              InstallerDialog.this.getShell().getDisplay().asyncExec(new Runnable()
              {
                public void run()
                {
                  MessageDialog.openInformation(null, "Update", "No updates were found");
                }
              });
            }
            else if (updateStatus.getSeverity() != IStatus.ERROR)
            {
              InstallerDialog.this.getShell().getDisplay().asyncExec(new Runnable()
              {
                public void run()
                {
                  close();
                  setReturnCode(RETURN_RESTART);

                  if (!needsEarlyConfirmation)
                  {
                    MessageDialog.openInformation(null, "Update", "Updates were installed, restart required");
                  }
                }
              });
            }
            else
            {
              throw new InvocationTargetException(new CoreException(updateStatus));
            }
          }
          finally
          {
            ServiceUtil.ungetService(agent);
            monitor.done();
          }
        }
      };

      viewer.getControl().getDisplay().asyncExec(new Runnable()
      {
        public void run()
        {
          try
          {
            runInProgressDialog(runnable);
          }
          catch (InvocationTargetException ex)
          {
            handleException(ex.getCause());
          }
          catch (InterruptedException ex)
          {
            // Do nothing
          }
        }
      });
    }
    catch (Throwable ex)
    {
      handleException(ex);
    }

    return true;
  }

  private IStatus checkForUpdates(IProvisioningAgent agent, SubMonitor sub)
  {
    try
    {
      addRepository(agent, SetupTaskPerformer.RELENG_URL, sub.newChild(200));
    }
    catch (ProvisionException ex)
    {
      return ex.getStatus();
    }

    ProvisioningSession session = new ProvisioningSession(agent);
    IProfileRegistry profileRegistry = (IProfileRegistry)agent.getService(IProfileRegistry.class.getName());
    IProfile profile = profileRegistry.getProfile(IProfileRegistry.SELF);
    IQueryResult<IInstallableUnit> queryResult = profile.query(QueryUtil.createIUAnyQuery(), null);

    List<IInstallableUnit> ius = new ArrayList<IInstallableUnit>();
    for (IInstallableUnit installableUnit : queryResult)
    {
      String id = installableUnit.getId();
      if (id.startsWith("org.eclipse.emf.cdo") || id.startsWith("org.eclipse.net4j"))
      {
        ius.add(installableUnit);
      }
    }

    UpdateOperation operation = new UpdateOperation(session, ius);
    IStatus status = operation.resolveModal(sub.newChild(300));
    if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE)
    {
      return status;
    }

    if (status.getSeverity() == IStatus.CANCEL)
    {
      throw new OperationCanceledException();
    }

    if (status.getSeverity() != IStatus.ERROR)
    {
      ProvisioningJob job = operation.getProvisioningJob(null);
      if (job == null)
      {
        String resolutionDetails = operation.getResolutionDetails();
        throw new IllegalStateException(resolutionDetails);
      }

      status = job.runModal(sub.newChild(300));
      if (status.getSeverity() == IStatus.CANCEL)
      {
        throw new OperationCanceledException();
      }
    }

    return status;
  }

  private void addRepository(IProvisioningAgent agent, String location, IProgressMonitor monitor)
      throws ProvisionException
  {
    SubMonitor sub = SubMonitor.convert(monitor, "Loading " + location, 1000);

    try
    {
      java.net.URI uri = new java.net.URI(location);
      addMetadataRepository(agent, uri, sub.newChild(500));
      addArtifactRepository(agent, uri, sub.newChild(500));
    }
    catch (URISyntaxException ex)
    {
      throw new IllegalArgumentException(ex);
    }
  }

  private void addMetadataRepository(IProvisioningAgent agent, java.net.URI location, IProgressMonitor monitor)
      throws ProvisionException
  {
    IMetadataRepositoryManager manager = (IMetadataRepositoryManager)agent
        .getService(IMetadataRepositoryManager.SERVICE_NAME);
    if (manager == null)
    {
      throw new IllegalStateException("No metadata repository manager found");
    }

    manager.loadRepository(location, monitor);
  }

  private void addArtifactRepository(IProvisioningAgent agent, java.net.URI location, IProgressMonitor monitor)
      throws ProvisionException
  {
    IArtifactRepositoryManager manager = (IArtifactRepositoryManager)agent
        .getService(IArtifactRepositoryManager.SERVICE_NAME);
    if (manager == null)
    {
      throw new IllegalStateException("No metadata repository manager found");
    }

    manager.loadRepository(location, monitor);
  }

  class UpdatingException extends Exception
  {
    private static final long serialVersionUID = 1L;

  }

  private SetupResource loadResourceSafely(URI uri) throws UpdatingException
  {
    SetupResource resource = EMFUtil.loadResourceSafely(resourceSet, uri);
    if (resource.getToolVersion() > SetupTaskMigrator.TOOL_VERSION)
    {
      if (update(true))
      {
        throw new UpdatingException();
      }
    }

    return resource;
  }

  private void init()
  {
    try
    {
      IRunnableWithProgress runnable = new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          try
          {
            monitor.beginTask("Loading " + EMFUtil.SETUP_URI.trimFragment(), IProgressMonitor.UNKNOWN);

            Resource configurationResource = loadResourceSafely(EMFUtil.SETUP_URI);

            configuration = (Configuration)configurationResource.getContents().get(0);

            InternalEList<Project> configuredProjects = (InternalEList<Project>)configuration.getProjects();

            String installFolder;
            String bundlePoolFolder;

            if (resourceSet.getURIConverter().exists(Preferences.PREFERENCES_URI, null))
            {
              Resource resource = loadResourceSafely(Preferences.PREFERENCES_URI);
              preferences = (Preferences)resource.getContents().get(0);

              installFolder = safe(preferences.getInstallFolder());
              bundlePoolFolder = safe(preferences.getBundlePoolFolder());
            }
            else
            {
              Resource resource = resourceSet.createResource(Preferences.PREFERENCES_URI);
              preferences = SetupFactory.eINSTANCE.createPreferences();
              resource.getContents().add(preferences);
              saveEObject(preferences);

              File rootFolder = new File(System.getProperty("user.home", "."));

              installFolder = safe(getAbsolutePath(rootFolder));
              bundlePoolFolder = safe(getAbsolutePath(new File(installFolder, ".p2pool-ide")));
            }

            ItemProvider input = new ItemProvider();
            EList<Object> projects = input.getChildren();

            for (int i = 0; i < configuredProjects.size(); i++)
            {
              InternalEObject project = (InternalEObject)configuredProjects.basicGet(i);
              if (project.eIsProxy())
              {
                URI uri = project.eProxyURI().trimFragment();
                if (uri.equals(EMFUtil.EXAMPLE_PROXY_URI))
                {
                  continue;
                }
              }

              projects.add(project);
            }

            initUI(input, installFolder, bundlePoolFolder);
          }
          catch (UpdatingException ex)
          {
            // Ignore
          }
          finally
          {
            monitor.done();
          }
        }

        private void initUI(final ItemProvider input, final String installFolder, final String bundlePoolFolder)
        {
          viewer.getControl().getDisplay().asyncExec(new Runnable()
          {
            public void run()
            {
              installFolderText.setText(installFolder);
              bundlePoolFolderText.setText(bundlePoolFolder);

              viewer.setInput(input);

              cellEditor.setInput(this);
            }
          });
        }
      };

      runInProgressDialog(runnable);
    }
    catch (InterruptedException ex)
    {
      // Do nothing
    }
    catch (InvocationTargetException ex)
    {
      handleException(ex.getCause());
    }
    catch (Throwable ex)
    {
      handleException(ex);
    }
  }

  private Setup getSetup(Branch branch)
  {
    if (setups == null)
    {
      setups = new HashMap<Branch, Setup>();
    }

    Setup setup = setups.get(branch);
    if (setup == null)
    {
      URI uri = getSetupURI(branch);
      if (resourceSet.getURIConverter().exists(uri, null))
      {
        try
        {
          Resource resource = loadResourceSafely(uri);
          setup = (Setup)resource.getContents().get(0);
        }
        catch (UpdatingException ex)
        {
          return null;
        }
      }
      else
      {
        setup = SetupFactory.eINSTANCE.createSetup();
        setup.setEclipseVersion(getDefaultEclipseVersion());
        setup.setBranch(branch);
        setup.setPreferences(preferences);

        Resource resource = resourceSet.createResource(uri);
        resource.getContents().add(setup);
      }

      setups.put(branch, setup);
    }

    return setup;
  }

  private Eclipse getDefaultEclipseVersion()
  {
    EList<Eclipse> eclipses = configuration.getEclipseVersions();
    return eclipses.get(eclipses.size() - 1);
  }

  private void validate()
  {
    String text = installFolderText.getText();
    String defaultBundlePoolFolder = text + File.separator + ".p2pool-ide";
    bundlePoolFolderText.setForeground(getShell().getDisplay().getSystemColor(
        bundlePoolFolderText.getText().equals(defaultBundlePoolFolder) ? SWT.COLOR_DARK_GRAY : SWT.COLOR_BLACK));

    if (viewer != null)
    {
      viewer.refresh(true);
    }

    Button installButton = getButton(IDialogConstants.OK_ID);
    if (installButton == null)
    {
      return;
    }

    if (viewer.getCheckedElements().length == 0)
    {
      setMessage("Select one or more project branches to install.", IMessageProvider.NONE);
      installButton.setEnabled(false);
      return;
    }

    if (installFolderText.getText().length() == 0)
    {
      setMessage("Enter the install folder.", IMessageProvider.ERROR);
      installButton.setEnabled(false);
      return;
    }

    setMessage("Click the Install button to start the installation process.", IMessageProvider.NONE);
    installButton.setEnabled(true);
  }

  private String safe(String string)
  {
    if (string == null)
    {
      return "";
    }

    return string;
  }

  private String getAbsolutePath(File file)
  {
    try
    {
      return file.getCanonicalPath();
    }
    catch (IOException ex)
    {
      return file.getAbsolutePath();
    }
  }

  private URI getSetupURI(Branch branch)
  {
    return getSetupURI(branch, installFolderText.getText());
  }

  private URI getSetupURI(Branch branch, String installFolder)
  {
    File projectFolder = new File(installFolder, branch.getProject().getName().toLowerCase());
    File branchFolder = new File(projectFolder, branch.getName().toLowerCase());
    File setupFile = new File(branchFolder, "setup.xmi");
    return URI.createFileURI(setupFile.getAbsolutePath());
  }

  private boolean isInstalled(Object object)
  {
    if (object instanceof Branch)
    {
      Branch branch = (Branch)object;
      Setup setup = getSetup(branch);
      if (setup != null)
      {
        URI uri = setup.eResource().getURI();
        return resourceSet.getURIConverter().exists(uri, null);
      }
    }

    return false;
  }

  private void install() throws Exception
  {
    final Object[] checkedElements = viewer.getCheckedElements();
    final String installFolder = installFolderText.getText();
    final String gitPrefix = safe(bundlePoolFolderText.getText());

    File folder = new File(installFolder);
    folder.mkdirs();

    final List<SetupTaskPerformer> setupTaskPerformers = new ArrayList<SetupTaskPerformer>();
    for (Object checkedElement : checkedElements)
    {
      if (checkedElement instanceof Branch)
      {
        Branch branch = (Branch)checkedElement;
        Setup setup = getSetup(branch);
        if (setup != null)
        {
          SetupTaskPerformer taskPerformer = createTaskPerformer(setup, installFolder, gitPrefix);
          setupTaskPerformers.add(taskPerformer);
        }
      }
    }

    ProgressLogDialog.run(getShell(), new ProgressLogRunnable()
    {
      public Set<String> run(ProgressLog log) throws Exception
      {
        for (SetupTaskPerformer setupTaskPerformer : setupTaskPerformers)
        {
          install(setupTaskPerformer);

          Preferences newPreferences = setupTaskPerformer.getPreferences();
          Resource eResource = preferences.eResource();
          eResource.getContents().set(0, newPreferences);
          eResource.save(null);
        }

        return null;
      }
    }, setupTaskPerformers);
  }

  private SetupTaskPerformer createTaskPerformer(Setup setup, String installFolder, String gitPrefix)
  {
    saveEObject(setup);

    Branch branch = setup.getBranch();
    Project project = branch.getProject();

    String branchFolder = branch.getName().toLowerCase();
    String projectFolder = project.getName().toLowerCase();
    File branchDir = new File(installFolder, projectFolder + "/" + branchFolder);

    SetupTaskPerformer performer = new SetupTaskPerformer(branchDir);
    return performer;
  }

  private void install(SetupTaskPerformer performer) throws Exception
  {
    performer.getWorkspaceDir().mkdirs();
    performer.perform();

    performer.log("Launching IDE");
    String eclipseExecutable = performer.getOS().getEclipseExecutable();
    String eclipsePath = new File(performer.getBranchDir(), "eclipse/" + eclipseExecutable).getAbsolutePath();
    File ws = new File(performer.getBranchDir(), "ws");

    ProcessBuilder builder = new ProcessBuilder(eclipsePath);
    builder.directory(ws);
    builder.start();
  }

  private void saveEObject(EObject eObject)
  {
    try
    {
      XMLResource xmlResource = (XMLResource)eObject.eResource();
      xmlResource.getEObjectToExtensionMap().clear();
      xmlResource.save(null);
    }
    catch (IOException ex)
    {
      Activator.log(ex);
    }
  }

  private void runInProgressDialog(IRunnableWithProgress runnable) throws InvocationTargetException,
      InterruptedException
  {
    ProgressMonitorDialog dialog = new ProgressMonitorDialog(viewer.getControl().getShell())
    {
      @Override
      protected Point getInitialSize()
      {
        Point calculatedSize = super.getInitialSize();
        if (calculatedSize.x < 800)
        {
          calculatedSize.x = 800;
        }

        return calculatedSize;
      }
    };

    dialog.run(true, true, runnable);
  }

  private void handleException(Throwable ex)
  {
    Activator.log(ex);
    ErrorDialog.open(ex);
  }

  /**
   * @author Eike Stepper
   */
  private final class SetupDialogAdapterFactory extends SetupItemProviderAdapterFactory
  {
    @Override
    public Adapter createConfigurationAdapter()
    {
      if (configurationItemProvider == null)
      {
        configurationItemProvider = new ConfigurationItemProvider(this)
        {
          @Override
          public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
          {
            if (childrenFeatures == null)
            {
              childrenFeatures = new ArrayList<EStructuralFeature>();
              childrenFeatures.add(SetupPackage.Literals.CONFIGURATION__PROJECTS);
            }

            return childrenFeatures;
          }
        };
      }

      return configurationItemProvider;
    }

    @Override
    public Adapter createProjectAdapter()
    {
      if (projectItemProvider == null)
      {
        projectItemProvider = new ProjectItemProvider(this)
        {
          @Override
          public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
          {
            if (childrenFeatures == null)
            {
              childrenFeatures = new ArrayList<EStructuralFeature>();
              childrenFeatures.add(SetupPackage.Literals.PROJECT__BRANCHES);
            }

            return childrenFeatures;
          }
        };
      }

      return projectItemProvider;
    }

    @Override
    public Adapter createBranchAdapter()
    {
      if (branchItemProvider == null)
      {
        branchItemProvider = new BranchItemProvider(this)
        {
          @Override
          public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
          {
            if (childrenFeatures == null)
            {
              childrenFeatures = new ArrayList<EStructuralFeature>();
            }

            return childrenFeatures;
          }
        };
      }

      return branchItemProvider;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class SetupDialogLabelDecorator extends LabelProvider implements ILabelDecorator
  {
    public Image decorateImage(Image image, Object element)
    {
      return image;
    }

    public String decorateText(String text, Object element)
    {
      return text;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class SetupDialogLabelProvider extends AdapterFactoryLabelProvider.ColorProvider
  {
    private SetupDialogLabelProvider(AdapterFactory adapterFactory, Viewer viewer)
    {
      super(adapterFactory, viewer);
    }

    @Override
    public Color getForeground(Object object)
    {
      if (isInstalled(object))
      {
        return getShell().getDisplay().getSystemColor(SWT.COLOR_BLUE);
      }

      return super.getBackground(object);
    }
  }
}
