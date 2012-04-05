package animation.autoedition;

import org.eclipse.jdt.core.ICompilationUnit;

public class TestingAtomaticEdition {

	
	private static AtomicEdition delete = new AtomicEdition(1, 1);
	private static AtomicEdition insertion = new AtomicEdition(1, "d");
	private static AtomicEditionComposite composite = prepareEditionCompostion();
	
	public static AtomicEditionComposite prepareEditionCompostion()
	{
		AtomicEditionComposite c = new AtomicEditionComposite();
		for(char ch = 'a'; ch<= 'z'; ch ++)
		{
			AtomicEdition e = new AtomicEdition(0, String.valueOf(ch));
			c.addEdition(e);
		}
		c.addObserver(ScalingBar.getInstance());
		ScalingBar.getInstance().addObserver(c);
		return c;
	}
	
	

	public static void test(ICompilationUnit unit) {
		
		TestingAtomaticEdition.test3(unit);
	}
	
	
	public static void test1(ICompilationUnit unit)
	{
		try {
			if(delete == null)
				return;
			delete.applyEdition(unit);
			delete = null;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static void test2(ICompilationUnit unit)
	{
		try {
			if(insertion == null)
				return;
			insertion.applyEdition(unit);
			insertion = null;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static void test3(ICompilationUnit unit)
	{
		try {
			if(composite == null)
				return;
			composite.applyEditions(unit);
			composite = null;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static void test4(ICompilationUnit unit)
	{
		if(composite == null)
			return;
		try {
			AtomicEditionComposite temp = composite;
			composite = null;
			temp.applyEditions(unit);
			temp = temp.getUndoEditionComposite();
			temp.applyEditions(unit);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}


	
}
