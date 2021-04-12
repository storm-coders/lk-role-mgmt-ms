package com.lk.cloud.role.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
import com.lk.cloud.role.service.impl.ApplicationModuleServiceImpl;
import com.lk.cloud.role.dto.ApplicationModuleDTO;
import com.lk.cloud.role.dto.PrivilegeDTO;
import com.lk.cloud.role.mappers.ApplicationModuleMapper;
import com.lk.cloud.role.persistence.ApplicationModuleRepository;
import com.lk.cloud.role.persistence.ModulePrivilegeRepository;
import com.lk.cloud.role.domain.ApplicationModule;
import com.lk.cloud.role.domain.ModulePrivilege;

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
import org.mockito.ArgumentCaptor;
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

@DisplayName("ApplicationModule Service Testing")
@ExtendWith(MockitoExtension.class)
public class ApplicationModuleServiceTest {

    @Mock
    private ApplicationModuleRepository repository;

    @Mock
    private ModulePrivilegeRepository privilegeRepository;

    private ApplicationModuleMapper mapper = Mappers.getMapper(ApplicationModuleMapper.class);

    private ApplicationModuleServiceImpl service;

    private ApplicationModule entity;

    private ApplicationModuleDTO applicationModuleDTO;

    private UUID id = UUID.randomUUID();

    @BeforeEach
    public void setup() {
        applicationModuleDTO = new ApplicationModuleDTO();
        applicationModuleDTO.setDescription("Description");
        applicationModuleDTO.setIsActive(Boolean.TRUE);
        applicationModuleDTO.setName("AppName");
        applicationModuleDTO.setPrivileges(List.of(
            PrivilegeDTO
            .builder()
            .code("code1")
            .description("desc1")
            .build(),
            PrivilegeDTO
            .builder()
            .code("code2")
            .description("desc2")
            .build()
        ));

        entity = mapper.fromDTO(applicationModuleDTO);
        entity.setId(UUID.randomUUID());
        entity.getPrivileges()
            .forEach(p -> p.setId(UUID.randomUUID()));
        service = new ApplicationModuleServiceImpl(repository, privilegeRepository, mapper);
    }

    private void assertDTOProperties(ApplicationModuleDTO dto) {
        assertNotNull(dto.getId());
        assertNotNull(dto.getDescription());
        assertNotNull(dto.getIsActive());
        assertNotNull(dto.getName());
        assertNotNull(dto.getPrivileges());
        dto.getPrivileges()
        .forEach(p-> {
            assertNotNull(p.getCode());
            assertNotNull(p.getDescription());
            assertNotNull(p.getId());
        });
    }

    @Test
    public void addApplicationModule_successTransaction() throws Exception {
        when(repository.save(any(ApplicationModule.class)))
            .thenAnswer(new Answer<ApplicationModule>(){
               @Override
               public ApplicationModule answer(InvocationOnMock invocation) throws Throwable {
                   ApplicationModule module = (ApplicationModule)invocation.getArguments()[0];
                   module.setId(UUID.randomUUID());
                   assertNotNull(module.getPrivileges());
                   module.getPrivileges()
                    .forEach(p -> p.setId(UUID.randomUUID()));
                   return null;
               } 
            });
        ApplicationModuleDTO dto = service.addApplicationModule(applicationModuleDTO);
        assertNotNull(dto);
        assertDTOProperties(dto);

        ArgumentCaptor<ApplicationModule> captor = ArgumentCaptor.forClass(ApplicationModule.class);
        verify(repository).save(captor.capture());

        assertNotNull(captor.getValue());

        ApplicationModule module = captor.getValue();

        assertNotNull(module.getPrivileges());
        module.getPrivileges()
            .forEach(p -> assertNotNull(p.getModule()));
    }


