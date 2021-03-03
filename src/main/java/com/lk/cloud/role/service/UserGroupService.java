package com.lk.cloud.role.service;

import com.vcgdev.common.exception.ServiceException;
import com.lk.cloud.role.dto.UserGroupDTO;
import java.util.Collection;
import java.util.UUID;

import org.springframework.data.domain.Page;
/**
* Versions 1.0
* Created using Rest API Generator
* Basic CRUD service
*/
public interface UserGroupService {

    /**
    * Method to add new UserGroup in database
    * @param userGroupDTO the values to be saved
    * @throws ServiceException if an error happens in transaction
    */
    UserGroupDTO addUserGroup(UserGroupDTO userGroupDTO) throws ServiceException;

    /**
    * Method to update values of entity
    * @param userGroupDTO the values to update
    * @throws ServiceException if an error happens in transaction
    */
    UserGroupDTO updateUserGroup(UserGroupDTO userGroupDTO, UUID groupId) throws ServiceException ;

    /**
    * Get details of UserGroup
    * @param id unique identifier to find object
    * throws ServiceException if an error happens in select transaction
    */
    UserGroupDTO findById(UUID id) throws ServiceException;

    /**
    * Get List of all UserGroup
    * throws ServiceException if an error happens in select transaction
    */
    Collection<UserGroupDTO> findAll() throws ServiceException;

    /**
    * Get paginated result
    * @param page -> the page to be extracted from database
    * @param size -> the size of dataset
    * @param columnToOrder -> The column to sort result
    * @param orderType -> values {'DESC','ASC'}
    * @return the page obtained from database
    * @throws ServiceException if an error happens in query select
    */
    Page<UserGroupDTO> getPaginatedResult(
        Integer page,Integer size,String columnToOrder, String orderType
    ) throws ServiceException ;

    void deleteById(UUID groupId) throws ServiceException;
}
