package springtraining;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainClass {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        GUI gui = (GUI) context.getBean("gui");

        gui.print("Начинаю работу...");

        ((ClassPathXmlApplicationContext) context).close();
    }

}