    @Test
    public void addApplicationModule_dataIntegrityExceptionIsThrown_customExceptionShouldBeThrown () throws Exception {
        when(repository.save(any(ApplicationModule.class)))
            .thenThrow(new DataIntegrityViolationException("Integrity violation"));
        
        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.addApplicationModule(applicationModuleDTO);
        });
        assertEquals(ErrorCode.INTERNAL, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());

        verify(repository).save(any(ApplicationModule.class));
    }

    @Test
    public void addApplicationModule_notManagedExceptionIsThrown_customExceptionShouldBeThrown () throws Exception {
        when(repository.save(any(ApplicationModule.class)))
            .thenThrow(new RuntimeException("Not managed exception"));
        
        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.addApplicationModule(applicationModuleDTO);
        });
        assertEquals(ErrorCode.INTERNAL, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());

        verify(repository).save(any(ApplicationModule.class));
    }

    @Test
    public void updateApplicationModule_notExistingEntity_customExceptionShouldBeThrown() throws Exception {
        when(repository.existsById(id))
           .thenReturn(Boolean.FALSE);

        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.updateApplicationModule(applicationModuleDTO, id);
        });
        assertEquals(ErrorCode.NOT_FOUND, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());

        verify(repository).existsById(id);

        verifyNoMoreInteractions(repository);
    }

    @Test
    public void updateApplicationModule_entityExistsInDB_successTransaction() throws Exception {
        when(repository.existsById(id))
           .thenReturn(Boolean.TRUE);
        when(repository.save(any(ApplicationModule.class)))
            .thenAnswer(new Answer<ApplicationModule>(){
                @Override
                public ApplicationModule answer(InvocationOnMock invocation) throws Throwable {
                    ApplicationModule module = (ApplicationModule)invocation.getArguments()[0];
                    assertNotNull(module.getPrivileges());
                    module.getPrivileges()
                        .forEach(p -> {
                            if(p.getId() == null ) {
                                p.setId(UUID.randomUUID());
                            }
                        });
                    return module;
                }
            });
        
        when(privilegeRepository.findByModuleId(id))
            .thenReturn(
                List.of(
                    ModulePrivilege.builder()
                    .id(UUID.randomUUID())
                    .code("code3")
                    .description("desc3")
                    .build(),
                    ModulePrivilege.builder()
                    .id(UUID.randomUUID())
                    .code("code4")
                    .description("desc4")
                    .build(),
                    ModulePrivilege.builder()
                    .id(UUID.randomUUID())
                    .code("code1")
                    .description("desc1")
                    .build()
                )
            );
        ApplicationModuleDTO dto = service.updateApplicationModule(applicationModuleDTO, id);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertDTOProperties(dto);

        verify(repository).existsById(id);

        ArgumentCaptor<ApplicationModule> captor = ArgumentCaptor.forClass(ApplicationModule.class);
        verify(repository).save(captor.capture());

        verify(privilegeRepository).findByModuleId(id);
        verify(privilegeRepository, times(2)).delete(any(ModulePrivilege.class));
        assertNotNull(captor.getValue());

        ApplicationModule module = captor.getValue();

        assertNotNull(module.getPrivileges());
        module.getPrivileges()
            .forEach(p -> assertNotNull(p.getModule()));
    }

    @Test
    public void updateApplicationModule_errorFetchingFromDB_customExceptionShouldBeThrown() throws Exception {
        when(repository.existsById(id))
           .thenThrow(new RuntimeException("Not managed exception"));

        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.updateApplicationModule(applicationModuleDTO, id);
        });

        assertEquals(ErrorCode.INTERNAL, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());

        verify(repository).existsById(id);

        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findById_entityNotExists_customExceptionShouldBeThrown() throws Exception {
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
    public void findById_errorFromDB_customExceptionShouldBeThrown() throws Exception {
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
    public void findById_entityIsInDB_DTOIsReturned() throws Exception {
        when(repository.existsById(id))
            .thenReturn(Boolean.TRUE);

        when(repository.getOne(id))
            .thenReturn(entity);

        ApplicationModuleDTO dto = service.findById(id);

        assertNotNull(dto);
        assertDTOProperties(dto);

        verify(repository).existsById(id);

        verify(repository).getOne(id);
    }

    @Test
    public void findAll_exceptionIsThrown_customExceptionShouldBeThrown() throws Exception {

        when(repository.findAll())
            .thenThrow(new RuntimeException("Invalid transaction"));

        ServiceException cte = assertThrows(ServiceException.class, () -> {
            service.findAll(Boolean.FALSE);
        });

        assertEquals(ErrorCode.INTERNAL, cte.getCode());
        assertTrue(cte.getErrors().isEmpty());
    }

    @Test
    public void findAll_successTransaction_dtoListIsReturned() throws Exception {
        when(repository.findAll())
            .thenReturn(Collections.singletonList(entity));

        Collection<ApplicationModuleDTO> dtoList = service.findAll(Boolean.TRUE);
        assertEquals(1, dtoList.size());
    }

    @Test
    public void findAll_ExpandIsFalse_dtoListIsReturned() throws Exception {
        when(repository.findAll())
            .thenReturn(Collections.singletonList(entity));

        Collection<ApplicationModuleDTO> dtoList = service.findAll(Boolean.FALSE);
        assertEquals(1, dtoList.size());
    }

    @Test
    public void getPaginatedResult_invalidParametersProvided_customExceptionIsThrown () throws Exception {        
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
        Page<ApplicationModuleDTO> dtoPage = service.getPaginatedResult(page, size, column, order);

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

}
