package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {

  @Autowired
  private PetStoreDao petStoreDao;

  @Autowired
  private EmployeeDao employeeDao;

  @Autowired
  private CustomerDao customerDao;


  /**
   * A service layer method that save or update PetStoreData.
   * 
   * @param petStoreData PetStoreData
   * @return created or modified PetStoreData
   */
  @Transactional(readOnly = false)
  public PetStoreData savePetStore(PetStoreData petStoreData) {

    PetStore petStore = findOrCreatePetStore(petStoreData.getPetStoreId());
    copyPetStoreFields(petStore, petStoreData);

    return new PetStoreData(petStoreDao.save(petStore));
  }


  /**
   * A convenient method for setting pet store object fields.
   * 
   * @param petStore PetStore
   * @param petStoreData PetStoreData
   */
  private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
    petStore.setPetStoreId(petStoreData.getPetStoreId());
    petStore.setPetStoreName(petStoreData.getPetStoreName());
    petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
    petStore.setPetStoreCity(petStoreData.getPetStoreCity());
    petStore.setPetStoreState(petStoreData.getPetStoreState());
    petStore.setPetStoreZip(petStoreData.getPetStoreZip());
    petStore.setPetStorePhone(petStoreData.getPetStorePhone());
  }


  /**
   * A method that accepts pet store id and returns pet store object associated with a give Id if
   * pet store Id is not null, otherwise creating new pet store object.
   * 
   * @param petStoreId Long
   * @return pet store object.
   */
  private PetStore findOrCreatePetStore(Long petStoreId) {
    PetStore petStore;
    if (Objects.isNull(petStoreId)) {
      petStore = new PetStore();
    } else {
      petStore = findPetStore(petStoreId);
    }

    return petStore;
  }


  /**
   * A method finds pet store detail from a given pet store Id.
   * 
   * @param petStoreId
   * @return pet store object.
   * @throws NoSuchElementException pet store Id is not found.
   */
  private PetStore findPetStore(Long petStoreId) {
    return petStoreDao.findById(petStoreId).orElseThrow(
        () -> new NoSuchElementException("Pet store with ID=" + petStoreId + " was not found."));
  }


  @Transactional(readOnly = false)
  public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
    PetStore petStore = findPetStore(petStoreId);
    Long employeeId = petStoreEmployee.getEmployeeId();

    Employee employee = findOrCreateEmployee(petStoreId, employeeId);

    copyEmployeeFields(employee, petStoreEmployee);

    // Setting relationships
    employee.setPetStore(petStore);
    petStore.getEmployees().add(employee);

    Employee dbEmployee = employeeDao.save(employee);
    return new PetStoreEmployee(dbEmployee);
  }


  private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
    employee.setEmployeeId(petStoreEmployee.getEmployeeId());
    employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
    employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
    employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
    employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
  }


  private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
    if (Objects.isNull(employeeId)) {
      return new Employee();
    } else {
      return findEmployeeById(petStoreId, employeeId);
    }
  }


  private Employee findEmployeeById(Long petStoreId, Long employeeId) {
    Employee employee = findEmployeeById(employeeId);

    if (employee.getPetStore().getPetStoreId() == petStoreId) {
      return employee;
    } else {
      throw new IllegalArgumentException(
          "Employee with ID=" + employeeId + " is not worked in pet store with ID=" + petStoreId);
    }
  }


  private Employee findEmployeeById(Long employeeId) {
    Employee employee = employeeDao.findById(employeeId).orElseThrow(
        () -> new NoSuchElementException("Employee with ID=" + employeeId + " was not found."));

    return employee;
  }


  @Transactional(readOnly = false)
  public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
    PetStore petStore = findPetStore(petStoreId);
    Long customerId = petStoreCustomer.getCustomerId();

    Customer customer = findOrCreateCustomer(petStoreId, customerId);

    copyCustomerFields(customer, petStoreCustomer);

    // Setting relationships
    customer.getPetStores().add(petStore);
    petStore.getCustomers().add(customer);

    Customer dbCustomer = customerDao.save(customer);
    return new PetStoreCustomer(dbCustomer);
  }


  private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
    if (Objects.isNull(customerId)) {
      return new Customer();
    } else {
      return findCustomerById(petStoreId, customerId);
    }
  }


  private Customer findCustomerById(Long petStoreId, Long customerId) {
    Customer customer = findCustomerById(customerId);

    for (PetStore petStore : customer.getPetStores()) {
      if (petStore.getPetStoreId() == petStoreId) {
        return customer;
      }
    }
    
    throw new IllegalArgumentException(
        "Customer with ID=" + customerId + " is not worked in pet store with ID=" + petStoreId);

  }


  private Customer findCustomerById(Long customerId) {
    Customer customer = customerDao.findById(customerId).orElseThrow(
        () -> new NoSuchElementException("Customer with ID=" + customerId + " was not found."));

    return customer;
  }
  
  
  private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
    customer.setCustomerId(petStoreCustomer.getCustomerId());
    customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
    customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
    customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
  }


  @Transactional(readOnly = true)
  public List<PetStoreData> retrieveAllPetStore() {
    List<PetStore> petStores = petStoreDao.findAll();
    List<PetStoreData> results = new LinkedList<>();
    
    for(PetStore petStore : petStores) {
      PetStoreData petStoreData = new PetStoreData(petStore);
      // Removing customer and employee from pet store data object before adding to the pet store data list
      petStoreData.getCustomers().clear();
      petStoreData.getEmployees().clear();
      
      results.add(petStoreData);
    }
    return results;
  }


  @Transactional
  public PetStoreData retrievePetStoreById(Long petStoreId) {
    return new PetStoreData(findPetStore(petStoreId));
  }


  @Transactional(readOnly = false)
  public void deletePetStoreById(Long petStoreId) {
    PetStore petStore = findPetStore(petStoreId);
    petStoreDao.delete(petStore);
    
  }
}
