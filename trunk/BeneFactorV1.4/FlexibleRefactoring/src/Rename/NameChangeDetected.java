package Rename;

import java.util.ArrayList;
import java.util.List;

class NameChangeDetected {
	
	public static final int MAXIMUM_LOOK_BACK_SEARCHING_INTERMIDIATE_NAME_CHANGE = 5;
	public static final int MAXIMUM_LOOK_BACK_SEARCHING_BINDINGKEY = 40;
	private final List<ASTNameChange> detectedNameChanges = new ArrayList<ASTNameChange>();
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
	
	public List<ASTNameChange> getSkipedDeclaredNameChangesInHistory(String currentBindingKey) {
		
		List<ASTNameChange> skips = new ArrayList<ASTNameChange>();
		int lookBack = Math.min(MAXIMUM_LOOK_BACK_SEARCHING_BINDINGKEY,
				detectedNameChanges.size());
		int start = detectedNameChanges.size() - 1;
		int end = start - lookBack;
		for (int i = start; i > end; i--) 
		{
			ASTNameChange change = detectedNameChanges.get(i);
			String newBinding = change.getNewNameBindingKey();
			if (change.isRenamingDeclaration() && newBinding.equals(currentBindingKey)) 
			{
				skips.add(0, change);
				currentBindingKey = change.getOldNameBindingKey();
			}
		}
		return skips;
	}
	

	public ASTNameChange searchIntermediateChange(ASTNameChange current)
	{
		int lookBack = Math.min(MAXIMUM_LOOK_BACK_SEARCHING_INTERMIDIATE_NAME_CHANGE,
				detectedNameChanges.size());
		int start = detectedNameChanges.size() - 1;
		int end = start - lookBack;
		for(int i = start; i> end; i--)
		{
			ASTNameChange change = detectedNameChanges.get(i);
			String codeOne = change.getNewCompilationUnitRecord().getSourceCode();
			int indexOne = change.getNodeOneIndex();
			String codeTwo = current.getOldCompilationUnitRecord().getSourceCode();
			int indexTwo = current.getNodeTwoIndex();
			if(codeOne.equals(codeTwo) && indexOne == indexTwo)
				return change;
		}
		
		return null;
		
	}
	
	public boolean isNewChange(ASTNameChange change)
	{
		return !detectedNameChanges.contains(change);
	}
	
	public void addNameChange(ASTNameChange change)
	{
		String binding = change.getOldNameBindingKey();
		int bindingCount = change.getOldNameBindingCount();
		nameChangeHistory.addNameChange(binding, bindingCount);
		detectedNameChanges.add(change);
	}
	
	public ASTNameChange getLatestDetectedChange()
	{
		return detectedNameChanges.get(detectedNameChanges.size() - 1);
	}
	
	
}
