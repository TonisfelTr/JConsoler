package consoler;

import java.util.Scanner;
import java.util.ArrayList;

public class Consoler {	
	private static ArrayList<ConsoleCommand> cmdList = new ArrayList<ConsoleCommand>();
	private static Scanner sc;
	private static String ProgramAuthor = "�������� ����";
	
	private static class cmdExit extends ConsoleCommand{
		public void executeCommand(){
			super.executeCommand();
			sc.close();
			System.exit(0);
		}
	}
	private static class cmdHelpList extends ConsoleCommand{
		public void executeCommand(){
			if (this.getArgms("?") == ""){
				System.out.println("������ ������:");
				System.out.println("----------------------------------------");
				for (int i = 0; i < cmdList.size(); i++){
					System.out.println(Integer.toString(i+1) + ". " + cmdList.get(i).getName() + " - " + cmdList.get(i).getHelpText());
				}
				System.out.println("----------------------------------------");
				System.out.println("��� ��������� ��������� ������� ����������� ���� \"?\". ������: help -? \"exit\".");
			} else {
				int e;
				if ((e = isExists(this.getArgms("?"))) == -1){
					System.out.println("\"" + this.getArgms("?") + "\" �� �������� ������������������ ��������.");
				} else {
					System.out.println(cmdList.get(e).getFullHelpText());
				}
			}
			super.executeCommand();
		}
	}
	private static class cmdHelpListAlternative extends ConsoleCommand{
		public void executeCommand(){
			if (this.getArgms("?") == ""){
				System.out.println("������ ������:");
				System.out.println("----------------------------------------");
				for (int i = 0; i < cmdList.size(); i++){
					System.out.println(Integer.toString(i+1) + ". " + cmdList.get(i).getName() + " - " + cmdList.get(i).getHelpText());
				}
				System.out.println("----------------------------------------");
				System.out.println("��� ��������� ��������� ������� ����������� ���� \"?\". ������: help -? \"exit\".");
			} else {
				int e;
				if ((e = isExists(this.getArgms("?"))) == -1){
					System.out.println("\"" + this.getArgms("?") + "\" �� �������� ������������������ ��������.");
				} else {
					System.out.println(cmdList.get(e).getFullHelpText());
				}
			}
			super.executeCommand();
		}
	}	
	private static class cmdScriptExecute extends ConsoleCommand{
		public void executeCommand(){
			if (!this.isFlagExists("p")){
				System.out.println("�� ������ ���� �� �����, ������� ����� ���������.\n"
						+ "����������� script -p \"<����_��_�����>\", ����� ��������� ������.");
				return;
			}
			if (this.getArgms("p") != ""){
				new ConsoleScript(this.getArgms("p"));
			} 
			super.executeCommand();
		}
	}
	
