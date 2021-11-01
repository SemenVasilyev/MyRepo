package spring.mvc;



import spring.mvc.validation.CheckEmail;

import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;

public class Employee {
    @Size(min = 2, message = "name must be mi 2 symbol")
    private String name;
    //@NotNull(message = "surname is required field") только для null
    //@NotEmpty(message = "surname is required field") для пустой строки ""
    @NotBlank(message = "surname is required field") // для пустой строки и пробелов "    "
    private String surname;
    @Min(value = 500, message = "must be greater than 499")
    @Max(value = 1000, message = "must be less than 1001")
    private int salary;
    private String department;
    private Map<String,String> departments;
    private String carBrand;
    private Map<String,String> carBrands;
    private String[] languages;
    private Map<String,String> languageMap;
    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{2}", message = "please use pattern XXX-XX-XX")
    private String phone;
    @CheckEmail(value = "abc.com" , message = "email must ends with abc.com")
    private String email;

    public Employee() {
        departments = new HashMap<>();
        departments.put("HR", "Human Resources");
        departments.put("IT", "Information Technology");
        departments.put("Sales", "Sales");

        carBrands = new HashMap<>();
        carBrands.put("BMW","BMW");
        carBrands.put("Audi","Audi");
        carBrands.put("Mercedes-Benz","MB");

        languageMap = new HashMap<>();
        languageMap.put("English","EN");
        languageMap.put("Deutsch","DE");
        languageMap.put("French","FR");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Map<String, String> getDepartments() {
        return departments;
    }

    public void setDepartments(Map<String, String> departments) {
        this.departments = departments;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public Map<String, String> getCarBrands() {
        return carBrands;
    }

    public void setCarBrands(Map<String, String> carBrands) {
        this.carBrands = carBrands;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public Map<String, String> getLanguageMap() {
        return languageMap;
    }

    public void setLanguagMap(Map<String, String> languagggeMap) {
        this.languageMap = languagggeMap;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surdName='" + surname + '\'' +
                ", age=" + salary +
                ", department='" + department + '\'' +
                '}';
    }
}
