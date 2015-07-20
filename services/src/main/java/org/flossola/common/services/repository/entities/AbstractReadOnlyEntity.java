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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.flossola.common.services.repository.entities;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Array;
import java.util.List;
import java.util.Map;
import org.flossola.common.utilities.constants.RolesConstants;
import org.flossola.common.utilities.exceptions.SOLAException;
import org.flossola.common.messaging.CommonMessage;
import org.flossola.common.services.LocalInfo;
import org.flossola.common.services.repository.CommonRepository;
import org.flossola.common.services.repository.CommonSqlProvider;
import org.flossola.common.services.repository.RepositoryUtility;

/**
 * The base class for all SOLA entities. This entity should be used to capture
 * simple read only query results. If the entity must update the database, then
 * extend {@linkplain AbstractEntity} or one of its descendents.
 *
 * @author soladev
 */
public abstract class AbstractReadOnlyEntity implements Serializable {

    private boolean loaded = false;
    private boolean getValueException = false;
    private boolean redacted = false;
    /**
     * The name of the column that contains the security classification assigned
     * to the entity. If a security classification is set on the entity and the
     * user does not have a matching (or higher) security classification, the
     * entity will not show in any results sets displayed to the user. To give a
     * user access to a classified record, they must be assigned the appropriate
     * SOLA Security Role.
     */
    public static final String CLASSIFICATION_CODE_COLUMN_NAME = "classification_code";
    /**
     * The name of the column that contains the redact code assigned to the
     * entity. If a redact code is set on the entity and the user does not have
     * the matching SOLA Security Role, the fields on the entity marked with the
     * {@linkplain org.sola.services.common.repository.Redact} annotation will
     * be redacted. Entity fields may also be redacted in bulk if the user does
     * not have a Security Classifications matching the list of classifications
     * identified on the {@linkplain org.sola.services.common.repository.Redact}
     * annotation.
     */
    public static final String REDACT_CODE_COLUMN_NAME = "redact_code";

    /**
     * @return Generates and returns a new UUID value.
     */
    protected String generateId() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * @return true if the entity was loaded from the database.
     */
    public Boolean isLoaded() {
        return loaded;
    }

    /**
     * Sets a flag indicating the entity was loaded from the database.
     *
     * @param loaded
     */
    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isRedacted() {
        return redacted;
    }

    public void setRedacted(boolean redacted) {
        this.redacted = redacted;
    }

    /**
     * Flags if the entity has been saved to the database or not.
     *
     * @return true if the entity was not loaded from the database (i.e.
     * !isLoaded()).
     */
    public Boolean isNew() {
        return !isLoaded();
    }

    /**
     * Obtains the value from the field indicated by the entityInfo parameter
     * using reflection.
     *
     * @param entityInfo Details of the field / column to get the value from.
     * @return The value of the field or an exception if the field indicated by
     * the entityInfo does not exist.
     */
    public Object getEntityFieldValue(AbstractEntityInfo entityInfo) {
        Object result = null;
        try {
            Method getter = this.getClass().getMethod(entityInfo.getterName());
            result = getter.invoke(this);
        } catch (Exception ex) {
            if (!getValueException) {
                // this.toString calls getEntityFieldValue. getValueException is used to avoid
                // a stack overflow if the exception is for an Id field. 
                getValueException = true;
                throw new SOLAException(CommonMessage.GENERAL_UNEXPECTED,
                        new Object[]{"Unable to get value from " + entityInfo.getterName()
                            + " for entity " + this.toString(), ex, ex.getCause()});
            }
        }
        return result;
    }

    /**
     * Sets the value of a field to the specified object using reflection. If
     * the field indicated by the entityInfo does not exist or the type of the
     * value does not match the field type an exception is raised.
     * <p>
     * Note that Mybatis treats char fields as strings. To avoid an unnecessary
     * type mismatch exception when setting a Character field, this method
     * converts the string value to a Character. Type conversion from Integer to
     * Short is also performed when necessary.
     * </p>
     *
     * @param entityInfo Details of the field / column to set the value to.
     * @param value The object to set the field to.
     */
    public void setEntityFieldValue(AbstractEntityInfo entityInfo, Object value) {
        try {

            Class<?> fieldType = entityInfo.getFieldType();
            // Mybatis serves up Character and char fields as String, so check if the target field
            // is char and if the object is String, convert it to a char value. 
            if (value != null && String.class.isAssignableFrom(value.getClass())
                    && (Character.class.isAssignableFrom(fieldType)
                    || char.class.isAssignableFrom(fieldType))) {
                value = new Character(value.toString().charAt(0));
            }

            // Mybatis also has problems with Short serving these up as Integer
            if (value != null && Integer.class.isAssignableFrom(value.getClass())
                    && (Short.class.isAssignableFrom(fieldType)
                    || short.class.isAssignableFrom(fieldType))) {
                value = new Short(value.toString());
            }

            // Support retreival of array columns from the database. 
            if (value != null && Array.class.isAssignableFrom(value.getClass())) {
                value = ((Array) value).getArray();
            }

            Method setter = this.getClass().getMethod(entityInfo.setterName(), fieldType);
            setter.invoke(this, value);
        } catch (Exception ex) {
            String valueType = "<null>";
            if (value != null) {
                valueType = value.getClass().getSimpleName();
            }

            throw new SOLAException(CommonMessage.GENERAL_UNEXPECTED,
                    new Object[]{"Unable to set value to " + entityInfo.setterName()
                        + " for entity " + this.toString(),
                        "Field Type: " + entityInfo.getFieldType().getSimpleName()
                        + ", Value Type:" + valueType, ex});
        }

    }

