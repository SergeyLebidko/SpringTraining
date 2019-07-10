package springtraining;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RecordDao {

    private JdbcTemplate jdbcTemplate;
    private TransactionTemplate transactionTemplate;

    private Logger logger;

    public RecordDao(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate, Logger logger) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        this.logger = logger;
    }

    public void clearDataBase() {
        String query = "DELETE FROM RECORDS";
        jdbcTemplate.update(query);

        logger.toLog("База данных очищена");
    }

    public void addRecord(Record record) {
        TransactionCallbackWithoutResult callbackWithoutResult = new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                //Проверяем, есть ли уже запись с таким ID
                String query = "SELECT COUNT(*) FROM RECORDS WHERE ID=?";
                RowMapper<Integer> mapRow = new RowMapper<Integer>() {
                    @Override
                    public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                        return resultSet.getInt(1);
                    }
                };

                //Если есть - выполняем откат
                int countRecord = jdbcTemplate.queryForObject(query, new Object[]{record.getId()}, mapRow);
                if (countRecord > 0) {
                    transactionStatus.setRollbackOnly();
                    logger.toLog("Запись " + record.toString() + " не добавлена. Количество id с таким же значением " + countRecord);
                    return;
                }

                //Если нет - выполняем транзакцию
                query = "INSERT INTO RECORDS (ID, DATE, NAME, COUNT) VALUES(?,?,?,?)";
                jdbcTemplate.update(query, record.getId(), record.getDate(), record.getName(), record.getCount());

                logger.toLog("Добавлена запись: " + record.toString());
            }
        };
        transactionTemplate.execute(callbackWithoutResult);
    }

    public List getRecordsWithCountLimit(int countLimit) {
        TransactionCallback<List<Record>> transactionCallback = new TransactionCallback<List<Record>>() {
            @Override
            public List<Record> doInTransaction(TransactionStatus transactionStatus) {
                String query = "SELECT ID, DATE, NAME, COUNT FROM RECORDS WHERE COUNT<?";
                RowMapper<Record> mapper = new RowMapper<Record>() {
                    @Override
                    public Record mapRow(ResultSet resultSet, int i) throws SQLException {
                        int id = resultSet.getInt(1);
                        LocalDate date = LocalDate.parse(resultSet.getString(2));
                        String name = resultSet.getString(3);
                        int count = resultSet.getInt(4);
                        return new Record(id, date, name, count);
                    }
                };
                List<Record> list = jdbcTemplate.query(query, mapper, countLimit);
                return list;
            }
        };
        List<Record> list = transactionTemplate.execute(transactionCallback);
        return list;
    }

}
