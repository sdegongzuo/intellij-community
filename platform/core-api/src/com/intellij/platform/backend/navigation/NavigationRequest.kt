// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.platform.backend.navigation

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread
import com.intellij.util.concurrency.annotations.RequiresReadLock
import org.jetbrains.annotations.ApiStatus.Experimental
import org.jetbrains.annotations.ApiStatus.NonExtendable

/**
 * Use static functions to create instances.
 *
 * @see [NavigationRequest.sourceNavigationRequest]
 */
@Experimental
@NonExtendable // sealed
interface NavigationRequest {

  companion object {

    /**
     * @return a request for the navigation to a specified [offset] in a [file],
     * or `null` if the navigation is not possible for any reason
     */
    @RequiresReadLock
    @RequiresBackgroundThread
    @JvmStatic
    fun sourceNavigationRequest(file: VirtualFile, offset: Int): NavigationRequest? {
      return NavigationRequests.getInstance().sourceNavigationRequest(file, offset)
    }

    /**
     * @return a request for the navigation to a specified [directory],
     * or `null` if the navigation is not possible for any reason
     */
    @RequiresReadLock
    @RequiresBackgroundThread
    @JvmStatic
    fun directoryNavigationRequest(directory: PsiDirectory): NavigationRequest? {
      return NavigationRequests.getInstance().directoryNavigationRequest(directory)
    }
  }
}
