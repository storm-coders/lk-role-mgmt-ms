package com.lk.cloud.role.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.vcgdev.common.exception.ServiceException;
import com.vcgdev.common.exception.ErrorCode;
import com.lk.cloud.role.service.impl.UserGroupServiceImpl;
import com.lk.cloud.role.dto.UserGroupDTO;
import com.lk.cloud.role.mappers.UserGroupMapper;
import com.lk.cloud.role.persistence.ModulePrivilegeRepository;
import com.lk.cloud.role.persistence.UserGroupRepository;
import com.google.common.util.concurrent.Service;
import com.lk.cloud.role.domain.ModulePrivilege;
import com.lk.cloud.role.domain.UserGroup;

import org.mapstruct.factory.Mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@DisplayName("UserGroup Service Testing")
@ExtendWith(MockitoExtension.class)
public class UserGroupServiceTest {

    @Mock
    private UserGroupRepository repository;

    @Mock
    private ModulePrivilegeRepository privilegeRepository;

    private UserGroupMapper mapper = Mappers.getMapper(UserGroupMapper.class);

    private UserGroupServiceImpl service;

    private UserGroup entity;

    private UserGroupDTO userGroupDTO;

    private UUID id = UUID.randomUUID();

    @BeforeEach
    public void setup() {
        
        userGroupDTO = new UserGroupDTO();
        userGroupDTO.setName("Employee");
        userGroupDTO.setPrivileges(List.of(
            UUID.randomUUID(),
            UUID.randomUUID()
        ));

        entity = mapper.fromDTO(userGroupDTO);

        entity.setPrivileges(List.of(
            ModulePrivilege.builder().id(UUID.randomUUID()).build()
        ));

        service = new UserGroupServiceImpl(repository, privilegeRepository, mapper);
    }

    private void assertDTOProperties(UserGroupDTO dto) {
        assertNotNull(dto.getId());
        assertNotNull(dto.getName());
        assertNotNull(dto.getPrivileges());
        dto.getPrivileges()
            .forEach(p -> assertNotNull(p));
    }

    @Test
    void addUserGroup_successTransaction() throws Exception {
        when(repository.save(any(UserGroup.class)))
            .thenAnswer(new Answer<UserGroup>(){
                @Override
                public UserGroup answer(InvocationOnMock invocation) throws Throwable {
                    UserGroup group = (UserGroup)invocation.getArguments()[0];
                    group.setId(id);
                    return group;
                }
            });
        
        when(privilegeRepository.existsById(any(UUID.class)))
            .thenReturn(true); 
                
        UserGroupDTO dto = service.addUserGroup(userGroupDTO);
        assertNotNull(dto);
        assertDTOProperties(dto);
        
        verify(privilegeRepository, times(userGroupDTO.getPrivileges().size())).existsById(any(UUID.class));
        verify(repository, never()).existsById(any(UUID.class));
    }

    @Test
    void addUserGroup_WithParentId_successTransaction() throws Exception {
        UUID parentId = UUID.randomUUID();
        userGroupDTO.setParentId(parentId);
        when(repository.existsById(parentId)).thenReturn(true);
        when(repository.save(any(UserGroup.class)))
            .thenAnswer(new Answer<UserGroup>(){
                @Override
                public UserGroup answer(InvocationOnMock invocation) throws Throwable {
                    UserGroup group = (UserGroup)invocation.getArguments()[0];
                    group.setId(id);
                    return group;
                }
            });
        
        when(privilegeRepository.existsById(any(UUID.class)))
            .thenReturn(true); 
                
        UserGroupDTO dto = service.addUserGroup(userGroupDTO);
        assertNotNull(dto);
        assertDTOProperties(dto);
        
        verify(privilegeRepository, times(userGroupDTO.getPrivileges().size())).existsById(any(UUID.class));
        verify(repository).existsById(parentId);
    }


