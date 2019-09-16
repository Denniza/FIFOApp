package service;

import org.junit.Before;
import org.junit.Test;
import repos.ProductDao;
import util.Commands;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class ProductServiceTest {

    static ProductService productService;
    static ProductDao productDao;
    static ArgsValidationService validationService;

    @Before
    public void init(){
        Properties properties;
        try {
            properties = new Properties();
            properties.load((ProductServiceTest.class.getClassLoader().getResourceAsStream("application-test.properties")));
            productDao = new ProductDao(properties);
            validationService = new ArgsValidationService();
            productService = new ProductService(productDao,validationService);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validationTest(){
        assertTrue(validationService.existsProdValidation(new String[]{"someCommand","iphone"}));
        assertFalse(validationService.existsProdValidation(new String[]{"someCommand","iphone"}));

        assertTrue(validationService.purchaseAndDemandValidation(new String[]{"PURCHASE","iphone","1","1000","01.01.2017"}));
        assertFalse(validationService.purchaseAndDemandValidation(new String[]{"PURCHASE","iphone","0","1000","01.01.2017"}));
        assertFalse(validationService.purchaseAndDemandValidation(new String[]{"DEMAND","iphone","1","-1000","01.01.2017"}));
        assertFalse(validationService.purchaseAndDemandValidation(new String[]{"DEMAND","iphone","1","1000","01.01.201754"}));

        assertTrue(validationService.salesReportValidation(new String[]{"SALESREPORT","iphone","01.01.2020"}));
        assertFalse(validationService.salesReportValidation(new String[]{"SALESREPORT","iphone","01.01.202cs0"}));
    }

    @Test
    public void executeCommandTest() {

        assertTrue(productService.executeCommand(new String[] {"NEWPRODUCT","iphone"}));
        assertFalse(productService.executeCommand(new String[] {"NEWPRODUCT","iphone"}));
        assertTrue(productService.executeCommand(new String[] {"PURCHASE","iphone","1","1000","01.01.2017"}));
        assertTrue(productService.executeCommand(new String[] {"PURCHASE","iphone","2","2000","01.01.2017"}));

        assertFalse(productService.executeCommand(new String[] {"PURCHASE","iphone","0","1000","01.01.2017"}));
        assertFalse(productService.executeCommand(new String[] {"PURCHASE","iphone","1","-34","01.01.2017"}));
        assertFalse(productService.executeCommand(new String[] {"PURCHASE","iphone","1","1000","01.01.20176"}));

        assertTrue(productService.executeCommand(new String[] {"DEMAND","iphone","2","5000","01.01.2018"}));
        assertFalse(productService.executeCommand(new String[] {"DEMAND","iphone","2","-32","01.01.2018"}));
        assertFalse(productService.executeCommand(new String[] {"DEMAND","iphone","2","-32","01.0ds1.2018"}));


        assertTrue(productService.executeCommand(new String[]{"SALESREPORT","iphone","01.01.2020"}));

    }
}