package Rename;

import java.util.ArrayList;
import java.util.List;

class NameChangeDetected {
	
	public static final int MAXIMUM_LOOK_BACK_SEARCHING_INTERMIDIATE_NAME_CHANGE = 5;
	public static final int MAXIMUM_LOOK_BACK_SEARCHING_BINDINGKEY = 40;
	private final List<ASTNameChangeInformation> detectedNameChanges = new ArrayList<ASTNameChangeInformation>();
	private final NameChangeCountHistory nameChangeHistory = new NameChangeCountHistory();

	private static NameChangeDetected instance = new NameChangeDetected();
	private NameChangeDetected(){};
	
	public static NameChangeDetected getInstance()
	{
		return instance;
	}
	
	public List<ASTNameChangeInformation> getSkipedDeclaredNameChangesInHistory(String currentBindingKey) {
		
		List<ASTNameChangeInformation> skips = new ArrayList<ASTNameChangeInformation>();
		int lookBack = Math.min(MAXIMUM_LOOK_BACK_SEARCHING_BINDINGKEY,
				detectedNameChanges.size());
		int start = detectedNameChanges.size() - 1;
		int end = start - lookBack;
		for (int i = start; i > end; i--) {
			ASTNameChangeInformation change = detectedNameChanges.get(i);
			String newBinding = change.getNewNameBindingKey();
			if (change.isRenamingDeclaration() && newBinding.equals(currentBindingKey)) 
			{
				if (!change.hasIntermediateChange())
				{
					skips.add(0, change);
					break;
				}
				else
				{
					skips.add(0, change);
					currentBindingKey = change.getOldNameBindingKey();
				}
			}

		}
		return skips;
	}

	public ASTNameChangeInformation searchIntermediateChange(ASTNameChangeInformation current)
	{
		int lookBack = Math.min(MAXIMUM_LOOK_BACK_SEARCHING_INTERMIDIATE_NAME_CHANGE,
				detectedNameChanges.size());
		int start = detectedNameChanges.size() - 1;
		int end = start - lookBack;
		for(int i = start; i> end; i--)
		{
			ASTNameChangeInformation change = detectedNameChanges.get(i);
			String codeOne = change.getNewCompilationUnitRecord().getSourceCode();
			int indexOne = change.getNodeOneIndex();
			String codeTwo = current.getOldCompilationUnitRecord().getSourceCode();
			int indexTwo = current.getNodeTwoIndex();
			if(codeOne.equals(codeTwo) && indexOne == indexTwo)
				return change;
		}
		
		return null;
		
	}
	
	public boolean isNewChange(ASTNameChangeInformation change)
	{
		return !detectedNameChanges.contains(change);
	}
	
	public void addNameChange(ASTNameChangeInformation change)
	{
		String binding = change.getOldNameBindingKey();
		int bindingCount = change.getOldNameBindingCount();
		nameChangeHistory.addNameChange(binding, bindingCount);
		detectedNameChanges.add(change);
	}
	
	public ASTNameChangeInformation getLatestDetectedChange()
	{
		return detectedNameChanges.get(detectedNameChanges.size() - 1);
	}
	
	
}
