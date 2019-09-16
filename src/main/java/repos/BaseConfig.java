package repos;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

//класс, конфигурирующий базу данных, данные базы берутся из файла application.properties
public class BaseConfig {
    private Properties properties;

    static {
        try {
            DriverManager.registerDriver(new Driver());
            System.out.println("Driver connected");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка загрузки драйвера DB");
        }
    }

    BaseConfig(Properties properties){
        this.properties = properties;
        try {
            init();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка создания соединения с БД и инициализацией таблицы");
        }
    }

    private void init() throws SQLException {

        Connection connection = getConnection();

        connection.createStatement().execute("DROP TABLE IF EXISTS Products");
        connection.createStatement().execute("CREATE TABLE Products(" +
                "PRODUCT_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY , " +
                "PRODUCT_NAME varchar (60) not null , " +
                "PRICE INT, " +
                "PURCHASE_DATE date," +
                "SOLD_PRICE int," +
                "SOLD_DATE date," +
                "IS_SOLD BOOLEAN," +
                "PRIMARY KEY (PRODUCT_ID))");
        closeConnection(connection);
        System.out.println("База инициализирована");
    }

    Connection getConnection(){
        Connection connection=null;
        try {
            connection = DriverManager.getConnection(properties.getProperty("datasourceUrl"),
                    properties.getProperty("datasourceUserName"), properties.getProperty("datasourcePassword"));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка создания соединения с базой данных");
        }
        return connection;
    }

    void closeConnection(Connection connection){
        if(connection==null) return;
        try{
            connection.close();
        }catch (SQLException e){
            System.out.println("Ошибка закрытия соединения с базой");
        }
    }

}
