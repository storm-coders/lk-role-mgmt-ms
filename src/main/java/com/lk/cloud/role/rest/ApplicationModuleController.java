package com.lk.cloud.role.rest;

import com.lk.cloud.role.config.SwaggerConfig;
import com.lk.cloud.role.dto.ApplicationModuleDTO;
import com.lk.cloud.role.service.ApplicationModuleService;
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
@RequestMapping("modules")
@Api(produces = SwaggerConstants.APPLICATION_JSON_RESPONSE, tags = SwaggerConfig.TAG_MODULE) 
@Slf4j
@Validated
public class ApplicationModuleController {
    
    private ApplicationModuleService applicationModuleService;

    public ApplicationModuleController(ApplicationModuleService applicationModuleService) {
       this.applicationModuleService = applicationModuleService;
    }

    @ApiOperation(value = "Create new Module", httpMethod = SwaggerConstants.HTTP_POST, response = ApplicationModuleDTO.class )
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_CREATED, message = SwaggerConstants.CREATED_MESSAGE, response = ApplicationModuleDTO.class),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = SwaggerConstants.BAD_REQUEST_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = SwaggerConstants.INTERNAL_SERVER_ERROR_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = SwaggerConstants.FORBIDDEN_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = SwaggerConstants.UNAUTHORIZED_MESSAGE)
    })
    @PostMapping()
    @PreAuthorize(value = "hasRole('ROLE_CREATE_MODULE')") 
    public ResponseEntity<ApplicationModuleDTO> addApplicationModule(
        @RequestBody @Valid ApplicationModuleDTO applicationModuleDTO
    ) throws ServiceException {
        try {         
            ApplicationModuleDTO dto = this.applicationModuleService.addApplicationModule(applicationModuleDTO);
            return new ResponseEntity<>(dto,HttpStatus.CREATED);    
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("Unable to save request", e);
            throw new ServiceException(ErrorCode.INTERNAL);
        }
    }

    
    @ApiOperation(value = "Update ApplicationModule", httpMethod = SwaggerConstants.HTTP_PUT, response = ApplicationModuleDTO.class )
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = SwaggerConstants.OK_MESSAGE, response = ApplicationModuleDTO.class),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = SwaggerConstants.BAD_REQUEST_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = SwaggerConstants.INTERNAL_SERVER_ERROR_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = SwaggerConstants.FORBIDDEN_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = SwaggerConstants.UNAUTHORIZED_MESSAGE)
    })
    @PutMapping(value="{id}")
    @PreAuthorize(value = "hasRole('ROLE_UPDATE_MODULE')") 
    public ResponseEntity<ApplicationModuleDTO> updateApplicationModule(
        @RequestBody @Valid ApplicationModuleDTO applicationModuleDTO,
        @PathVariable UUID id
        ) throws ServiceException {
        try {
            ApplicationModuleDTO dto = this.applicationModuleService.updateApplicationModule(applicationModuleDTO, id);        
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("Unable to update request", e);
            throw new ServiceException(ErrorCode.INTERNAL);
        }        
    }

    
    @ApiOperation(value = "Fetch Module by Id", httpMethod = SwaggerConstants.HTTP_GET, response = ApplicationModuleDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = SwaggerConstants.OK_MESSAGE, response = ApplicationModuleDTO.class),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = SwaggerConstants.BAD_REQUEST_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = SwaggerConstants.INTERNAL_SERVER_ERROR_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = SwaggerConstants.FORBIDDEN_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = SwaggerConstants.UNAUTHORIZED_MESSAGE)
    })    
    @GetMapping(value="{id}")
    @PreAuthorize(value = "hasRole('ROLE_VIEW_MODULE_ID')") 
    public ResponseEntity<ApplicationModuleDTO> findById(@PathVariable UUID id) throws ServiceException {        
        return new ResponseEntity<>(this.applicationModuleService.findById(id), HttpStatus.OK);
    }

    
    @ApiOperation(value = "Fetch list of ApplicationModule", httpMethod = SwaggerConstants.HTTP_GET, response = Collection.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = SwaggerConstants.OK_MESSAGE, response = Collection.class),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = SwaggerConstants.BAD_REQUEST_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = SwaggerConstants.INTERNAL_SERVER_ERROR_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = SwaggerConstants.FORBIDDEN_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = SwaggerConstants.UNAUTHORIZED_MESSAGE)
    })
    @GetMapping()
    @PreAuthorize(value = "hasRole('ROLE_FETCH_MODULES')") 
    public ResponseEntity<Collection<ApplicationModuleDTO>> findAll() throws ServiceException {        
        Collection<ApplicationModuleDTO> dtoList = this.applicationModuleService.findAll();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    
    @ApiOperation(value = "Fetch paginated result of Module", httpMethod = SwaggerConstants.HTTP_GET, response = Page.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = SwaggerConstants.OK_MESSAGE, response = Page.class),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = SwaggerConstants.BAD_REQUEST_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = SwaggerConstants.INTERNAL_SERVER_ERROR_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = SwaggerConstants.FORBIDDEN_MESSAGE),
        @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = SwaggerConstants.UNAUTHORIZED_MESSAGE)
    })
    @GetMapping(value="pages")
    @PreAuthorize(value = "hasRole('ROLE_FETCH_MODULES')") 
    public ResponseEntity<Page<ApplicationModuleDTO>> getPaginatedResult(
            @RequestParam(required = false,defaultValue = "0") Integer page,
            @RequestParam(required = false,defaultValue = "20") Integer size,
            @RequestParam(required = false) String columnToOrder,
            @RequestParam(required = false) String orderType
        ) throws ServiceException {
        Page<ApplicationModuleDTO> paginatedResult = this.applicationModuleService.getPaginatedResult(page,size,columnToOrder,orderType);
        return new ResponseEntity<>(paginatedResult, HttpStatus.OK);
    }
}
