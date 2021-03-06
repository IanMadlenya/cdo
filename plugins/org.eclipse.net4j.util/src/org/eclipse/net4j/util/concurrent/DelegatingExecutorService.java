/*
 * Copyright (c) 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.concurrent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Eike Stepper
 * @since 3.6
 */
public class DelegatingExecutorService implements ExecutorService
{
  private final ExecutorService delegate;

  public DelegatingExecutorService(ExecutorService delegate)
  {
    this.delegate = delegate;
  }

  public void execute(Runnable command)
  {
    delegate.execute(command);
  }

  public void shutdown()
  {
    // Do nothing.
  }

  public List<Runnable> shutdownNow()
  {
    return Collections.emptyList();
  }

  public boolean isShutdown()
  {
    return delegate.isShutdown();
  }

  public boolean isTerminated()
  {
    return delegate.isTerminated();
  }

  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
  {
    return delegate.awaitTermination(timeout, unit);
  }

  public <T> Future<T> submit(Callable<T> task)
  {
    return delegate.submit(task);
  }

  public <T> Future<T> submit(Runnable task, T result)
  {
    return delegate.submit(task, result);
  }

  public Future<?> submit(Runnable task)
  {
    return delegate.submit(task);
  }

  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException
  {
    return delegate.invokeAll(tasks);
  }

  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException
  {
    return delegate.invokeAll(tasks, timeout, unit);
  }

  public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
  {
    return delegate.invokeAny(tasks);
  }

  public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
  {
    return delegate.invokeAny(tasks, timeout, unit);
  }
}
