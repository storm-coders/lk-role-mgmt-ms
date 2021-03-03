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
import java.util.UUID;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lk.cloud.role.service.UserGroupService;
import com.vcgdev.common.exception.ErrorCode; 
import com.vcgdev.common.exception.ServiceException;
import com.lk.cloud.role.dto.PrivilegeDTO;
import com.lk.cloud.role.dto.UserGroupDTO;
import com.lk.cloud.role.dto.UserGroupTreeDTO;
import com.lk.cloud.role.mappers.UserGroupMapper;
import com.lk.cloud.role.persistence.ModulePrivilegeRepository;
import com.lk.cloud.role.persistence.UserGroupRepository;
import com.lk.cloud.role.domain.ModulePrivilege;
import com.lk.cloud.role.domain.UserGroup;

/**
* Created using Rest API Generator
* Basic CRUD service
*/
@Service
@Transactional(readOnly = true)
@Slf4j
public class UserGroupServiceImpl implements UserGroupService {


    private final UserGroupRepository userGroupRepository;
    private final UserGroupMapper userGroupMapper;
    private final ModulePrivilegeRepository privilegeRepository;

    public UserGroupServiceImpl(UserGroupRepository userGroupRepository, //
        ModulePrivilegeRepository privilegeRepository,
        UserGroupMapper userGroupMapper) {
        this.userGroupRepository = userGroupRepository;
        this.userGroupMapper = userGroupMapper;
        this.privilegeRepository = privilegeRepository;
    }

    private static final Map<String,String> columnNames = new HashMap<>();

    static {
        columnNames.put("id","id");
        columnNames.put("name","name");
        columnNames.put("parentId","parentId");
    }

    /**
    * Method to add new UserGroup in database
    * @param userGroupDTO the values to be saved
    * @throws ServiceException if an error happens in transaction
    */
    @Transactional(rollbackFor=ServiceException.class)
    public UserGroupDTO addUserGroup(UserGroupDTO userGroupDTO) throws ServiceException {
        try{
            UserGroup userGroup = userGroupMapper.fromDTO(userGroupDTO);   
            userGroup.setPrivileges(getPrivileges(userGroupDTO.getPrivileges()));
            validateParent(userGroup);            
            userGroupRepository.save(userGroup);
            return userGroupMapper.fromEntity(userGroup);
        } catch (ServiceException se) {
            throw se;
        } catch(Exception ex){
            log.error("Not managed error in insert transaction",ex);
            throw new ServiceException(ErrorCode.INTERNAL);
        }
    }

    private void validateParent(UserGroup userGroup) throws ServiceException {
        if (userGroup.getParentId() != null && !userGroupRepository.existsById(userGroup.getParentId())) {
            log.error("Can not link parent: {}", userGroup.getParentId());
            throw new ServiceException(ErrorCode.NOT_FOUND);
        }
    }

    private List<ModulePrivilege> getPrivileges(List<UUID> pUuids) throws ServiceException {
        List<ModulePrivilege> privileges = new ArrayList<>();
        for (UUID p : pUuids) {
                if (!privilegeRepository.existsById(p)){
                    log.error("Can not link privilege: {}", p);
                    throw new ServiceException(ErrorCode.NOT_FOUND);
                } else {
                    privileges.add(ModulePrivilege.builder().id(p).build());
                }
        }
        return privileges;
    }

    /**
    * Method to update values of entity
    * @param userGroupDTO the values to update
    * @throws ServiceException if an error happens in transaction
    */
    @Transactional(rollbackFor=ServiceException.class)
    public UserGroupDTO updateUserGroup(UserGroupDTO userGroupDTO, UUID groupId) throws ServiceException {
        try{
            if(!userGroupRepository.existsById(groupId)){
                log.error("Entity not exists in database - ID: {}",groupId);
                throw new ServiceException(ErrorCode.NOT_FOUND);
            }
            userGroupRepository.deletePrivileges(groupId);
            UserGroup userGroup = userGroupMapper.fromDTO(userGroupDTO);
            userGroup.setId(groupId);
            userGroup.setPrivileges(getPrivileges(userGroupDTO.getPrivileges()));
            validateParent(userGroup);     
            userGroupRepository.save(userGroup);
            return userGroupMapper.fromEntity(userGroup);
        }catch(ServiceException xe){
            throw xe;
        }catch(Exception ex){
            log.error("Not managed error in insert transaction", ex);
            throw new ServiceException(ErrorCode.INTERNAL);
        }
    }

