import repos.ProductDao;
import service.ArgsValidationService;
import service.ProductService;
import util.Commands;

import javax.xml.bind.SchemaOutputResolver;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Application {
    private static Properties properties;

    public static void main(String[] args) {
        init();
        ProductDao productDao = new ProductDao(properties);
        ArgsValidationService argsValidationService = new ArgsValidationService();
        ProductService productService = new ProductService(productDao,argsValidationService);

        System.out.println("Добро пожаловать! Ниже вы увидите список доступных команд. Для выхода введите 'exit'\n" +
                "параметры вводятся через пробел\n" +
                "NEWPRODUCT <name> - Создать товар - на вход подается уникальное\n" +
                "наименование товара\n" +
                "PURCHASE <name> <amount> <price> <date> - Закупить товар - на вход подается\n" +
                "наименование товара, кол-во закупленного товара, цена единицы товара и дата\n" +
                "закупки\n" +
                "DEMAND <name> <amount> <price> <date> - Продать товар - на вход подается\n" +
                "наименование товара, кол-во проданного товара, цена единицы товара и дата\n" +
                "продажи\n" +
                "SALESREPORT <name> <date> - Рассчитать прибыльность - на вход подается\n" +
                "наименование товара и дата. Результат - прибыль на указанную дату.");

        while (true){
            Scanner scanner = new Scanner(System.in);
            String [] arguments = scanner.nextLine().split(" ");
            if(arguments[0].equals("exit")) break;
            productService.executeCommand(arguments);
        }

    }
    private static void init(){
        try {
            properties = new Properties();
            properties.load(Application.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
