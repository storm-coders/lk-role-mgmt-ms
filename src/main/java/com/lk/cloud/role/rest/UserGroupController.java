package com.lk.cloud.role.rest;

import com.lk.cloud.role.config.SwaggerConfig;
import com.lk.cloud.role.dto.UserGroupDTO;
import com.lk.cloud.role.service.UserGroupService;
import com.vcgdev.common.exception.ErrorCode;
import com.vcgdev.common.exception.ServiceException;
import com.vcgdev.common.swagger.SwaggerConstants;

import java.util.Collection;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController()
@RequestMapping("groups")
@Api(produces = SwaggerConstants.APPLICATION_JSON_RESPONSE, tags = SwaggerConfig.TAG_GROUPS)
@Slf4j
@Validated
public class UserGroupController {
    
    private UserGroupService userGroupService;

    public UserGroupController(UserGroupService userGroupService) {
       this.userGroupService = userGroupService;
    }

    @ApiOperation(value = "Create new Group", httpMethod = SwaggerConstants.HTTP_POST, response = UserGroupDTO.class )
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_CREATED, message = SwaggerConstants.CREATED_MESSAGE, response = UserGroupDTO.class),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = SwaggerConstants.BAD_REQUEST_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = SwaggerConstants.INTERNAL_SERVER_ERROR_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = SwaggerConstants.FORBIDDEN_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = SwaggerConstants.UNAUTHORIZED_MESSAGE)
    })
    @PostMapping()
    @PreAuthorize(value = "hasRole('ROLE_CREATE_GROUP')")
    public ResponseEntity<UserGroupDTO> addUserGroup(
        @RequestBody @Valid UserGroupDTO userGroupDTO
    ) throws ServiceException {
        try {         
            UserGroupDTO dto = this.userGroupService.addUserGroup(userGroupDTO);
            return new ResponseEntity<>(dto,HttpStatus.CREATED);    
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("Unable to save request", e);
            throw new ServiceException(ErrorCode.INTERNAL);
        }
    }

    
    @ApiOperation(value = "Update Group", httpMethod = SwaggerConstants.HTTP_PUT, response = UserGroupDTO.class )
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = SwaggerConstants.OK_MESSAGE, response = UserGroupDTO.class),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = SwaggerConstants.BAD_REQUEST_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = SwaggerConstants.INTERNAL_SERVER_ERROR_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = SwaggerConstants.FORBIDDEN_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = SwaggerConstants.UNAUTHORIZED_MESSAGE)
    })
    @PutMapping(value="{id}")
    @PreAuthorize(value = "hasRole('ROLE_UPDATE_GROUP')")
    public ResponseEntity<UserGroupDTO> updateUserGroup(
        @RequestBody @Valid UserGroupDTO userGroupDTO,
        @PathVariable UUID id
        ) throws ServiceException {
        try {
            UserGroupDTO dto = this.userGroupService.updateUserGroup(userGroupDTO, id);        
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("Unable to update request", e);
            throw new ServiceException(ErrorCode.INTERNAL);
        }        
    }

    
    @ApiOperation(value = "Fetch Group by Id", httpMethod = SwaggerConstants.HTTP_GET, response = UserGroupDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = SwaggerConstants.OK_MESSAGE, response = UserGroupDTO.class),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = SwaggerConstants.BAD_REQUEST_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = SwaggerConstants.INTERNAL_SERVER_ERROR_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = SwaggerConstants.FORBIDDEN_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = SwaggerConstants.UNAUTHORIZED_MESSAGE)
    })    
    @GetMapping(value="{id}")
    @PreAuthorize(value = "hasRole('ROLE_VIEW_GROUP_ID')")
    public ResponseEntity<UserGroupDTO> findById(@PathVariable UUID id) throws ServiceException {        
        return new ResponseEntity<>(this.userGroupService.findById(id), HttpStatus.OK);
    }

    
    @ApiOperation(value = "Fetch list of Group", httpMethod = SwaggerConstants.HTTP_GET, response = Collection.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = SwaggerConstants.OK_MESSAGE, response = Collection.class),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = SwaggerConstants.BAD_REQUEST_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = SwaggerConstants.INTERNAL_SERVER_ERROR_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = SwaggerConstants.FORBIDDEN_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = SwaggerConstants.UNAUTHORIZED_MESSAGE)
    })
    @GetMapping()
    @PreAuthorize(value = "hasRole('ROLE_FETCH_GROUPS')")
    public ResponseEntity<Collection<UserGroupDTO>> findAll() throws ServiceException {        
        Collection<UserGroupDTO> dtoList = this.userGroupService.findAll();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    
    @ApiOperation(value = "Fetch paginated result of Group", httpMethod = SwaggerConstants.HTTP_GET, response = Page.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = SwaggerConstants.OK_MESSAGE, response = Page.class),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = SwaggerConstants.BAD_REQUEST_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = SwaggerConstants.INTERNAL_SERVER_ERROR_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = SwaggerConstants.FORBIDDEN_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = SwaggerConstants.UNAUTHORIZED_MESSAGE)
    })
    @GetMapping(value="pages")
    @PreAuthorize(value = "hasRole('ROLE_FETCH_GROUPS')")
    public ResponseEntity<Page<UserGroupDTO>> getPaginatedResult(
            @RequestParam(required = false,defaultValue = "0") Integer page,
            @RequestParam(required = false,defaultValue = "20") Integer size,
            @RequestParam(required = false) String columnToOrder,
            @RequestParam(required = false) String orderType
        ) throws ServiceException {
        Page<UserGroupDTO> paginatedResult = this.userGroupService.getPaginatedResult(page,size,columnToOrder,orderType);
        return new ResponseEntity<>(paginatedResult, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Group by Id", httpMethod = SwaggerConstants.HTTP_DELETE)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = SwaggerConstants.OK_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = SwaggerConstants.BAD_REQUEST_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Not Found"),
        @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = SwaggerConstants.INTERNAL_SERVER_ERROR_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = SwaggerConstants.FORBIDDEN_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = SwaggerConstants.UNAUTHORIZED_MESSAGE)
    })
    @DeleteMapping("{id}")
    @PreAuthorize(value = "hasRole('ROLE_DELETE_GROUP')")
    public ResponseEntity<Void> deleteByIdAndUserId(
        @PathVariable UUID id
        ) throws ServiceException {
            this.userGroupService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
