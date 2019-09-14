package service;

import repos.ProductDao;
import util.Commands;
import util.Product;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

//класс - сервис, обеспечивает бизнес-логику приложения, все операции осуществляются через 1 метод,
//при необходимости функциональность можно расширить
public class ProductService {
    private ProductDao productDao;
    private ArgsValidationService validationService;

    public ProductService(ProductDao productDao, ArgsValidationService validationService) {
        this.productDao = productDao;
        this.validationService = validationService;
    }

    public void executeCommand(String [] args){
        try {
            switch (Commands.valueOf(args[0])) {
                case NEWPRODUCT:
                    newProductCommand(args);
                    break;
                case PURCHASE:
                    purchaseCommand(args);
                    break;
                case DEMAND:
                    demandCommand(args);
                    break;
                case SALESREPORT:
                    salesReportCommand(args);
                    break;
            }
        }catch (IllegalArgumentException e){
            System.out.println("ERROR  (Несуществующая команда) ");
        }
    }

    private void newProductCommand(String [] args){
        try {
            if (!validationService.existsProdValidation(args)){
                System.out.println("ERROR (товар уже есть в базе)");
            } else{
                System.out.println("OK");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void purchaseCommand(String []args){
        if(validationService.purchaseAndDemandValidation(args)){
            String productName = args[1];
            int price = Integer.parseInt(args[3]);
            int amount = Integer.parseInt(args[2]);
            LocalDate date = LocalDate.parse(args[4],
                    DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            productDao.addProduct(productName,amount,price,date);
            System.out.println("OK");
        } else System.out.println("ERROR");
    }

    private void demandCommand(String [] args){
        if(validationService.purchaseAndDemandValidation(args)){
            String productName = args[1];
            int amount = Integer.parseInt(args[2]);
            int price = Integer.parseInt(args[3]);
            LocalDate date = LocalDate.parse(args[4],
                    DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            productDao.demandProduct(productName,amount,price,date);
            System.out.println("OK");
        } else System.out.println("ERROR");
    }

    private void salesReportCommand(String [] args){
        if(validationService.salesReportValidation(args)){
            String productName= args[1];
            Date date = Date.valueOf(LocalDate.parse(args[2],
                    DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            List<Product> result = productDao.findByNameAndDate(productName,date);
            System.out.println(FIFOCostPrice(result));
        } else System.out.println("ERROR");
    }

    //метод рассчёта прибыли
    private int FIFOCostPrice(List<Product> productList){
        int purchaseCost = (productList.stream()
                                .flatMapToInt(s-> IntStream.of(s.getPrice())).sum());
        int soldCost = (productList.stream()
                                .flatMapToInt(s-> IntStream.of(s.getSoldPrice())).sum());
        return soldCost - purchaseCost;
    }
}
