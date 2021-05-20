package consoler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;

public class ConsoleScript {
	private File file;
	private FileReader readStream;
	private ArrayDeque<String> fileText;
	private String filePath;
	
	public ConsoleScript(String filePath){
		this.filePath = filePath;
		this.fileText = new ArrayDeque<>();
		this.file = new File(this.filePath);
		try {
			this.readStream = new FileReader(this.file);
			System.out.println("Файл найден. Загрузка...");
			this.setFileText();
			System.out.println("Файл загружен. Исполнение...");
			this.executeScript();
		} catch(Exception e){
			System.out.println("Ошибка при создании потоков ввода\\вывода.");
			e.printStackTrace(System.out);
		}
	}
	
	private void setFileText(){
		String s = "";
		char buff[] = new char[(int) this.file.length()];
		try {
			this.readStream.read(buff);
			for (int i = 0; i < this.file.length(); i++){
				if (buff[i] == '#'){
					while (buff[i] != '\n')	i++;
					i++;
				}
				if (buff[i] != '\n' && buff[i] != '\r'){
					s += Character.toString(buff[i]);
					continue;
				} else i += 1;
				this.fileText.addLast(s);
				s = "";
			}
			this.fileText.addLast(s);
			s = "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void executeScript(){
		for (String s : this.fileText){
			Consoler.parseCommand(s);
		}
	}
}
