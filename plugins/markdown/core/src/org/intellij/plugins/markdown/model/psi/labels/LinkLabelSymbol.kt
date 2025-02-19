package org.intellij.plugins.markdown.model.psi.labels

import com.intellij.find.usages.api.SearchTarget
import com.intellij.find.usages.api.UsageHandler
import com.intellij.model.Pointer
import com.intellij.navigation.NavigatableSymbol
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.backend.navigation.NavigationTarget
import com.intellij.platform.backend.presentation.TargetPresentation
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.rename.api.RenameTarget
import com.intellij.refactoring.suggested.startOffset
import org.intellij.plugins.markdown.MarkdownIcons
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownLinkDefinition
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownLinkLabel
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownShortReferenceLink
import org.intellij.plugins.markdown.model.psi.MarkdownSourceNavigationTarget
import org.intellij.plugins.markdown.model.psi.MarkdownSymbolWithUsages
import org.intellij.plugins.markdown.model.psi.withLocationIn
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
data class LinkLabelSymbol(
  override val file: PsiFile,
  override val range: TextRange,
  val text: @NlsSafe String
): MarkdownSymbolWithUsages, SearchTarget, RenameTarget, NavigatableSymbol {
  override fun createPointer(): Pointer<out LinkLabelSymbol> {
    return createPointer(file, range, text)
  }

  override val targetName: String
    get() = text

  override val maximalSearchScope: SearchScope
    get() = GlobalSearchScope.fileScope(file)

  override val usageHandler: UsageHandler
    get() = UsageHandler.createEmptyUsageHandler(text)

  override val searchText: String
    get() = text

  override fun presentation(): TargetPresentation {
    val builder = TargetPresentation.builder(text).icon(MarkdownIcons.EditorActions.Link)
    return builder.withLocationIn(file).presentation()
  }

  override fun getNavigationTargets(project: Project): Collection<NavigationTarget> {
    val virtualFile = file.virtualFile ?: return emptyList()
    return listOf(LinkNavigationTarget(virtualFile, range.startOffset))
  }

  private inner class LinkNavigationTarget(file: VirtualFile, offset: Int): MarkdownSourceNavigationTarget(file, offset) {
    override fun computePresentation(): TargetPresentation {
      return this@LinkLabelSymbol.presentation()
    }
  }

  companion object {
    fun createPointer(label: MarkdownLinkLabel): Pointer<LinkLabelSymbol> {
      val text = label.text
      val rangeInElement = TextRange(0, text.length)
      val absoluteRange = rangeInElement.shiftRight(label.startOffset)
      val textInElement = rangeInElement.substring(text)
      val file = label.containingFile
      return createPointer(file, absoluteRange, textInElement)
    }

    fun createPointer(file: PsiFile, range: TextRange, text: String): Pointer<LinkLabelSymbol> {
      return Pointer.fileRangePointer(file, range) { restoredFile, restoredRange ->
        LinkLabelSymbol(restoredFile, restoredRange, text)
      }
    }

    val MarkdownLinkLabel.isDeclaration: Boolean
      get() = parentOfType<MarkdownLinkDefinition>() != null

    val MarkdownLinkLabel.isShortLink: Boolean
      get() = parentOfType<MarkdownShortReferenceLink>() != null
  }
}