    /**
     * @return Returns the value for the unique identifier for the entity (as
     * indicated by the @Id annotation) or null if the entity has 0 or more than
     * 1 fields marked as @Id.
     */
    public String getEntityId() {
        String result = null;
        if (getIdColumns().size() == 1) {
            result = getEntityFieldValue(getIdColumns().get(0)).toString();
        }
        return result;
    }

    /**
     * @return The list of fields on the entity marked with the JPA
     * {@linkplain javax.persistence.Column} annotation.
     */
    public List<ColumnInfo> getColumns() {
        return RepositoryUtility.getColumns(this.getClass());
    }

    /**
     * @return The list of fields on the entity marked with the JPA
     * {@linkplain javax.persistence.Id} annotation.
     */
    public List<ColumnInfo> getIdColumns() {
        return RepositoryUtility.getIdColumns(this.getClass(), getColumns());
    }

    /**
     * Determines if the field indicated is an id column or not.
     *
     * @param fieldName The name of the field to check.
     * @return true if the field is an id field on the entity.
     */
    public Boolean isIdColumn(String fieldName) {
        return RepositoryUtility.isIdColumn(this.getClass(), fieldName);
    }

    /**
     * @return The list of fields on the entity marked with the
     * {@linkplain org.sola.services.common.repository.ChildEntity} and/or
     * {@linkplain org.sola.services.common.repository.ChildEntityList}
     * annotation.
     */
    public List<ChildEntityInfo> getChildEntityInfo() {
        return RepositoryUtility.getChildEntityInfo(this.getClass());
    }

    /**
     * Retrieves the ColumnInfo object for a specific column of the entity.
     *
     * @param fieldName The name of the field to obtain the column info for.
     */
    public ColumnInfo getColumnInfo(String fieldName) {
        return RepositoryUtility.getColumnInfo(this.getClass(), fieldName);
    }

    /**
     * Retrieves the ChildEntityInfo object for a specific column of the entity.
     *
     * @param fieldName The name of the field to obtain the child entity info
     * for.
     */
    public ChildEntityInfo getChildEntityInfo(String fieldName) {
        return RepositoryUtility.getChildEntityInfo(this.getClass(), fieldName);
    }

    /**
     * @return The name of the database table (prefixed with the schema if
     * specified) this entity represents. This information is obtained from the
     * JPA {@linkplain javax.persistence.Table} annotation.
     */
    public String getTableName() {
        return RepositoryUtility.getTableName(this.getClass());
    }

    /**
     * @return True if the entity is annotated with a Cacheable annotation that
     * has its value set to true.
     */
    public boolean isCacheable() {
        return RepositoryUtility.isCachable(this.getClass());
    }

    /**
     * Allows the SQL parameters used to retrieve child entities to be set,
     * overriding the default join criteria used by
     * {@linkplain CommonRepository}. This override is available for One to One,
     * One to Many and Many to Many joins.
     * <p>
     * Override this method in the parent entity and populate the parameter map
     * with the appropriate SQL parts (e.g.
     * {@linkplain CommonSqlProvider#PARAM_WHERE_PART}) for the join.
     * </p>
     *
     * @param <T> The generic type of the child entity. Must be a descendent of
     * {@linkplain AbstractReadOnlyEntity}.
     * @param childInfo The ChildEntityInfo for the child being loaded. Can be
     * used to help differentiate children of the parent class.
     * @return The SQL Parameter Map to use for retrieving the child entities.
     */
    public Map<String, Object> getChildJoinSqlParams(ChildEntityInfo childInfo) {
        return null;
    }

    /**
     * Checks the Security Classification to ensure the user has the appropriate
     * clearance to view the record details. The security clearance assigned to
     * a user will grant them access to all records that have the same or lower
     * security classification. Where a specialty classification is being used,
     * the user must have that clearance assigned (as a security role) or have a
     * clearance level of Top Secret.
     *
     * @param classificationCode The security classification to check
     * @return true user has the appropriate security clearance to view the
     * entity, false otherwise.
     */
    public boolean hasSecurityClearance(String classificationCode) {
        return RepositoryUtility.hasSecurityClearance(classificationCode);
    }

    /**
     * Returns a default redact code for the entity. To be overridden in
     * descendent classes that implement redact security controls.
     *
     * @return Always null unless this method is overridden in a descendent
     * class.
     */
    public String getRedactCode() {
        return null;
    }

