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

    public boolean executeCommand(String [] args){
        try {
            switch (Commands.valueOf(args[0])) {
                case NEWPRODUCT:
                    return newProductCommand(args);
                case PURCHASE:
                    return purchaseCommand(args);
                case DEMAND:
                    return demandCommand(args);
                case SALESREPORT:
                    return salesReportCommand(args);
            }
        }catch (IllegalArgumentException e){
            System.out.println("ERROR  (Несуществующая команда) ");
        }
        return false;
    }

    private boolean newProductCommand(String [] args){
        try {
            if (!validationService.existsProdValidation(args)){
                System.out.println("ERROR (товар уже есть в базе)");
                return false;
            } else{
                System.out.println("OK");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean purchaseCommand(String []args){
        if(validationService.purchaseAndDemandValidation(args)){
            String productName = args[1];
            int price = Integer.parseInt(args[3]);
            int amount = Integer.parseInt(args[2]);
            LocalDate date = LocalDate.parse(args[4],
                    DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            productDao.addProduct(productName,amount,price,date);
            System.out.println("OK");
            return true;
        } else System.out.println("ERROR");
        return false;
    }

    private boolean demandCommand(String [] args){
        if(validationService.purchaseAndDemandValidation(args)){
            String productName = args[1];
            int amount = Integer.parseInt(args[2]);
            int price = Integer.parseInt(args[3]);
            LocalDate date = LocalDate.parse(args[4],
                    DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            productDao.demandProduct(productName,amount,price,date);
            System.out.println("OK");
            return true;
        } else System.out.println("ERROR");
        return false;
    }

    private boolean salesReportCommand(String [] args){
        if(validationService.salesReportValidation(args)){
            String productName= args[1];
            Date date = Date.valueOf(LocalDate.parse(args[2],
                    DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            List<Product> result = productDao.findByNameAndDate(productName,date);
            System.out.println(FIFOCostPrice(result));
            return true;
        } else System.out.println("ERROR");
        return false;
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
