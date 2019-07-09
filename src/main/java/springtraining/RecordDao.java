package springtraining;

import org.springframework.jdbc.core.JdbcTemplate;

public class RecordDao {

    private JdbcTemplate jdbcTemplate;

    public RecordDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clearDataBase(){
        String query = "DELETE FROM RECORDS";
        jdbcTemplate.update(query);
    }

    public void addRecord(Record record) {
        String query = "INSERT INTO RECORDS (ID, DATE, NAME, COUNT) VALUES(?,?,?,?)";
        jdbcTemplate.update(query, record.getId(), record.getDate(), record.getName(), record.getCount());
    }

}
