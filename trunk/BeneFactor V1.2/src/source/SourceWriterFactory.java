package source;


import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.formatter.CodeFormatter;

/**
 * A configurable factory to create {@link SourceWriter} objects.
 *
 * @author Philippe Beaudoin
 */
public class SourceWriterFactory {

  /**
   * Creates a new {@link SourceWriter} that can be used to format a new component inside the body
   * of a class, for example a field or a method.
   *
   * @return The new {@link SourceWriter}.
   */
  public SourceWriter createForNewClassBodyComponent() {
    return new SourceWriter(CodeFormatter.K_CLASS_BODY_DECLARATIONS);
  }

  /**
   * Creates a new {@link SourceWriter} that can be used to format a new class at the top-level of
   * the compilation unit.
   *
   * @return The new {@link SourceWriter}.
   */
  public SourceWriter createForNewClass() {
    return new SourceWriter(CodeFormatter.K_COMPILATION_UNIT);
  }

  /**
   * Creates a {@link SourceWriter} meant to append to the provided method. You
   * will have to call {@link SourceWriter#commit()} in order for the new
   *
   * @param method
   *          The method to append to.
   * @return The new {@link SourceWriter}.
   * @throws JavaModelException
   */
	public SourceWriter createForMethod(IMethod method)
			throws JavaModelException {
		return new SourceWriter(method);
	}

}