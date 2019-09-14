import repos.ProductDao;
import service.ArgsValidationService;
import service.ProductService;
import util.Commands;

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

        while (true){
            Scanner scanner = new Scanner(System.in);
            String [] arguments = scanner.nextLine().split(" ");
            if(arguments[0].equals("exit")) break;
            productService.executeCommand(arguments);
        }
//        productService.executeCommand(Commands.NEWPRODUCT, new String[]{"iphone"});
//        productService.executeCommand(Commands.NEWPRODUCT, new String[]{"iphone"});
//        productService.executeCommand(Commands.PURCHASE, new String[]{"iphone", "1", "1000", "01.01.2017"});
//        productService.executeCommand(Commands.PURCHASE, new String[]{"iphone", "2", "2000", "01.01.2017"});
//        productService.executeCommand(Commands.DEMAND, new String[]{"iphone", "2", "5000", "01.03.2017"});
//        productService.executeCommand(Commands.SALESREPORT, new String[]{"iphone", "01.01.2020"});

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
