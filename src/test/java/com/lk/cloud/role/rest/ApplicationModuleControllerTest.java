package com.lk.cloud.role.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vcgdev.common.exception.ServiceException;
import com.vcgdev.common.exception.ErrorCode;
import com.lk.cloud.role.dto.ApplicationModuleDTO;
import com.lk.cloud.role.service.ApplicationModuleService;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationModule REST Testing")
public class ApplicationModuleControllerTest {

    @Mock
    private ApplicationModuleService service;

    @InjectMocks
    private ApplicationModuleController controller;

    private ApplicationModuleDTO expectedDTO;

    // TODO change id class type
    private UUID expectedId = UUID.randomUUID();

    @BeforeEach
    public void setup() {
        expectedDTO = mockDTO();
    }

    private ApplicationModuleDTO mockDTO() {
        ApplicationModuleDTO dto = new ApplicationModuleDTO();
        dto.setId(expectedId);
        // TODO add properties
        return dto;
    }

    private void assertDTOProperties(ApplicationModuleDTO dto) {
        assertEquals(expectedId, dto.getId());
        //TODO add all dto properties
    }

    @Test
    public void addApplicationModule_successTransaction_responseCreated() throws Exception {
        ApplicationModuleDTO resultFromService = mockDTO();
        
        when(service.addApplicationModule(any(ApplicationModuleDTO.class)))
           .thenReturn(resultFromService);
        ResponseEntity<ApplicationModuleDTO> responseResult = controller.addApplicationModule(expectedDTO);

        assertNotNull(responseResult);
        assertEquals(HttpStatus.CREATED, responseResult.getStatusCode());
        assertNotNull(responseResult.getBody());

        ApplicationModuleDTO resultDTO = responseResult.getBody();
        assertDTOProperties(resultDTO);
    }

    @Test
    public void addApplicationModule_CatchServiceException_ExceptionIsPropagated() throws Exception {
        
        when(service.addApplicationModule(any(ApplicationModuleDTO.class)))
           .thenThrow(new ServiceException(ErrorCode.INTERNAL));
        
        assertThrows(ServiceException.class, () -> controller.addApplicationModule(expectedDTO));
    }

    @Test
    public void addApplicationModule_CatchException_ExceptionIsPropagated() throws Exception {
        
        when(service.addApplicationModule(any(ApplicationModuleDTO.class)))
           .thenThrow(new RuntimeException());
        
        assertThrows(ServiceException.class, () -> controller.addApplicationModule(expectedDTO));
    }

    @Test
    public void updateApplicationModule_sucessTransaction_responseOK() throws Exception {
        ApplicationModuleDTO resultFromService = mockDTO();

        when(service.updateApplicationModule(any(ApplicationModuleDTO.class), any(UUID.class)))
           .thenReturn(resultFromService);
        ResponseEntity<ApplicationModuleDTO> responseResult = controller.updateApplicationModule(expectedDTO, expectedId);

        assertNotNull(responseResult);
        assertEquals(HttpStatus.OK, responseResult.getStatusCode());
        assertNotNull(responseResult.getBody());

        ApplicationModuleDTO resultDTO = responseResult.getBody();
        assertDTOProperties(resultDTO);
    }

    @Test
    public void updateApplicationModule_CatchServiceException_ExceptionIsPropagated() throws Exception {        
        when(service.updateApplicationModule(any(ApplicationModuleDTO.class), any(UUID.class)))
           .thenThrow(new ServiceException(ErrorCode.INTERNAL));
        
        assertThrows(ServiceException.class, () -> controller.updateApplicationModule(expectedDTO, expectedId));
    }

    @Test
    public void updateApplicationModule_CatchException_ExceptionIsPropagated() throws Exception {        
        when(service.updateApplicationModule(any(ApplicationModuleDTO.class), any(UUID.class)))
           .thenThrow(new RuntimeException());
        
        assertThrows(ServiceException.class, () -> controller.updateApplicationModule(expectedDTO, expectedId));
    }


    @Test
    public void findById_serviceReturnsElement_responseOK() throws Exception {
        ApplicationModuleDTO resultFromService = mockDTO();
        when(service.findById(eq(expectedId)))
            .thenReturn(resultFromService);

        ResponseEntity<ApplicationModuleDTO> responseResult = controller.findById(expectedId);

        assertNotNull(responseResult);
        assertEquals(HttpStatus.OK, responseResult.getStatusCode());
        assertNotNull(responseResult.getBody());

        ApplicationModuleDTO resultDTO = responseResult.getBody();
        assertDTOProperties(resultDTO);      
    }

    @Test
    public void findAll_serviceReturnsCollection_responseOK() throws Exception {
        ApplicationModuleDTO resultFromService = mockDTO();
        when(service.findAll())
            .thenReturn(Collections.singletonList(resultFromService));

        ResponseEntity<Collection<ApplicationModuleDTO>>   responseResult = controller.findAll();

        assertNotNull(responseResult);
        assertEquals(HttpStatus.OK, responseResult.getStatusCode());
        assertNotNull(responseResult.getBody());

        Collection<ApplicationModuleDTO> collectionResult = responseResult.getBody();
        assertFalse(collectionResult.isEmpty());
    }

    @Test
    public void getPaginatedResult_serviceReturnsPage_responseOK() throws Exception {
        when(service.getPaginatedResult(eq(0), eq(10), eq("id"), eq("asc")))
            .thenReturn(new PageImpl<>(Collections.singletonList(expectedDTO), PageRequest.of(0, 10), 10));
        
        ResponseEntity<Page<ApplicationModuleDTO>> responseResult = controller.getPaginatedResult(0, 10, "id", "asc");
        
        assertNotNull(responseResult);
        assertEquals(HttpStatus.OK, responseResult.getStatusCode());
        assertNotNull(responseResult.getBody());

        Page<ApplicationModuleDTO> paginatedResult = responseResult.getBody();
        assertEquals(1, paginatedResult.getContent().size());
        assertDTOProperties(paginatedResult.getContent().get(0));
    }


}