    /**
    * Get details of UserGroup
    * @param id unique identifier to find object
    * throws ServiceException if an error happens in select transaction
    */
    public UserGroupDTO findById(UUID id) throws ServiceException {
        try{
            if(!userGroupRepository.existsById(id)){
                log.error("Entity not exists in database - ID: {}",id);
                throw new ServiceException(ErrorCode.NOT_FOUND);
            }
            return  userGroupMapper.fromEntity(userGroupRepository.getOne(id));
        }catch(ServiceException excp){
            throw excp;
        }catch(Exception ex){
            log.error("Not managed error in select by id transaction", ex);
            throw new ServiceException(ErrorCode.INTERNAL);
        }
    }

    /**
    * Get List of all UserGroup
    * throws ServiceException if an error happens in select transaction
    */
    public Collection<UserGroupDTO> findAll() throws ServiceException {
        try{
            return userGroupRepository.findAll()
            .stream()
            .map(userGroupMapper::fromEntitySummary)
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
    public Page<UserGroupDTO> getPaginatedResult(
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
                Page<UserGroup> userGroupPage = userGroupRepository.findAll(request);
                List<UserGroupDTO> resourcesList = userGroupPage.getContent()
                                    .stream()
                                    .map(userGroupMapper::fromEntitySummary)
                                    .collect(Collectors.toList());
                return new PageImpl<>(resourcesList, request, userGroupPage.getTotalElements());
            }catch (Exception ex){
                log.error("Not managed error in select transaction",ex);
                throw new ServiceException(ErrorCode.INTERNAL);
            }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = ServiceException.class)
    public void deleteById(UUID groupId) throws ServiceException {
        try {
            if (!userGroupRepository.existsById(groupId)) {
                log.error("No entity was found in DB {}", groupId);
                throw new ServiceException(ErrorCode.NOT_FOUND);
            }
            userGroupRepository.deletePrivileges(groupId);
            userGroupRepository.deleteById(groupId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("Can not delete by id: {}", groupId, e);
            throw new ServiceException(ErrorCode.INTERNAL);
        }
    }

    @Override
    public UserGroupTreeDTO getTree(UUID groupId, boolean toTop) throws ServiceException {
        if (!userGroupRepository.existsById(groupId)) {
            log.error("Can not load tree from inexisting group {}", groupId);
            throw new ServiceException(ErrorCode.NOT_FOUND);
        }
        return toTop ? toTop(groupId) : toBottom(groupId);
    }

    private UserGroupTreeDTO toTop(UUID groupId) {
        UserGroup group = userGroupRepository.getOne(groupId);
        UserGroupTreeDTO root = fromUserGroup(group);
        if (group.getParentId() == null) {
            return root;
        } else {
            return parent(group.getParentId(), root);
        }
    }

    private UserGroupTreeDTO parent(UUID parentId, UserGroupTreeDTO child) {
        UserGroup group = userGroupRepository.getOne(parentId);
        UserGroupTreeDTO root = fromUserGroup(group);
        root.getNodes().add(child);
        if (group.getParentId() != null ) {
                return parent(group.getParentId(), root);
        } else {
            return root;
        }
    }

    private UserGroupTreeDTO toBottom(UUID groupId) {
        UserGroup group = userGroupRepository.getOne(groupId);
        List<UserGroup> children = userGroupRepository.findByParentId(groupId);
        UserGroupTreeDTO root = fromUserGroup(group);
        
        children.parallelStream()
            .forEach(child -> root.getNodes().add(fromUserGroup(child)));
        return root;
    }

    private UserGroupTreeDTO fromUserGroup(UserGroup group) {
        UserGroupTreeDTO dto = UserGroupTreeDTO
                            .builder()
                            .id(group.getId())
                            .name(group.getName())
                            .nodes(new ArrayList<>())
                            .build();
        List<PrivilegeDTO> privileges = group
                                            .getPrivileges()
                                            .parallelStream()
                                            .map(p -> PrivilegeDTO.builder()
                                                        .id(p.getId())
                                                        .code(p.getCode())
                                                        .description(p.getDescription())                                
                                                        .build())
                                            .collect(Collectors.toList());
        dto.setPrivileges(privileges);
        return dto;
    }
}
