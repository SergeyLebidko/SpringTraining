package springtraining;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");

        //Создаем графический интерфейс
        GUI gui = (GUI) context.getBean("gui");
        Logger logger = (Logger) context.getBean("logger");

        //Работаем с БД
        //Создаем объект для работы с БД
        RecordDao recordDao = (RecordDao) context.getBean("recordDao");

        //Очищаем БД от имеющихся данных
        recordDao.clearDataBase();

        //Генерируем список объектов и записываем их в БД
        RecordGenerator generator = (RecordGenerator) context.getBean("record_generator");
        int listSize = 50;
        for (int i = 0; i < listSize; i++) {
            recordDao.addRecord(generator.getRecord());
        }

        //Добавляем в БД заведомо некорректную запись
        Record badRecord = new Record(10, LocalDate.now(), "Test rollback", 0);
        recordDao.addRecord(badRecord);

        //Получаем список объектов, у которых count не более заданной величины
        List<Record> list = recordDao.getRecordsWithCountLimit(20);
        logger.toLog("");
        logger.toLog("Перечень объектов, у которых count < 20:");
        for (Record record: list){
            logger.toLog(record.toString());
        }

        //Закрываем контекст
        ((ClassPathXmlApplicationContext) context).close();
    }

}
