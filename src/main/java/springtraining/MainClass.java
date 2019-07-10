package springtraining;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDate;
import java.util.LinkedList;
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
        int listSize = 15;
        for (int i = 0; i < listSize; i++) {
            recordDao.addRecord(generator.getRecord());
        }

        //Получаем список объектов, у которых count не более заданной величины
        List<Record> list = recordDao.getRecordsWithCountLimit(20);
        logger.toLog("");
        logger.toLog("Перечень объектов, у которых count < 20:");
        for (Record record : list) {
            logger.toLog(record.toString());
        }

        //Пытаемся добавить в БД заведомо некоррекнтую запись
        logger.toLog("");
        Record badRecord = new Record(10, LocalDate.now(), "Test rollback", 0);
        recordDao.addRecord(badRecord);

        //Снова очищаем БД и добавляем список объектов. Все объекты в списке корректны (транзакция должна завершиться успешно)
        logger.toLog("");
        logger.toLog("В рамках одной транзакции добавляем список объектов");
        recordDao.addRecordList(createRecordList(10));

        //Добавляем в БД список объектов, один из которых некорректен (транзакция не проходит, ни один объект не должен быть добавлен).
        logger.toLog("");
        logger.toLog("В рамках одной транзакции добавляем список с некорректным объектом");
        list = createRecordList(10);
        list.add(new Record(5, LocalDate.now(), "Test", 0));
        recordDao.addRecordList(list);

        //Выводим текущее содержимое БД
        logger.toLog("");
        logger.toLog("Текущее содержимое БД:");
        list = recordDao.getRecordsWithCountLimit(Record.getCountLimit() + 1);
        for (Record record: list){
            logger.toLog(record.toString());
        }

        //Закрываем контекст
        logger.toLog("");
        ((ClassPathXmlApplicationContext) context).close();
    }

    private static List<Record> createRecordList(int listSize) {
        List<Record> list = new LinkedList<>();
        listSize = 20;
        for (int i = 0; i < listSize; i++) {
            list.add(new Record());
        }
        return list;
    }

}
