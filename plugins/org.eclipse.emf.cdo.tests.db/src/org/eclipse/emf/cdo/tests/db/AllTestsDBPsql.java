/**
 * Copyright (c) 2004 - 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Victor Roldan Betancort - initial API and implementation
 *    Eike Stepper - maintenance
 *    Stefan Winkler - maintenance
 */
package org.eclipse.emf.cdo.tests.db;

import org.eclipse.emf.cdo.server.db.CDODBUtil;
import org.eclipse.emf.cdo.server.db.mapping.IMappingStrategy;
import org.eclipse.emf.cdo.tests.db.bundle.OM;

import org.eclipse.net4j.db.DBUtil;
import org.eclipse.net4j.db.IDBAdapter;
import org.eclipse.net4j.db.postgresql.PostgreSQLAdapter;
import org.eclipse.net4j.util.io.IOUtil;

import org.postgresql.jdbc3.Jdbc3SimpleDataSource;

import javax.sql.DataSource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Database preparation before test execution:
 * <ol>
 * <li>create user sa (pass: sa)
 * <li>create databases cdodb1, authrepo, repo2
 * </ol>
 * Database creation/removal is avoided because it takes too long and makes the test-suite impractical.
 * 
 * @author Victor Roldan Betancort
 */
public class AllTestsDBPsql extends DBConfigs
{
  public static Test suite()
  {
    return new AllTestsDBPsql().getTestSuite("CDO Tests (DBStore PSQL Horizontal)");
  }

  @Override
  protected void initConfigSuites(TestSuite parent)
  {
    addScenario(parent, COMBINED, AllTestsDBPsql.Psql.INSTANCE, JVM, NATIVE);
  }

  @Override
  protected boolean hasAuditSupport()
  {
    return true;
  }

  @Override
  protected boolean hasBranchingSupport()
  {
    return false;
  }

  /**
   * @author Victor Roldan Betancort
   */
  public static class Psql extends DBStoreRepositoryConfig
  {
    private static final long serialVersionUID = 1L;

    public static final AllTestsDBPsql.Psql INSTANCE = new Psql("PostgreSQL");

    private transient Jdbc3SimpleDataSource dataSource;

    private transient Jdbc3SimpleDataSource setupDataSource;

    private String currentRepositoryName = "cdodb1";

    public Psql(String name)
    {
      super(name);
    }

    @Override
    protected IMappingStrategy createMappingStrategy()
    {
      return CDODBUtil.createHorizontalMappingStrategy(false);
    }

    @Override
    protected IDBAdapter createDBAdapter()
    {
      return new PostgreSQLAdapter();
    }

    @Override
    protected DataSource createDataSource(String repoName)
    {
      currentRepositoryName = repoName;

      dataSource = new Jdbc3SimpleDataSource();
      dataSource.setServerName("localhost");
      dataSource.setDatabaseName(currentRepositoryName);
      dataSource.setUser("sa");
      dataSource.setPassword("sa");

      try
      {
        dataSource.setLogWriter(new PrintWriter(System.err));
      }
      catch (SQLException ex)
      {
        OM.LOG.warn(ex.getMessage());
      }

      dropDatabase();

      return dataSource;
    }

    @Override
    protected void deactivateRepositories()
    {
      super.deactivateRepositories();
      dataSource = null;
      setupDataSource = null;
      dropDatabase();
    }

    private void dropDatabase()
    {
      Connection connection = null;

      try
      {
        connection = getSetupDataSource().getConnection();
        DBUtil.dropAllTables(connection, currentRepositoryName);
      }
      catch (SQLException ignore)
      {
        IOUtil.ERR().println(ignore);
      }
      finally
      {
        DBUtil.close(connection);
      }
    }

    private DataSource getSetupDataSource()
    {
      if (setupDataSource == null)
      {
        setupDataSource = new Jdbc3SimpleDataSource();
        setupDataSource.setServerName("localhost");
        setupDataSource.setDatabaseName(currentRepositoryName);
        setupDataSource.setUser("sa");
        setupDataSource.setPassword("sa");
      }

      return setupDataSource;
    }
  }
}
