package consoler;

import java.util.ArrayList;

public class ConsoleCommand {
	private String commandName;
	private String commandHelpText;
	private String commandHelpFullText;
	private ArrayList<String> commandFlag = new ArrayList<String>();
	private ArrayList<String> commandArgms = new ArrayList<String>();
	
	protected void addArgument(String argmName){
		this.commandArgms.add(argmName);
	}
	protected void addFlags(String flagName){
		this.commandFlag.add(flagName);
	}
	
	public ConsoleCommand setFullHelpText(String string){
		this.commandHelpFullText = string;
		return this;
	}
	public ConsoleCommand setHelpText(String string){
		this.commandHelpText = string;
		return this;
	}
	public ConsoleCommand setCommandName(String comName){
		this.commandName = comName;
		return this;
	}
	public String getHelpText(){
		return this.commandHelpText;
	}
	public String getFullHelpText(){
		if (this.commandHelpFullText != null)
			return this.commandHelpFullText.toString();
		else return this.commandHelpText;
	}
	public String getFlagByIndex(int indexOf){
		return this.commandFlag.get(indexOf);
	}
	public String getArgms(int e){
		return this.commandArgms.get(e);
	}
	public String getArgms(String e){
		String compStr;
		for (int i = 0; i <= commandFlag.size()-1; i++){
			compStr = commandFlag.get(i);
			if (compStr.compareTo(e) == 0)
				return commandArgms.get(i);
		}
		return "";
	}
	public String getName(){
		return this.commandName;
	}
	public boolean isFlagExists(String flag){
		if (this.commandFlag.indexOf(flag) != -1) return true;
		else return false;
	}
	public void executeCommand(){
		commandFlag.clear();
		commandArgms.clear();
	}
}