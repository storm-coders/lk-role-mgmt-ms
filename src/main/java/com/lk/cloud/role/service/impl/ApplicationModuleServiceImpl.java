package com.lk.cloud.role.service.impl;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lk.cloud.role.service.ApplicationModuleService;
import com.vcgdev.common.exception.ErrorCode; 
import com.vcgdev.common.exception.ServiceException;
import com.lk.cloud.role.dto.ApplicationModuleDTO;
import com.lk.cloud.role.dto.PrivilegeDTO;
import com.lk.cloud.role.mappers.ApplicationModuleMapper;
import com.lk.cloud.role.persistence.ApplicationModuleRepository;
import com.lk.cloud.role.persistence.ModulePrivilegeRepository;
import com.lk.cloud.role.domain.ApplicationModule;
import com.lk.cloud.role.domain.ModulePrivilege;

/**
* Created using Rest API Generator
* Basic CRUD service
*/
@Service
@Transactional(readOnly = true)
@Slf4j
public class ApplicationModuleServiceImpl implements ApplicationModuleService {


    private final ApplicationModuleRepository applicationModuleRepository;
    private final ApplicationModuleMapper applicationModuleMapper;
    private final ModulePrivilegeRepository privilegeRepository;

    public ApplicationModuleServiceImpl(ApplicationModuleRepository applicationModuleRepository, //
        ModulePrivilegeRepository modulePrivilegeRepository, //
        ApplicationModuleMapper applicationModuleMapper) {
        this.applicationModuleRepository = applicationModuleRepository;
        this.applicationModuleMapper = applicationModuleMapper;
        this.privilegeRepository = modulePrivilegeRepository;
    }

    private static final Map<String,String> columnNames = new HashMap<>();

    static {
        columnNames.put("id","id");
        columnNames.put("name", "name");
        columnNames.put("description", "description");
        columnNames.put("isActive", "isActive");
    }

    /**
    * Method to add new ApplicationModule in database
    * @param applicationModuleDTO the values to be saved
    * @throws ServiceException if an error happens in transaction
    */
    @Transactional(rollbackFor=ServiceException.class)
    public ApplicationModuleDTO addApplicationModule(ApplicationModuleDTO applicationModuleDTO) throws ServiceException {
        try{
            ApplicationModule applicationModule = applicationModuleMapper.fromDTO(applicationModuleDTO);
            applicationModule.getPrivileges()
                .parallelStream()
                .forEach(p -> p.setModule(applicationModule));
            applicationModuleRepository.save(applicationModule);
            return applicationModuleMapper.fromEntity(applicationModule);
        } catch(Exception ex){
            log.error("Not managed error in insert transaction",ex);
            throw new ServiceException(ErrorCode.INTERNAL);
        }
    }

