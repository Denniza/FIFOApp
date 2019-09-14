package service;

import jdk.nashorn.internal.runtime.ECMAException;
import repos.ProductDao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

public class ArgsValidationService {
    private Set<String> productsNames;

    public ArgsValidationService() {
        productsNames = new HashSet<>();
    }

    private boolean baseValidation(String[] args) {
        if (args[1].isEmpty()) try {
            throw new Exception("Некорректное название товара");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return productsNames.contains(args[1]);
    }

    public boolean existsProdValidation(String[] args) { //проверка, есть ли такой товар в базе данных
        if (!baseValidation(args)) {
            productsNames.add(args[1]);
            return true;
        } else return false;
    }

    public boolean purchaseAndDemandValidation(String[] args) {
        if (baseValidation(args)) {
            try {
                if (Integer.parseInt(args[3]) <= 0) throw new Exception("Стоимость должна быть больше 0");
                if (Integer.parseInt(args[2]) <= 0) throw new Exception("количество не может быть меньше 0");
                LocalDate.parse(args[4],
                        DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (NumberFormatException e) {
                System.out.println("стоимость и количество должны быть целыми числами");
                return false;
            } catch (DateTimeParseException e) {
                System.out.println("Ошибка ввода даты");
                return false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        } else return false;
        return true;
    }

    public boolean salesReportValidation(String[] args) {
        if (baseValidation(args)) {
            try {
                LocalDate.parse(args[2],
                        DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Ошибка ввода даты");
                return false;
            }
        } else return false;
        return true;
    }
}
