/**
 * ******************************************************************************************
 * Copyright (C) 2015 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.flossola.common.services.ejbs;

import java.net.URL;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.flossola.common.utilities.constants.RolesConstants;
import org.flossola.common.utilities.exceptions.SOLAException;
import org.flossola.common.messaging.CommonMessage;
import org.flossola.common.services.EntityAction;
import org.flossola.common.services.LocalInfo;
import org.flossola.common.services.repository.entities.AbstractCodeEntity;
import org.flossola.common.services.repository.CommonRepository;
import org.flossola.common.services.repository.CommonRepositoryImpl;
import org.flossola.common.services.repository.DatabaseConnectionManager;
import org.flossola.common.services.repository.entities.AbstractEntity;

@DeclareRoles({
    RolesConstants.DASHBOARD_VIEW_ASSIGNED_APPS,
    RolesConstants.DASHBOARD_VIEW_UNASSIGNED_APPS,
    RolesConstants.APPLICATION_VIEW_APPS,
    RolesConstants.APPLICATION_CREATE_APPS,
    RolesConstants.APPLICATION_EDIT_APPS,
    RolesConstants.APPLICATION_PRINT_STATUS_REPORT,
    RolesConstants.APPLICATION_ASSIGN_TO_YOURSELF,
    RolesConstants.APPLICATION_ASSIGN_TO_OTHERS,
    RolesConstants.APPLICATION_UNASSIGN_FROM_YOURSELF,
    RolesConstants.APPLICATION_UNASSIGN_FROM_OTHERS,
    RolesConstants.APPLICATION_SERVICE_START,
    RolesConstants.APPLICATION_SERVICE_COMPLETE,
    RolesConstants.APPLICATION_SERVICE_CANCEL,
    RolesConstants.APPLICATION_SERVICE_REVERT,
    RolesConstants.APPLICATION_REQUISITE,
    RolesConstants.APPLICATION_RESUBMIT,
    RolesConstants.APPLICATION_APPROVE,
    RolesConstants.APPLICATION_WITHDRAW,
    RolesConstants.APPLICATION_REJECT,
    RolesConstants.APPLICATION_VALIDATE,
    RolesConstants.APPLICATION_DISPATCH,
    RolesConstants.APPLICATION_ARCHIVE,
    RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE,
    RolesConstants.ADMINISTRATIVE_BA_UNIT_PRINT_CERT,
    RolesConstants.ADMINISTRATIVE_BA_UNIT_SEARCH,
    RolesConstants.ADMINISTRATIVE_NOTATION_SAVE,
    RolesConstants.SOURCE_TRANSACTIONAL,
    RolesConstants.SOURCE_SAVE,
    RolesConstants.SOURCE_SEARCH,
    RolesConstants.SOURCE_PRINT,
    RolesConstants.GIS_VIEW_MAP,
    RolesConstants.GIS_PRINT,
    RolesConstants.CADASTRE_PARCEL_SAVE,
    RolesConstants.PARTY_SAVE,
    RolesConstants.PARTY_RIGHTHOLDERS_SAVE,
    RolesConstants.REPORTS_VIEW,
    RolesConstants.ADMIN_MANAGE_SECURITY,
    RolesConstants.ADMIN_MANAGE_REFDATA,
    RolesConstants.ADMIN_MANAGE_SETTINGS,
    RolesConstants.ADMINISTRATIVE_SYSTEMATIC_REGISTRATION,
    RolesConstants.ADMIN_CHANGE_PASSWORD,
    RolesConstants.CS_ACCESS_CS,
    RolesConstants.ADMINISTRATIVE_ASSIGN_TEAM,
    RolesConstants.CS_MODERATE_CLAIM,
    RolesConstants.CS_RECORD_CLAIM,
    RolesConstants.CS_REVIEW_CLAIM,
    RolesConstants.CLASSIFICATION_CHANGE_CLASS,
    RolesConstants.CLASSIFICATION_UNRESTRICTED,
    RolesConstants.CLASSIFICATION_RESTRICTED,
    RolesConstants.CLASSIFICATION_CONFIDENTIAL,
    RolesConstants.CLASSIFICATION_SECRET,
    RolesConstants.CLASSIFICATION_TOPSECRET,
    RolesConstants.CLASSIFICATION_SUPPRESSION_ORDER,
    RolesConstants.SERVICE_START_CHECKLIST,
    RolesConstants.SERVICE_START_PUBLIC_DISPLAY,
    RolesConstants.SERVICE_START_OBJECTIONS,
    RolesConstants.SERVICE_START_NOTIFY,
    RolesConstants.SERVICE_START_NEGOTIATE
})
public abstract class AbstractEJB implements AbstractEJBLocal {

    @Resource
    private SessionContext sessionContext;
    private CommonRepository repository;
    private String entityPackage;

    /**
     * Returns name of entities package. Should be set explicitely.
     */
    public String getEntityPackage() {
        return entityPackage;
    }

    /** Returns entity list size excluding deleted or disassociated */
    public <T extends AbstractEntity> int getEntityListSize(List<T> entityList) {
        int cnt = 0;
        for (AbstractEntity entity : entityList) {
            if (entity.getEntityAction() == null || (!entity.getEntityAction().equals(EntityAction.DELETE) && !entity.getEntityAction().equals(EntityAction.DISASSOCIATE))) {
                cnt += 1;
            }
        }
        return cnt;
    }
    
    /**
     * Sets name of the entities package.
     */
    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public CommonRepository getRepository() {
        return repository;
    }

    public void setRepository(CommonRepository repository) {
        this.repository = repository;
    }

    @Override
    public void setDbConnectionManager(DatabaseConnectionManager dbConnectionManager) {
        if (this.repository != null) {
            this.repository.setDbConnectionManager(dbConnectionManager);
        }
    }

    /**
     * Checks if current user belongs to any of provided roles.
     *
     * @param roles List of roles to check.
     */
    public boolean isInRole(String... roles) {
        return LocalInfo.isInRole(roles);
    }

    /**
     * Obtains the user name of the currently logged in user.
     *
     * @return The user name.
     */
    public String getUserName() {
        return sessionContext.getCallerPrincipal().getName();
    }

    /**
     * This method is invoked after the container has completed resource
     * injection for the EJB. To perform additional tasks as part of the
     * postConstruct process in descendent EJB's, override the postConstruct
     * method.
     */
    @PostConstruct
    private void onPostConstruct() {
        URL connectConfigFileUrl = this.getClass().getResource(CommonRepository.CONNECT_CONFIG_FILE_NAME);
        repository = new CommonRepositoryImpl(connectConfigFileUrl);
        postConstruct();
    }

    /**
     * This method has no implementation and can be overridden in descendent EJB
     * classes to perform setup actions following the injection of resources
     * into the EJB. Note that in accordance with EJB postConstruct rules, this
     * methods cannot throw a checked exception. Refer to
     * download.oracle.com/javaee/6/api/javax/annotation/PostConstruct.html
     */
    protected void postConstruct() {
    }

    /*
     * This method is triggered by every method invocation on the EJB. Sets the username for the
     * current ejb context. Also triggers the beforeInvoke and afterInvoke methods. It is necessary
     * to set the user name as part of the invoke rather than during postConstruct as the security
     * context is not initialized until after postConstruct.
     */
    @AroundInvoke
    private Object onInvoke(InvocationContext ctx) throws Exception {

        String userName = sessionContext.getCallerPrincipal().getName();
        if (userName == null || userName.isEmpty()) {
            userName = "SOLA_ANONYMOUS";
        }
        LocalInfo.setUserName(userName);
        LocalInfo.setSessionContext(sessionContext);

        beforeInvoke(ctx);
        Object result = ctx.proceed();
        afterInvoke(ctx);
        return result;
    }

    /**
     * This method has no implementation and can be overridden in descendent EJB
     * classes to perform actions just prior to invoking the EJB method.
     */
    protected void beforeInvoke(InvocationContext ctx) throws Exception {
    }

    /**
     * This method has no implementation and can be overridden in descendent EJB
     * classes to perform actions just after invoking the EJB method.
     */
    protected void afterInvoke(InvocationContext ctx) throws Exception {
    }

    /**
     * Performs clean up tasks such as removing all LocalInfo references before
     * destroying the EJB.
     */
    @PreDestroy
    private void onPreDestroy() {
        preDestroy();
        LocalInfo.remove();
    }

    /**
     * This method has no implementation and can be overridden in descendent EJB
     * classes to perform cleanup actions just prior to destroying the EJB
     * object.
     */
    protected void preDestroy() {
    }

    /**
     * Saves {@link AbstractCodeEntity} object in a generic way.
     *
     * @param codeEntity Entity code instance to save.
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_REFDATA)
    @Override
    public <T extends AbstractCodeEntity> T saveCodeEntity(T codeEntity) {
        if (!codeEntity.getClass().getPackage().getName().equals(getEntityPackage())) {
            throw new SOLAException(CommonMessage.EXCEPTION_ENTITY_PACKAGE_VIOLATION);
        }
        return getRepository().saveEntity(codeEntity);
    }

    /**
     * Returns {@link AbstractCodeEntity} object in a generic way.
     *
     * @param codeEntityClass Entity class.
     * @param code Code value to use for retrieving entity.
     */
    @Override
    public <T extends AbstractCodeEntity> T getCodeEntity(Class<T> codeEntityClass, String code) {
        return getCodeEntity(codeEntityClass, code, null);
    }

    /**
     * Returns {@link AbstractCodeEntity} object in a generic way.
     *
     * @param codeEntityClass Entity class.
     * @param code Code value to use for retrieving entity.
     * @param lang Language code
     */
    @Override
    public <T extends AbstractCodeEntity> T getCodeEntity(Class<T> codeEntityClass, String code, String lang) {
//        if (!codeEntityClass.getPackage().getName().equals(getEntityPackage())) {
//            throw new SOLAException(CommonMessage.EXCEPTION_ENTITY_PACKAGE_VIOLATION);
//        }
        return getRepository().getCode(codeEntityClass, code, lang);
    }

    /**
     * Returns list of {@link AbstractCodeEntity} object in a generic way.
     *
     * @param codeEntityClass Entity class.
     * @param lang Language code.
     */
    @Override
    public <T extends AbstractCodeEntity> List<T> getCodeEntityList(Class<T> codeEntityClass, String lang) {
//        if (!codeEntityClass.getPackage().getName().equals(getEntityPackage())) {
//            throw new SOLAException(CommonMessage.EXCEPTION_ENTITY_PACKAGE_VIOLATION);
//        }
        return getRepository().getCodeList(codeEntityClass, lang);
    }

    /**
     * Returns list of {@link AbstractCodeEntity} object in a generic way.
     *
     * @param codeEntityClass Entity class.
     */
    @Override
    public <T extends AbstractCodeEntity> List<T> getCodeEntityList(Class<T> codeEntityClass) {
        return getCodeEntityList(codeEntityClass, null);
    }

    @Override
    public <T extends AbstractEntity> T saveEntity(T entityObject) {
        T result = null;
        if (entityObject != null) {
            result = this.getRepository().saveEntity(entityObject);
        }
        return result;
    }

    @Override
    public <T extends AbstractEntity> T getEntityById(Class<T> entityClass, String id) {
        T result = null;
        if (entityClass != null && id != null) {
            result = this.getRepository().getEntity(entityClass, id);
        }
        return result;
    }
}
