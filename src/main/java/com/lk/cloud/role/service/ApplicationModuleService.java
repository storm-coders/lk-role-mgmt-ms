package com.lk.cloud.role.service;

import com.vcgdev.common.exception.ServiceException;
import com.lk.cloud.role.dto.ApplicationModuleDTO;
import java.util.Collection;
import java.util.UUID;

import org.springframework.data.domain.Page;
/**
* Versions 1.0
* Created using Rest API Generator
* Basic CRUD service
*/
public interface ApplicationModuleService {

    /**
    * Method to add new ApplicationModule in database
    * @param applicationModuleDTO the values to be saved
    * @throws ServiceException if an error happens in transaction
    */
    ApplicationModuleDTO addApplicationModule(ApplicationModuleDTO applicationModuleDTO) throws ServiceException;

    /**
    * Method to update values of entity
    * @param applicationModuleDTO the values to update
    * @param moduleId
    * @throws ServiceException if an error happens in transaction
    */
    ApplicationModuleDTO updateApplicationModule(ApplicationModuleDTO applicationModuleDTO, UUID moduleId) throws ServiceException ;

    /**
    * Get details of ApplicationModule
    * @param id unique identifier to find object
    * throws ServiceException if an error happens in select transaction
    */
    ApplicationModuleDTO findById(UUID id) throws ServiceException;

    /**
    * Get List of all ApplicationModule
    * throws ServiceException if an error happens in select transaction
    */
    Collection<ApplicationModuleDTO> findAll(Boolean expand) throws ServiceException;

    /**
    * Get paginated result
    * @param page -> the page to be extracted from database
    * @param size -> the size of dataset
    * @param columnToOrder -> The column to sort result
    * @param orderType -> values {'DESC','ASC'}
    * @return the page obtained from database
    * @throws ServiceException if an error happens in query select
    */
    Page<ApplicationModuleDTO> getPaginatedResult(
        Integer page,Integer size,String columnToOrder, String orderType
    ) throws ServiceException ;

}
