package Rename;

import java.util.ArrayList;
import java.util.List;

class NameChangeDetected {
	
	public static final int MAXIMUM_LOOK_BACK_SEARCHING_INTERMIDIATE_NAME_CHANGE = 5;
	public static final int MAXIMUM_LOOK_BACK_SEARCHING_BINDINGKEY = 40;
	private final List<ASTChangeName> detectedNameChanges = new ArrayList<ASTChangeName>();
	private final NameChangeCountHistory nameChangeHistory = new NameChangeCountHistory();

	private static NameChangeDetected instance = new NameChangeDetected();
	private NameChangeDetected(){};
	
	public static NameChangeDetected getInstance()
	{
		return instance;
	}

	public boolean isDeclarationChangeTrackable(String binding_key)
	{
		return getSkipedDeclaredNameChangesInHistory(binding_key).size() != 0;
	}
	
	public List<ASTChangeName> getSkipedDeclaredNameChangesInHistory(String currentBindingKey) {
		
		List<ASTChangeName> skips = new ArrayList<ASTChangeName>();
		int lookBack = Math.min(MAXIMUM_LOOK_BACK_SEARCHING_BINDINGKEY,
				detectedNameChanges.size());
		int start = detectedNameChanges.size() - 1;
		int end = start - lookBack;
		for (int i = start; i > end; i--) 
		{
			ASTChangeName change = detectedNameChanges.get(i);
			String newBinding = change.getNewNameBindingKey();
			if (change.isRenamingDeclaration() && newBinding.equals(currentBindingKey)) 
			{
				skips.add(0, change);
				currentBindingKey = change.getOldNameBindingKey();
			}
		}
		return skips;
	}
	

	public ASTChangeName searchIntermediateChange(ASTChangeName current)
	{
		int lookBack = Math.min(MAXIMUM_LOOK_BACK_SEARCHING_INTERMIDIATE_NAME_CHANGE,
				detectedNameChanges.size());
		int start = detectedNameChanges.size() - 1;
		int end = start - lookBack;
		for(int i = start; i> end; i--)
		{
			ASTChangeName change = detectedNameChanges.get(i);
			String codeOne = change.getNewCompilationUnitRecord().getSourceCode();
			int indexOne = change.getNodeOneIndex();
			String codeTwo = current.getOldCompilationUnitRecord().getSourceCode();
			int indexTwo = current.getNodeTwoIndex();
			if(codeOne.equals(codeTwo) && indexOne == indexTwo)
				return change;
		}
		
		return null;
		
	}
	
	public boolean isNewChange(ASTChangeName change)
	{
		return !detectedNameChanges.contains(change);
	}
	
	public void addNameChange(ASTChangeName change)
	{
		String binding = change.getOldNameBindingKey();
		int bindingCount = change.getOldNameBindingCount();
		nameChangeHistory.addNameChange(binding, bindingCount);
		detectedNameChanges.add(change);
	}
	
	public ASTChangeName getLatestDetectedChange()
	{
		return detectedNameChanges.get(detectedNameChanges.size() - 1);
	}
	
	
}
