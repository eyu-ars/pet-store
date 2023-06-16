package pet.store.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
  
  @Autowired
  private PetStoreService petStoreService;
  
  
  /**
   * A method handles HTTP post request.
   * @param petStoreData PetStoreData as JSON
   * @return created PetStoreData object as JSON
   */
  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public PetStoreData savePetStore(@RequestBody PetStoreData petStoreData) {
    log.info("Creating pet store {}", petStoreData);    
    return petStoreService.savePetStore(petStoreData);
  }
  
  
  /**
   * A method handles HTTP put request.
   * @param petStoreId Long
   * @param petStoreData PetStoreData as JSON
   * @return modified PetStoreData object as JSON
   */
  @PutMapping("/{petStoreId}")
  public PetStoreData updatePetStore(@PathVariable Long petStoreId,  @RequestBody PetStoreData petStoreData) {
    petStoreData.setPetStoreId(petStoreId);
    log.info("Modifying pet store {}", petStoreData);
    
    return petStoreService.savePetStore(petStoreData);
  }
  
  
  @PostMapping("/{petStoreId}/employee")
  @ResponseStatus(code = HttpStatus.CREATED)
  public PetStoreEmployee saveEmployee(@PathVariable Long petStoreId, @RequestBody PetStoreEmployee petStoreEmployee) {
    log.info("Creating employee {} for pet store with ID={}", petStoreEmployee, petStoreId);
    return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
  }
  
  
  @PostMapping("/{petStoreId}/customer")
  @ResponseStatus(code = HttpStatus.CREATED)
  public PetStoreCustomer saveCustomer(@PathVariable Long petStoreId, @RequestBody PetStoreCustomer petStoreCustomer) {
    log.info("Creating customer {} for pet store with ID={}", petStoreCustomer, petStoreId);
    return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
  }
  
  
  @GetMapping
  public List<PetStoreData> retrieveAllPetStore(){
    return petStoreService.retrieveAllPetStore();
  }
  
  
  @GetMapping("/{petStoreId}")
  public PetStoreData retrievePetStoreById(@PathVariable Long petStoreId) {
    return petStoreService.retrievePetStoreById(petStoreId);
  }
  
  
  @DeleteMapping("/{petStoreId}")
  public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId) {
    log.info("Deleting pet store with ID{}", petStoreId);
    petStoreService.deletePetStoreById(petStoreId);
    
    return Map.of("massage",
        "Deletion of pet store with ID=" + petStoreId + " was successful.");    
  }
}