    /**
    * Method to update values of entity
    * @param applicationModuleDTO the values to update
    * @throws ServiceException if an error happens in transaction
    */
    @Transactional(rollbackFor=ServiceException.class)
    public ApplicationModuleDTO updateApplicationModule(ApplicationModuleDTO applicationModuleDTO, UUID moduleId) throws ServiceException {
        try{
            if(!applicationModuleRepository.existsById(moduleId)){
                log.error("Entity not exists in database - ID: {}", moduleId);
                throw new ServiceException(ErrorCode.NOT_FOUND);
            }
            ApplicationModule applicationModule = applicationModuleMapper.fromDTO(applicationModuleDTO);
            applicationModule.setId(moduleId);
            applicationModule.setPrivileges(new ArrayList<>());

            List<ModulePrivilege> currentPrivileges = privilegeRepository.findByModuleId(moduleId);
            for (ModulePrivilege privilege: currentPrivileges) {
                Optional<PrivilegeDTO> privilegeDTOOPT = applicationModuleDTO
                                    .getPrivileges()
                                    .parallelStream()
                                    .filter(p -> p.getCode().equals(privilege.getCode()))
                                    .findFirst();
                if (!privilegeDTOOPT.isPresent()) {
                    privilegeRepository.delete(privilege);
                } else {
                    privilege.setDescription(privilegeDTOOPT.get().getDescription());
                    privilege.setModule(applicationModule);
                    applicationModule.getPrivileges().add(privilege);
                }
            }

            applicationModuleDTO.getPrivileges()
                .parallelStream()
                .forEach(p -> {
                    boolean isPresent = currentPrivileges.stream()
                                            .anyMatch(pr -> pr.getCode().equals(p.getCode()));
                    if (!isPresent) {
                        ModulePrivilege privilege = new ModulePrivilege();
                        privilege.setCode(p.getCode());
                        privilege.setDescription(p.getDescription());
                        privilege.setModule(applicationModule);
                        applicationModule.getPrivileges().add(privilege);
                    }
                }); 
            applicationModuleRepository.save(applicationModule);
            return applicationModuleMapper.fromEntity(applicationModule);
        }catch(ServiceException xe){
            throw xe;
        }catch(Exception ex){
            log.error("Not managed error in insert transaction", ex);
            throw new ServiceException(ErrorCode.INTERNAL);
        }
    }

    /**
    * Get details of ApplicationModule
    * @param id unique identifier to find object
    * throws ServiceException if an error happens in select transaction
    */
    public ApplicationModuleDTO findById(UUID id) throws ServiceException {
        try{
            if(!applicationModuleRepository.existsById(id)){
                log.error("Entity not exists in database - ID: {}",id);
                throw new ServiceException(ErrorCode.NOT_FOUND);
            }
            return applicationModuleMapper.fromEntity(applicationModuleRepository.getOne(id));
        }catch(ServiceException excp){
            throw excp;
        }catch(Exception ex){
            log.error("Not managed error in select by id transaction", ex);
            throw new ServiceException(ErrorCode.INTERNAL);
        }
    }

    /**
    * Get List of all ApplicationModule
    * throws ServiceException if an error happens in select transaction
    */
    public Collection<ApplicationModuleDTO> findAll() throws ServiceException {
        try{
            return applicationModuleRepository.findAll()
            .stream()
            .map(applicationModuleMapper::fromEntitySummary)
            .collect(Collectors.toList());
        }catch(Exception ex){
            log.error("Not managed error in select transaction", ex);
            throw new ServiceException(ErrorCode.INTERNAL);
        }
    }

    /**
    * Get paginated result
    * @param page -> the page to be extracted from database
    * @param size -> the size of dataset
    * @param columnToOrder -> The column to sort result
    * @param orderType -> values {'DESC','ASC'}
    * @return the page obtained from database
    * @throws ServiceException if an error happens in query select
    */
    public Page<ApplicationModuleDTO> getPaginatedResult(
            Integer page,Integer size,String columnToOrder, String orderType
        ) throws ServiceException {
            try {
                Sort sort = Sort.by(Sort.Direction.ASC, columnNames.get("id"));
                if (columnToOrder != null && orderType != null) {
                    if (orderType.equalsIgnoreCase("asc")) {
                        sort = Sort.by(Sort.Direction.ASC, columnNames.get(columnToOrder));
                    }else {
                        sort = Sort.by(Sort.Direction.DESC, columnNames.get(columnToOrder));
                    }
                }
                PageRequest request = PageRequest.of(page, size, sort);
                Page<ApplicationModule> applicationModulePage = applicationModuleRepository.findAll(request);
                List<ApplicationModuleDTO> resourcesList = applicationModulePage.getContent()
                                    .stream()
                                    .map(applicationModuleMapper::fromEntitySummary)
                                    .collect(Collectors.toList());
                return new PageImpl<>(resourcesList, request, applicationModulePage.getTotalElements());
            }catch (Exception ex){
                log.error("Not managed error in select transaction",ex);
                throw new ServiceException(ErrorCode.INTERNAL);
            }
    }
}