    @Test
    void addUserGroup_dataIntegrityExceptionIsThrown_customExceptionShouldBeThrown () throws Exception {
        when(repository.save(any(UserGroup.class)))
            .thenThrow(new DataIntegrityViolationException("Integrity violation"));
        
        when(privilegeRepository.existsById(any(UUID.class)))
            .thenReturn(true);

        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.addUserGroup(userGroupDTO);
        });
        assertEquals(ErrorCode.INTERNAL, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());

        verify(repository).save(any(UserGroup.class));
    }

    @Test
    void addUserGroup_PrivilegeIsNotFound_customExceptionShouldBeThrown () throws Exception {
       
        when(privilegeRepository.existsById(any(UUID.class)))
            .thenReturn(false);
        
        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.addUserGroup(userGroupDTO);
        });
        assertEquals(ErrorCode.NOT_FOUND, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());

        verify(privilegeRepository).existsById(any(UUID.class));
        verify(privilegeRepository, never()).getOne(any(UUID.class));
        verifyNoInteractions(repository);
    }

    @Test
    void addUserGroup_ParentIsNotFound_customExceptionShouldBeThrown () throws Exception {
       UUID parentId = UUID.randomUUID();
        when(privilegeRepository.existsById(any(UUID.class)))
            .thenReturn(true);
        userGroupDTO.setParentId(parentId);
        
        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.addUserGroup(userGroupDTO);
        });
        assertEquals(ErrorCode.NOT_FOUND, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());

        verify(privilegeRepository, times(userGroupDTO.getPrivileges().size())).existsById(any(UUID.class));
        verify(repository).existsById(parentId);
        verify(repository, never()).save(any(UserGroup.class));
    }

    @Test
    void addUserGroup_notManagedExceptionIsThrown_customExceptionShouldBeThrown () throws Exception {
        when(repository.save(any(UserGroup.class)))
            .thenThrow(new RuntimeException("Not managed exception"));

        when(privilegeRepository.existsById(any(UUID.class)))
            .thenReturn(true);
        
        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.addUserGroup(userGroupDTO);
        });
        assertEquals(ErrorCode.INTERNAL, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());

        verify(repository).save(any(UserGroup.class));
    }

    @Test
    void updateUserGroup_notExistingEntity_customExceptionShouldBeThrown() throws Exception {
        when(repository.existsById(id))
           .thenReturn(Boolean.FALSE);

        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.updateUserGroup(userGroupDTO, id);
        });
        assertEquals(ErrorCode.NOT_FOUND, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());

        verify(repository).existsById(id);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void updateUserGroup_entityExistsInDB_successTransaction() throws Exception {
        when(repository.existsById(id))
           .thenReturn(Boolean.TRUE);
        
        when(privilegeRepository.existsById(any(UUID.class)))
           .thenReturn(true); 

        UserGroupDTO dto = service.updateUserGroup(userGroupDTO, id);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertDTOProperties(dto);

        verify(privilegeRepository, times(userGroupDTO.getPrivileges().size())).existsById(any(UUID.class));
        verify(repository).existsById(id);
        verify(repository).deletePrivileges(id);
        verify(repository).save(any(UserGroup.class));
    }

    @Test
    void updateUserGroup_errorFetchingFromDB_customExceptionShouldBeThrown() throws Exception {
        when(repository.existsById(id))
           .thenThrow(new RuntimeException("Not managed exception"));

        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.updateUserGroup(userGroupDTO, id);
        });

        assertEquals(ErrorCode.INTERNAL, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());

        verify(repository).existsById(id);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_entityNotExists_customExceptionShouldBeThrown() throws Exception {
        when(repository.existsById(id))
           .thenReturn(Boolean.FALSE);

        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.findById(id);
        });
        assertEquals(ErrorCode.NOT_FOUND, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());

        verify(repository).existsById(id);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_errorFromDB_customExceptionShouldBeThrown() throws Exception {
        when(repository.existsById(id))
           .thenThrow(new RuntimeException("Not managed error"));

        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.findById(id);
        });
        assertEquals(ErrorCode.INTERNAL, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());

        verify(repository).existsById(id);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_entityIsInDB_DTOIsReturned() throws Exception {
        when(repository.existsById(id))
            .thenReturn(Boolean.TRUE);
        entity.setId(id);
        when(repository.getOne(id))
            .thenReturn(entity);

        UserGroupDTO dto = service.findById(id);

        assertNotNull(dto);
        assertDTOProperties(dto);

        verify(repository).existsById(id);

        verify(repository).getOne(id);
    }

    @Test
    void findAll_exceptionIsThrown_customExceptionShouldBeThrown() throws Exception {

        when(repository.findAll())
            .thenThrow(new RuntimeException("Invalid transaction"));

        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.findAll();
        });

        assertEquals(ErrorCode.INTERNAL, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());
    }

    @Test
    void findAll_successTransaction_dtoListIsReturned() throws Exception {
        when(repository.findAll())
            .thenReturn(Collections.singletonList(entity));

        Collection<UserGroupDTO> dtoList = service.findAll();
        assertEquals(1, dtoList.size());
    }

    @Test
    void getPaginatedResult_invalidParametersProvided_customExceptionIsThrown () throws Exception {        
        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.getPaginatedResult(0, 10, "invalidColumn", "invalidOrder");
        });
        

        assertEquals(ErrorCode.INTERNAL, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());
        verifyNoInteractions(repository);
    }

    @ParameterizedTest(name = "#{index} Run test with Page= {0} - Size = {1} - Column = {2} - Orded = {3}")
    @MethodSource("validPageParameters")
    void getPaginatedResult_parametersProvided_pageIsReturned(Integer page, Integer size, String column, String order) throws Exception {
        when(repository.findAll(any(PageRequest.class)))
            .thenReturn(new PageImpl<>(Collections.singletonList(entity), PageRequest.of(page, size), size));
        Page<UserGroupDTO> dtoPage = service.getPaginatedResult(page, size, column, order);

        assertNotNull(dtoPage);
        assertEquals(1, dtoPage.getContent().size());
    }

    static Stream<Arguments> validPageParameters() {
        return Stream.of(
            Arguments.of(0, 10, null,  null),
            Arguments.of(1, 10, "id", "asc"),
            Arguments.of(1, 10, "id", "desc"),
            Arguments.of(1, 10, "is", null)
        );
    }

    @Test
    void deleteById_GroupNotFound_ExceptionShouldBeThrown() {

        ServiceException se = assertThrows(ServiceException.class, () -> service.deleteById(id));

        assertEquals(ErrorCode.NOT_FOUND, se.getCode());
        verify(repository, never()).deleteById(id);
        verify(repository, never()).deletePrivileges(id);
    }

    @Test
    void deleteByID_ExceptionCatch_CustomExceptionIsThrown() {

        doThrow(new RuntimeException())
            .when(repository).existsById(id);
        ServiceException se = assertThrows(ServiceException.class, () -> service.deleteById(id));

        assertEquals(ErrorCode.INTERNAL, se.getCode());
        verify(repository, never()).deleteById(id);
        verify(repository, never()).deletePrivileges(id);
    }

    @Test
    void deleteById_SuccessTransaction() throws ServiceException {

        when(repository.existsById(id))
            .thenReturn(true);

        service.deleteById(id);

        verify(repository).deleteById(id);
        verify(repository).deletePrivileges(id);
    }

}
