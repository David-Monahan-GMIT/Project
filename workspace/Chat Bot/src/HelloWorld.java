import org.alicebot.ab.*;
public class HelloWorld {

	public static void main(String[] args) {
		String botName = new String("Bob");
		Bot bot = new Bot(botName);
		Chat chatSession = new Chat(bot);
	}
}
