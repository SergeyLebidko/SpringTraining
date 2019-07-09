package springtraining;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainClass {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");

        //Создаем графический интерфейс
        GUI gui = (GUI) context.getBean("gui");

        //Работаем с БД
        //Создаем объект для работы с БД
        RecordDao recordDao = (RecordDao) context.getBean("recordDao");

        //Очищаем БД от имеющихся данных
        recordDao.clearDataBase();

        //Генерируем список объектов и записываем их в БД
        RecordGenerator generator = (RecordGenerator) context.getBean("record_generator");
        int listSize = 200;
        for (int i = 0; i < listSize; i++) {
            recordDao.addRecord(generator.getRecord());
        }

        //Закрываем контекст
        ((ClassPathXmlApplicationContext) context).close();
    }

    private static void workDataBaseSpring(ApplicationContext context) {

    }

}