    /**
     * Returns a default security classification code for the entity. To be
     * overridden in descendent classes that implement security classification
     * controls.
     *
     * @return Always null unless this method is overridden in a descendent
     * class.
     */
    public String getClassificationCode() {
        return null;
    }

    /**
     * Determines if the the contents of a field need to be redacted or not
     * based on the security clearance assigned to the user and the minimum
     * redact classification for the field. The minimum redaction classification
     * can be overridden by setting the redact_code field on the entity
     * explicitly.
     *
     * @param columnInfo The column info for the field being checked
     * @param redactCode The override redact code set on the entity.
     * @return true if the content of the field must be redacted, false
     * otherwise.
     */
    public boolean isRedactRequired(AbstractEntityInfo columnInfo, String redactCode) {
        return RepositoryUtility.isRedactRequired(columnInfo, redactCode);
    }

    /**
     * Attempts to obtain the appropriate redacted value for a field. This
     * method attempts to provide a redacted value that matches the type of the
     * field. The messageCode on the Redact annotation can be used to indicate
     * the a custom redact value.
     *
     * @param columnInfo
     * @return The value to use to redact the field or null.
     */
    public Object getRedactedValue(ColumnInfo columnInfo) {
        return RepositoryUtility.getRedactedValue(columnInfo);
    }

    /**
     * Validates the column to verify if it can be used during an update
     * operation. It checks 1) The column is marked as updatable in the Column
     * annotation 2) The field has not been redacted. 3) If the field is a
     * security column (classification_code or redact_code), then the user has
     * permission to change the security classification. 4) If the user has
     * removed the redact code for the entity which would otherwise cause the
     * redacated value to overwrite the original data
     *
     * @param columnInfo The column info object for the column to verify.
     * @return true if the field can be used in the insert, false otherwise.
     */
    public boolean isUpdatable(ColumnInfo columnInfo) {
        boolean result = false;
        if (columnInfo.isUpdatable() && !isRedactRequired(columnInfo, getRedactCode())) {
            if (REDACT_CODE_COLUMN_NAME.equals(columnInfo.getColumnName())
                    || CLASSIFICATION_CODE_COLUMN_NAME.equals(columnInfo.getColumnName())) {
                result = LocalInfo.isInRole(RolesConstants.CLASSIFICATION_CHANGE_CLASS);
            } else if (columnInfo.isRedact() && this.isRedacted()) {
                // The column is a redact column and the entity was redacted when retrieved
                // from the database. The user may have modified the redactCode so that
                // the entity no longer requires redaction, however this field must be 
                // omitted from the update otherwise it will overwrite the original field 
                // value with the redacted value.
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }

    /**
     * Validates the column to verify if it can be used during an insert
     * operation. It checks 1) The column is marked as insertable in the Column
     * annotation 2) The value is not null. Null values are omitted to ensure
     * any default values are correctly assigned by the database. 3) If the
     * field is a security column (classification_code or redact_code), then the
     * user has permission to change the security classification.
     *
     * @param columnInfo The column info object for the column to verify.
     * @return true if the field can be used in the insert, false otherwise.
     */
    public boolean isInsertable(ColumnInfo columnInfo) {
        boolean result = false;
        Object value = this.getEntityFieldValue(columnInfo);
        if (value != null && columnInfo.isInsertable()) {
            if (REDACT_CODE_COLUMN_NAME.equals(columnInfo.getColumnName())
                    || CLASSIFICATION_CODE_COLUMN_NAME.equals(columnInfo.getColumnName())) {
                result = LocalInfo.isInRole(RolesConstants.CLASSIFICATION_CHANGE_CLASS);
            } else {
                result = true;
            }
        }
        return result;
    }

    /**
     * Overridden to be consistent with the {@linkplain #equals}.
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        for (ColumnInfo idColumnInfo : getIdColumns()) {
            Object idValue = getEntityFieldValue(idColumnInfo);
            hash += (idValue != null ? idValue.hashCode() : 0);
        }
        return hash;
    }

    /**
     * Compares objects using the id columns of the entities. Note that the
     * object must be the same class as this entity. During TO to Entity
     * translation, Dozer uses this method to match TO's to existing entities.
     * As some TO's omit parent id values from association entities (e.g. see
     * PartyRoleTO), the {@linkplain AbstractVersionedEntity#equals} overrides
     * this implementation to use the rowId value for comparison.
     *
     * @param object The Object to compare with this one.
     * @return true if the values of the id columns on the entities match.
     */
    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object != null && object.getClass() == this.getClass()) {
            result = this.toString().equals(object.toString());
        }
        return result;
    }

    /**
     * Returns the class if the entity along with the values for any id columns
     * marked on the entity.
     *
     * @return
     */
    @Override
    public String toString() {
        String result = this.getClass().getSimpleName();
        for (ColumnInfo idColumnInfo : getIdColumns()) {
            Object idValue = getEntityFieldValue(idColumnInfo);
            result += ", " + idColumnInfo.getFieldName() + "="
                    + (idValue == null ? "null" : idValue.toString());
        }
        return result;
    }
}
