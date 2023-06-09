package pet.store.controller.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

/**
 * A DTO class used to transform the PetStore object to and from JSON.
 * @author eyusias
 *
 */
@Data
@NoArgsConstructor
public class PetStoreData {
  private Long petStoreId;
  private String petStoreName;
  private String petStoreAddress;
  private String petStoreCity;
  private String petStoreState;
  private String petStoreZip;
  private String petStorePhone;
  private Set<PetStoreEmployee> employees = new HashSet<>();
  private Set<PetStoreCustomer> customers = new HashSet<>();
 
  /**
   * A constructor that sets PetStoreData DTO fields.
   * @param petStore PetStore entity class.
   */
  public PetStoreData(PetStore petStore) {
    petStoreId = petStore.getPetStoreId();
    petStoreName = petStore.getPetStoreName();
    petStoreAddress = petStore.getPetStoreAddress();
    petStoreCity = petStore.getPetStoreCity();
    petStoreState = petStore.getPetStoreState();
    petStoreZip = petStore.getPetStoreZip();
    petStorePhone = petStore.getPetStorePhone();
    
    // Setting PetStoreEmployee fields in PetStoreData DTO 
    for(Employee employee : petStore.getEmployees()) {
      employees.add(new PetStoreEmployee(employee));
    }
    
    // Setting PetStoreCustomer fields in PetStoreData DTO
    for(Customer customer : petStore.getCustomers()) {
      customers.add(new PetStoreCustomer(customer));
    }
  }

  /**
   * An inner class inside PetStoreData DTO used to transfer 
   * Customer object, associated with PetStoreData,  to and from JSON.
   * @author eyusias
   *
   */
  @Data
  @NoArgsConstructor
  public static class PetStoreCustomer{
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    
    /**
     * A constructor that sets PetStoreCustomer DTO fields.
     * @param customer Customer entity class.
     */
    public PetStoreCustomer(Customer customer) {
      customerId = customer.getCustomerId();
      customerFirstName = customer.getCustomerFirstName();
      customerLastName = customer.getCustomerLastName();
      customerEmail = customer.getCustomerEmail();
    }
  }
  
  /**
   * An inner class inside PetStoreData DTO used to transfer 
   * Employee object, associated with PetStoreData,  to and from JSON.
   * @author eyusias
   *
   */
  @Data
  @NoArgsConstructor
  public static class PetStoreEmployee{
    private Long employeeId;    
    private String employeeFirstName;
    private String employeeLastName;
    private String employeePhone;
    private String employeeJobTitle;    
    
    /**
     * A constructor that sets PetStoreEmployee DTO fields.
     * @param employee Employee entity class
     */
    public PetStoreEmployee(Employee employee) {
      employeeId = employee.getEmployeeId();
      employeeFirstName = employee.getEmployeeFirstName();
      employeeLastName = employee.getEmployeeLastName();
      employeePhone = employee.getEmployeePhone();
      employeeJobTitle = employee.getEmployeeJobTitle();
    }
  }
}
