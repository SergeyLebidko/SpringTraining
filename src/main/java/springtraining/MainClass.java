package springtraining;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.LinkedList;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");

        //Создаем интерфейс
        GUI gui = (GUI) context.getBean("gui");

        //Создаем подключение к БД
        Logger logger = (Logger) context.getBean("logger");
        DBHandler dbHandler = (DBHandler) context.getBean("dbhandler");
        dbHandler.cleanTables();

        //Генерируем список объектов
        RecordGenerator generator = (RecordGenerator) context.getBean("record_generator");
        int listSize = 200;
        List<Record> records = new LinkedList<>();
        for (int i = 0; i < listSize; i++) {
            records.add(generator.getRecord());
        }

        //Записываем объекты в БД
        dbHandler.addRecordsToBase(records);

        //Закрываем контекст
        ((ClassPathXmlApplicationContext) context).close();
    }

}
