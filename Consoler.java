package consoler;

import java.util.Scanner;
import java.util.ArrayList;

public class Consoler {	
	private static ArrayList<ConsoleCommand> cmdList = new ArrayList<ConsoleCommand>();
	private static Scanner sc;
	private static String ProgramAuthor = "Багданов Илья";
	
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
				System.out.println("Список команд:");
				System.out.println("----------------------------------------");
				for (int i = 0; i < cmdList.size(); i++){
					System.out.println(Integer.toString(i+1) + ". " + cmdList.get(i).getName() + " - " + cmdList.get(i).getHelpText());
				}
				System.out.println("----------------------------------------");
				System.out.println("Для получения подробной справки используйте флаг \"?\". Пример: help -? \"exit\".");
			} else {
				int e;
				if ((e = isExists(this.getArgms("?"))) == -1){
					System.out.println("\"" + this.getArgms("?") + "\" не является зарегистрированной командой.");
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
				System.out.println("Список команд:");
				System.out.println("----------------------------------------");
				for (int i = 0; i < cmdList.size(); i++){
					System.out.println(Integer.toString(i+1) + ". " + cmdList.get(i).getName() + " - " + cmdList.get(i).getHelpText());
				}
				System.out.println("----------------------------------------");
				System.out.println("Для получения подробной справки используйте флаг \"?\". Пример: help -? \"exit\".");
			} else {
				int e;
				if ((e = isExists(this.getArgms("?"))) == -1){
					System.out.println("\"" + this.getArgms("?") + "\" не является зарегистрированной командой.");
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
				System.out.println("Не указан путь до файла, который нужно исполнить.\n"
						+ "Используйте script -p \"<путь_до_файла>\", чтобы запустить скрипт.");
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
		//Узнать название команды.
		String cmdName;
		if (strCommand.indexOf(" ") != -1)
			cmdName = strCommand.substring(0, strCommand.indexOf(" "));
		else cmdName = strCommand.substring(0, strCommand.length());
		String cmdFlag;
		String cmdArgm;
		strCommand = strCommand.substring(strCommand.indexOf(" ")+1, strCommand.length());
		//Узнать, существует ли такая команда.
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
				System.out.println("Не найденно значение одного из аргументов.");
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
			System.out.println("\"" + cmdName + "\" не является зарегистрированной командой.");
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
		     .setHelpText("Закрыть программу.");
		cmdHL.setCommandName("help")
		     .setHelpText("Вывод списка команд.");
		cmdHLa.setCommandName("?")
	     .setHelpText("Альтернативная команда вывода списока команд.");
		cmdExS.setCommandName("script")
			.setHelpText("Команда выполнения внешних скриптовых файлов.")
			.setFullHelpText("Команда выполнения внешних скриптовых файлов.\n"
					+ "-p \"<путь_до_файла>\" - выполнить скрипт, находящийся по данному адресу.\n"
					+ "-----------------------------------------------------\n"
					+ "Скрипты пишутся используя тот же синтаксис, что используется в консоли.\n"
					+ "Комментарии могут быть только однострочные и только с начала и до конца\n"
					+ "строки. Начинаются они со знака решётки (#). Каждая команда должна быть\n"
					+ "на своей строке, разделителей в скриптах нет.");
		register(cmdEx);
		register(cmdHL);
		register(cmdHLa);
		register(cmdExS);
		System.out.println("Среда командной обработки Consoler v1.7.3");
		System.out.println("Автор: Багданов Илья.");
		System.out.println("Автор программы: " + ProgramAuthor + ".");
		System.out.println("Введите help, чтобы получить список всех возможных комманд.");
		while(true){
			System.out.print("> ");
			Consoler.parseCommand(sc.nextLine());
		}
	}
}