	private static int isExists(String cmdBody){
		for (int i = 0; i <= Consoler.cmdList.size()-1; i++){
			if (Consoler.cmdList.get(i).getName().compareTo(cmdBody) == 0)
				return i;
		}
		return -1;
	}
	static void parseCommand(String strCommand){
		//������ �������� �������.
		String cmdName;
		if (strCommand.indexOf(" ") != -1)
			cmdName = strCommand.substring(0, strCommand.indexOf(" "));
		else cmdName = strCommand.substring(0, strCommand.length());
		String cmdFlag;
		String cmdArgm;
		strCommand = strCommand.substring(strCommand.indexOf(" ")+1, strCommand.length());
		//������, ���������� �� ����� �������.
		int indexOfCmd = Consoler.isExists(cmdName);
		if (indexOfCmd != -1){
			if ((strCommand.indexOf("-") != -1) && (strCommand.indexOf("-", strCommand.indexOf("-")+1) == strCommand.indexOf("-")+1)){
				Consoler.cmdList.get(indexOfCmd).addFlags(strCommand.substring(strCommand.indexOf("-", strCommand.indexOf("-")+1), (strCommand.indexOf(" ", strCommand.indexOf("-")+1) != -1) ? 
						strCommand.indexOf(" ", strCommand.indexOf("-")+1) : strCommand.length()));
				Consoler.cmdList.get(indexOfCmd).addArgument("");
				if (strCommand.indexOf(" ", strCommand.indexOf("-")+1) == -1){
					Consoler.cmdList.get(indexOfCmd).executeCommand();
					return;
				} else {
					strCommand = strCommand.substring(strCommand.indexOf(" ", strCommand.indexOf("--"))+1, strCommand.length());
				}
			}
			if ((strCommand.indexOf("-") != -1) && 
					((strCommand.indexOf("\"") == -1) || (strCommand.indexOf("\"", strCommand.indexOf("\"")+1) == -1))){
				System.out.println("�� �������� �������� ������ �� ����������.");
				return;
			}
			while(strCommand.indexOf("-") != -1){
				cmdFlag = strCommand.substring(strCommand.indexOf("-")+1, strCommand.indexOf(" "));
				cmdArgm = strCommand.substring(strCommand.indexOf("\"")+1, strCommand.indexOf("\"", strCommand.indexOf("\"")+1));
				Consoler.cmdList.get(indexOfCmd).addFlags(cmdFlag);
				Consoler.cmdList.get(indexOfCmd).addArgument(cmdArgm);
				if (strCommand.indexOf("\"", strCommand.indexOf("\"")+1)+2 < strCommand.length())
					strCommand = strCommand.substring(strCommand.indexOf("\"", strCommand.indexOf("\"")+1)+2, strCommand.lastIndexOf("\"")+1);
				else strCommand = "";
			}
			Consoler.cmdList.get(indexOfCmd).executeCommand();
		} else {
			System.out.println("\"" + cmdName + "\" �� �������� ������������������ ��������.");
		}
	}
	
	public static void setProgramAuthor(String name){
		ProgramAuthor = name;
	}
	public static String getProgramAuthor(){
		return ProgramAuthor;
	}
	public static ConsoleCommand register(ConsoleCommand cmd){
		Consoler.cmdList.add(cmd);
		return Consoler.cmdList.get(Consoler.cmdList.size()-1);
	}
	public static void listen(){
		sc = new Scanner(System.in);
		ConsoleCommand cmdExS = new cmdScriptExecute();
		ConsoleCommand cmdEx = new cmdExit();
		ConsoleCommand cmdHL = new cmdHelpList();
		ConsoleCommand cmdHLa = new cmdHelpListAlternative();
		cmdEx.setCommandName("exit")
		     .setHelpText("������� ���������.");
		cmdHL.setCommandName("help")
		     .setHelpText("����� ������ ������.");
		cmdHLa.setCommandName("?")
	     .setHelpText("�������������� ������� ������ ������� ������.");
		cmdExS.setCommandName("script")
			.setHelpText("������� ���������� ������� ���������� ������.")
			.setFullHelpText("������� ���������� ������� ���������� ������.\n"
					+ "-p \"<����_��_�����>\" - ��������� ������, ����������� �� ������� ������.\n"
					+ "-----------------------------------------------------\n"
					+ "������� ������� ��������� ��� �� ���������, ��� ������������ � �������.\n"
					+ "����������� ����� ���� ������ ������������ � ������ � ������ � �� �����\n"
					+ "������. ���������� ��� �� ����� ������� (#). ������ ������� ������ ����\n"
					+ "�� ����� ������, ������������ � �������� ���.");
		register(cmdEx);
		register(cmdHL);
		register(cmdHLa);
		register(cmdExS);
		System.out.println("����� ��������� ��������� Consoler v1.7.3");
		System.out.println("�����: �������� ����.");
		System.out.println("����� ���������: " + ProgramAuthor + ".");
		System.out.println("������� help, ����� �������� ������ ���� ��������� �������.");
		while(true){
			System.out.print("> ");
			Consoler.parseCommand(sc.nextLine());
		}
	}
}
