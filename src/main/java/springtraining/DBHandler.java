package springtraining;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DBHandler {

    private String jdbcDriverName;
    private String connectionString;
    private Connection connection;
    private Statement statement;

    private PreparedStatement addRecordsStmt;

    private boolean successfulConnection;

    private Logger logger;

    public DBHandler(String jdbcDriverName, String connectionString, Logger logger) {
        this.jdbcDriverName = jdbcDriverName;
        this.connectionString = connectionString;
        this.logger = logger;
        successfulConnection = false;
    }

    public void initConnection() throws Exception {
        try {
            Class.forName(jdbcDriverName);
            connection = DriverManager.getConnection(connectionString);
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            addRecordsStmt = connection.prepareStatement("INSERT INTO RECORDS (ID, DATE, NAME, COUNT) VALUES(?,?,?,?)");
        } catch (Exception ex) {
            logger.toLog("Не удалось подключиться к БД. Ошибка: " + ex.getMessage());
            successfulConnection = false;
            return;
        }
        logger.toLog("Подкючение к БД выполнено успешно");
        successfulConnection = true;
    }

    public void cleanTables() {
        try {
            if (!successfulConnection) throw new Exception("Подключение к БД не было установлено");
            String query = "SELECT * FROM sqlite_master WHERE type=\"table\"";
            ResultSet resultSet = statement.executeQuery(query);

            String tableName;
            List<String> tableList = new LinkedList<>();
            while (resultSet.next()) {
                tableName = resultSet.getString("name");
                tableList.add(tableName);
            }

            for (String name : tableList) {
                query = "DELETE FROM " + name;
                statement.executeUpdate(query);

            }
        } catch (Exception ex) {
            logger.toLog("При очистке таблиц БД произошла ошибка: " + ex.getMessage());
            return;
        }
        logger.toLog("Таблицы БД успешно очищены");
    }

    public void addRecordsToBase(List<Record> list) {
        try {
            for (Record record : list) {
                addRecordsStmt.setInt(1, record.getId());
                addRecordsStmt.setString(2, record.getDate().toString());
                addRecordsStmt.setString(3, record.getName());
                addRecordsStmt.setInt(4, record.getCount());
                addRecordsStmt.executeUpdate();
                logger.toLog("Добавлено в БД: "+record.toString());
            }
            connection.commit();
        } catch (Exception ex1) {
            logger.toLog("Не удалось добавить список записей в БД. Ошибка: " + ex1.getMessage());
            try {
                connection.rollback();
            } catch (Exception ex2) {
                logger.toLog("Не удалось отменить транзакцию. Ошибка: " + ex2.getMessage());
            }
            return;
        }
    }

    public void close() {
        try {
            statement.close();
            addRecordsStmt.close();
            connection.close();
        } catch (Exception ex) {
            logger.toLog("Подключение к БД закрыто некорректно. Ошибка: " + ex.getMessage());
            return;
        }
        logger.toLog("Подключение к БД успешно закрыто");
    }

}
