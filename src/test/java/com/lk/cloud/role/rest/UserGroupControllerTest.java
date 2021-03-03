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
import com.lk.cloud.role.dto.UserGroupDTO;
import com.lk.cloud.role.service.UserGroupService;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserGroup REST Testing")
public class UserGroupControllerTest {

    @Mock
    private UserGroupService service;

    @InjectMocks
    private UserGroupController controller;

    private UserGroupDTO expectedDTO;

    private UUID expectedId = UUID.randomUUID();

    @BeforeEach
    public void setup() {
        expectedDTO = mockDTO();
    }

    private UserGroupDTO mockDTO() {
        UserGroupDTO dto = new UserGroupDTO();
        dto.setId(expectedId);
        // TODO add properties
        return dto;
    }

    private void assertDTOProperties(UserGroupDTO dto) {
        assertEquals(expectedId, dto.getId());
        //TODO add all dto properties
    }

    @Test
    public void addUserGroup_successTransaction_responseCreated() throws Exception {
        UserGroupDTO resultFromService = mockDTO();
        
        when(service.addUserGroup(any(UserGroupDTO.class)))
           .thenReturn(resultFromService);
        ResponseEntity<UserGroupDTO> responseResult = controller.addUserGroup(expectedDTO);

        assertNotNull(responseResult);
        assertEquals(HttpStatus.CREATED, responseResult.getStatusCode());
        assertNotNull(responseResult.getBody());

        UserGroupDTO resultDTO = responseResult.getBody();
        assertDTOProperties(resultDTO);
    }

    @Test
    public void addUserGroup_CatchServiceException_ExceptionIsPropagated() throws Exception {
        
        when(service.addUserGroup(any(UserGroupDTO.class)))
           .thenThrow(new ServiceException(ErrorCode.INTERNAL));
        
        assertThrows(ServiceException.class, () -> controller.addUserGroup(expectedDTO));
    }

    @Test
    public void addUserGroup_CatchException_ExceptionIsPropagated() throws Exception {
        
        when(service.addUserGroup(any(UserGroupDTO.class)))
           .thenThrow(new RuntimeException());
        
        assertThrows(ServiceException.class, () -> controller.addUserGroup(expectedDTO));
    }

    @Test
    public void updateUserGroup_sucessTransaction_responseOK() throws Exception {
        UserGroupDTO resultFromService = mockDTO();

        when(service.updateUserGroup(any(UserGroupDTO.class), any(UUID.class)))
           .thenReturn(resultFromService);
        ResponseEntity<UserGroupDTO> responseResult = controller.updateUserGroup(expectedDTO, expectedId);

        assertNotNull(responseResult);
        assertEquals(HttpStatus.OK, responseResult.getStatusCode());
        assertNotNull(responseResult.getBody());

        UserGroupDTO resultDTO = responseResult.getBody();
        assertDTOProperties(resultDTO);
    }

    @Test
    public void updateUserGroup_CatchServiceException_ExceptionIsPropagated() throws Exception {        
        when(service.updateUserGroup(any(UserGroupDTO.class), any(UUID.class)))
           .thenThrow(new ServiceException(ErrorCode.INTERNAL));
        
        assertThrows(ServiceException.class, () -> controller.updateUserGroup(expectedDTO, expectedId));
    }

    @Test
    public void updateUserGroup_CatchException_ExceptionIsPropagated() throws Exception {        
        when(service.updateUserGroup(any(UserGroupDTO.class), any(UUID.class)))
           .thenThrow(new RuntimeException());
        
        assertThrows(ServiceException.class, () -> controller.updateUserGroup(expectedDTO, expectedId));
    }


    @Test
    public void findById_serviceReturnsElement_responseOK() throws Exception {
        UserGroupDTO resultFromService = mockDTO();
        when(service.findById(eq(expectedId)))
            .thenReturn(resultFromService);

        ResponseEntity<UserGroupDTO> responseResult = controller.findById(expectedId);

        assertNotNull(responseResult);
        assertEquals(HttpStatus.OK, responseResult.getStatusCode());
        assertNotNull(responseResult.getBody());

        UserGroupDTO resultDTO = responseResult.getBody();
        assertDTOProperties(resultDTO);      
    }

    @Test
    public void findAll_serviceReturnsCollection_responseOK() throws Exception {
        UserGroupDTO resultFromService = mockDTO();
        when(service.findAll())
            .thenReturn(Collections.singletonList(resultFromService));

        ResponseEntity<Collection<UserGroupDTO>>   responseResult = controller.findAll();

        assertNotNull(responseResult);
        assertEquals(HttpStatus.OK, responseResult.getStatusCode());
        assertNotNull(responseResult.getBody());

        Collection<UserGroupDTO> collectionResult = responseResult.getBody();
        assertFalse(collectionResult.isEmpty());
    }

    @Test
    public void getPaginatedResult_serviceReturnsPage_responseOK() throws Exception {
        when(service.getPaginatedResult(eq(0), eq(10), eq("id"), eq("asc")))
            .thenReturn(new PageImpl<>(Collections.singletonList(expectedDTO), PageRequest.of(0, 10), 10));
        
        ResponseEntity<Page<UserGroupDTO>> responseResult = controller.getPaginatedResult(0, 10, "id", "asc");
        
        assertNotNull(responseResult);
        assertEquals(HttpStatus.OK, responseResult.getStatusCode());
        assertNotNull(responseResult.getBody());

        Page<UserGroupDTO> paginatedResult = responseResult.getBody();
        assertEquals(1, paginatedResult.getContent().size());
        assertDTOProperties(paginatedResult.getContent().get(0));
    }


}
