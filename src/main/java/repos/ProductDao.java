package repos;

import util.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//класс, обеспечивающий работу с базой данных
public class ProductDao extends BaseConfig {

    public ProductDao(Properties properties) {
        super(properties);
    }

    //метод возвращает список позицицй которые были проданы до указанной даты
    public List<Product> findByNameAndDate(String name, Date date){
        List<Product> result = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection= null;
        try {
            connection = getConnection();
            statement = connection
                    .prepareStatement("select * FROM Products where PRODUCT_NAME=? and SOLD_DATE <= ?");
            statement.setString(1, name);
            statement.setDate(2,date);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Product product = new Product();
                product.setName(resultSet.getString("PRODUCT_NAME"));
                product.setPrice(resultSet.getInt("PRICE"));
                product.setPurchaseDate(resultSet.getDate("PURCHASE_DATE").toLocalDate());
                product.setSoldPrice(resultSet.getInt("SOLD_PRICE"));
                product.setSoldDate(resultSet.getDate("SOLD_DATE").toLocalDate());
                product.setSold(resultSet.getBoolean("IS_SOLD"));
                result.add(product);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return result;
    }

    //метод добавляет указанное количество какого-либо продукта
    public boolean addProduct(String productName, int amount, int price, LocalDate date){
        PreparedStatement statement = null;
        Connection connection= null;
        try {
            connection = getConnection();
            for(int i=0;i<amount;i++) {
                statement = connection
                        .prepareStatement("insert into Products" +
                                "(PRODUCT_NAME, PRICE, PURCHASE_DATE, IS_SOLD)" +
                                "values (?,?,?,?)");
                statement.setString(1, productName);
                statement.setInt(2, price);
                statement.setDate(3, Date.valueOf(date));
                statement.setBoolean(4, false);
                statement.execute();
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(connection);
        }
        return true;
    }

    //метод обновляет данные в БД, выставляя цену продажи и дату для нужного количества продуктов
    public boolean demandProduct(String productName, int amount, int price, LocalDate date){
        PreparedStatement statement;
        Connection connection= null;
        try {
            connection = getConnection();
                statement = connection
                        .prepareStatement("update Products " +
                                        "SET SOLD_PRICE = ?," +
                                        "SOLD_DATE = ?," +
                                        "IS_SOLD = true " +
                                        "WHERE PRODUCT_ID IN(SELECT PRODUCT_ID FROM Products " +
                                        "where PRODUCT_NAME = ? AND IS_SOLD = false " +
                                        "ORDER BY(PURCHASE_DATE) LIMIT ?)");
                statement.setInt(1,price);
                statement.setDate(2,Date.valueOf(date));
                statement.setString(3,productName);
                statement.setInt(4,amount);
                statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(connection);
        }
        return true;
    }
}
