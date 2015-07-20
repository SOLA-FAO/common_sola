/**
 * ******************************************************************************************
 * Copyright (C) 2015 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.flossola.common.services.repository;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.ClassUtils;
import org.flossola.common.utilities.DateUtility;
import org.flossola.common.utilities.constants.RolesConstants;
import org.flossola.common.utilities.exceptions.SOLAException;
import org.flossola.common.utilities.StringUtility;
import org.flossola.common.utilities.logging.LogUtility;
import org.flossola.common.messaging.CommonMessageUtility;
import org.flossola.common.messaging.CommonMessage;
import org.flossola.common.services.LocalInfo;
import org.flossola.common.services.repository.entities.AbstractEntityInfo;
import org.flossola.common.services.repository.entities.AbstractReadOnlyEntity;
import org.flossola.common.services.repository.entities.ChildEntityInfo;
import org.flossola.common.services.repository.entities.ColumnInfo;

/**
 * Repository Utility class providing a number of utility methods for dealing
 * with entities, database repositories and accessing EJBs.
 *
 * This class stores a cache of the metadata for each entity to limit avoid
 * excessive reflection over entity classes during each database operation.
 *
 * @author soladev
 */
public class RepositoryUtility {

    private static Map<String, List<ColumnInfo>> entityColumns = new HashMap<String, List<ColumnInfo>>();
    private static Map<String, List<ColumnInfo>> entityIdColumns = new HashMap<String, List<ColumnInfo>>();
    private static Map<String, String> entityTableNames = new HashMap<String, String>();
    private static Map<String, Boolean> entityCacheable = new HashMap<String, Boolean>();
    private static Map<String, String> sorterExpressions = new HashMap<String, String>();
    private static Map<String, List<ChildEntityInfo>> childEntities = new HashMap<String, List<ChildEntityInfo>>();
    private static Boolean isCacheEJBDeployed = null;

    /**
     * Uses recursion to obtain the list of all declared fields of a class
     * including those fields declared on ancestor classes
     *
     * @param c The class to assess
     * @param fields The list of declared fields for the class and all its super
     * classes. Note that this parameter must be an empty list. The list is
     * populated by the recursive function.
     */
    public static void getAllFields(Class<?> c, List<Field> fields) {
        fields.addAll(Arrays.asList(c.getDeclaredFields()));
        Class<?> superClass = c.getSuperclass();
        if (superClass != null) {
            getAllFields(superClass, fields);
        }
    }

    public static <T extends AbstractReadOnlyEntity> String getTableName(Class<T> entityClass) {
        String tableName = null;
        if (entityTableNames.containsKey(entityClass.getName())) {
            tableName = entityTableNames.get(entityClass.getName());
        } else {
            Table tableAnnotation = entityClass.getAnnotation(Table.class);
            if (tableAnnotation != null) {
                tableName = tableAnnotation.schema() + "." + tableAnnotation.name();
                entityTableNames.put(entityClass.getName(), tableName);
            }
        }
        return tableName;
    }

    /**
     * Indicates if an entity can be cached for fast access. Used for reference
     * codes that are not frequently updated. Will return false if the CacheEJB
     * has not been deployed.
     *
     * @param <T>
     * @param entityClass The entity class to check for the cacheable annotation
     * @return true if the Cacheable Annotation value is true, false otherwise.
     */
    public static <T extends AbstractReadOnlyEntity> boolean isCachable(Class<T> entityClass) {
        boolean result = false;

        if (isCacheEJBDeployed == null) {
            // Check if the CacheEJB has been deployed or not
            isCacheEJBDeployed = RepositoryUtility.tryGetEJB("CacheEJBLocal") != null;
            LogUtility.log("isCacheEJBDeployed = " + isCacheEJBDeployed);
        }
        if (isCacheEJBDeployed) {
            if (entityCacheable.containsKey(entityClass.getName())) {
                result = entityCacheable.get(entityClass.getName());
            } else {
                Cacheable cacheableAnnotation = entityClass.getAnnotation(Cacheable.class);
                if (cacheableAnnotation == null && entityClass.getSuperclass() != null
                        && AbstractReadOnlyEntity.class.isAssignableFrom(entityClass.getSuperclass())) {
                    result = isCachable((Class<AbstractReadOnlyEntity>) entityClass.getSuperclass());
                }
                if (cacheableAnnotation != null) {
                    result = cacheableAnnotation.value();
                    // Set the cacheable state of the entity based on the value of the
                    // Cacheable annotation
                    entityCacheable.put(entityClass.getName(), result);
                } else {
                    // Set the cacheable state for this entity. Note that the result 
                    // may hve been inherited from a super class. 
                    entityCacheable.put(entityClass.getName(), result);
                }
            }
        }
        return result;
    }

    public static <T extends AbstractReadOnlyEntity> String getSorterExpression(Class<T> entityClass) {
        String sorterExpression = null;
        if (sorterExpressions.containsKey(entityClass.getName())) {
            sorterExpression = sorterExpressions.get(entityClass.getName());
        } else {
            DefaultSorter sorterAnnotation = entityClass.getAnnotation(DefaultSorter.class);
            if (sorterAnnotation != null) {
                sorterExpression = sorterAnnotation.sortString();
                sorterExpressions.put(entityClass.getName(), sorterExpression);
            }
        }
        return sorterExpression;
    }

    public static <T extends AbstractReadOnlyEntity> List<ColumnInfo> getColumns(Class<T> entityClass) {

        List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
        if (entityColumns.containsKey(entityClass.getName())) {
            columns = entityColumns.get(entityClass.getName());
        } else {
            List<Field> allFields = new ArrayList<Field>();
            getAllFields(entityClass, allFields);

            for (Field field : allFields) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                if (columnAnnotation != null) {
                    Boolean isId = (field.getAnnotation(Id.class) != null);
                    Boolean isLocalized = (field.getAnnotation(Localized.class) != null);
                    Class<?> fieldType = field.getType();
                    String columnName = columnAnnotation.name();
                    if (columnName == null || columnName.length() < 1) {
                        columnName = field.getName();
                    }
                    ColumnInfo columnInfo = new ColumnInfo(columnName,
                            field.getName(), fieldType, isId, isLocalized,
                            columnAnnotation.insertable(), columnAnnotation.updatable());
                    AccessFunctions accessFunctions = field.getAnnotation(AccessFunctions.class);
                    if (accessFunctions != null) {
                        columnInfo.setOnSelectFunction(accessFunctions.onSelect());
                        columnInfo.setOnChangeFunction(accessFunctions.onChange());
                    }
                    Redact redactInfo = field.getAnnotation(Redact.class);
                    if (redactInfo != null) {
                        columnInfo.setRedact(true);
                        columnInfo.setMinRedactClassification(
                                StringUtility.isEmpty(redactInfo.minClassification()) ? null
                                : redactInfo.minClassification());
                        columnInfo.setRedactMessageCode(
                                StringUtility.isEmpty(redactInfo.messageCode()) ? null
                                : redactInfo.messageCode());
                    }
                    columns.add(columnInfo);
                }
            }
            entityColumns.put(entityClass.getName(), columns);
        }
        return columns;
    }

    /**
     * Retrieves the column info metadata from an entity class based on a field
     * name or database column name.
     *
     * @param <T>
     * @param entityClass The class of entity to retrieve the column info from
     * @param fieldNameOrDbColName The name of the entity field or the name of
     * the database column
     * @return The ColumnInfo metadata if a matching field is found, otherwise
     * null.
     */
    public static <T extends AbstractReadOnlyEntity> ColumnInfo getColumnInfo(Class<T> entityClass,
            String fieldNameOrDbColName) {
        ColumnInfo result = null;
        if (fieldNameOrDbColName != null) {
            for (ColumnInfo columnInfo : getColumns(entityClass)) {
                if (columnInfo.getFieldName().equalsIgnoreCase(fieldNameOrDbColName)) {
                    // Check the entity field name
                    result = columnInfo;
                    break;
                }
                if (columnInfo.getColumnName() != null
                        && columnInfo.getColumnName().equalsIgnoreCase(fieldNameOrDbColName)) {
                    // Check the database column name
                    result = columnInfo;
                    break;
                }
            }
        }
        return result;
    }

    public static <T extends AbstractReadOnlyEntity> ChildEntityInfo getChildEntityInfo(Class<T> entityClass,
            String fieldName) {
        ChildEntityInfo result = null;
        if (fieldName != null) {
            for (ChildEntityInfo childInfo : getChildEntityInfo(entityClass)) {
                if (childInfo.getFieldName().equalsIgnoreCase(fieldName)) {
                    result = childInfo;
                    break;
                }
            }
        }
        return result;
    }

    public static <T extends AbstractReadOnlyEntity> Boolean isIdColumn(Class<T> entityClass,
            String fieldName) {
        Boolean result = false;
        if (fieldName != null) {
            for (ColumnInfo columnInfo : getIdColumns(entityClass)) {
                if (columnInfo.getFieldName().equalsIgnoreCase(fieldName)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public static <T extends AbstractReadOnlyEntity> List<ColumnInfo> getIdColumns(Class<T> entityClass) {
        return getIdColumns(entityClass, getColumns(entityClass));
    }

    public static <T extends AbstractReadOnlyEntity> List<ColumnInfo> getIdColumns(Class<T> entityClass,
            List<ColumnInfo> columns) {
        List<ColumnInfo> idColumns = new ArrayList<ColumnInfo>();
        if (entityIdColumns.containsKey(entityClass.getName())) {
            idColumns = entityIdColumns.get(entityClass.getName());
        } else {
            for (ColumnInfo columnInfo : columns) {
                if (columnInfo.isIdColumn()) {
                    idColumns.add(columnInfo);
                }
            }
            entityIdColumns.put(entityClass.getName(), idColumns);
        }
        return idColumns;
    }

    public static <T extends AbstractReadOnlyEntity> List<ChildEntityInfo> getChildEntityInfo(Class<T> entityClass) {

        List<ChildEntityInfo> children = new ArrayList<ChildEntityInfo>();
        if (childEntities.containsKey(entityClass.getName())) {
            children = childEntities.get(entityClass.getName());
        } else {
            List<Field> allFields = new ArrayList<Field>();
            getAllFields(entityClass, allFields);

            for (Field field : allFields) {
                ChildEntity childAnnotation = field.getAnnotation(ChildEntity.class);
                ChildEntityList childListAnnotation = field.getAnnotation(ChildEntityList.class);
                ExternalEJB externalEJBAnnoation = field.getAnnotation(ExternalEJB.class);
                Redact redactInfo = field.getAnnotation(Redact.class);
                ParameterizedType paramType = null;
                if (Iterable.class.isAssignableFrom(field.getType())) {
                    paramType = (ParameterizedType) field.getGenericType();
                }
                ChildEntityInfo childInfo = null;
                if (childAnnotation != null) {
                    boolean insert = childAnnotation.insertBeforeParent();

                    if ((insert && childAnnotation.childIdField().isEmpty())
                            || (!insert && childAnnotation.parentIdField().isEmpty())) {
                        throw new SOLAException(CommonMessage.GENERAL_UNEXPECTED,
                                // The ChildEntity annoation is not configured correctly
                                new Object[]{"ChildEntity annotation is not configured correclty on "
                                    + entityClass.getSimpleName() + "." + field.getName()});
                    }

                    childInfo = new ChildEntityInfo(field.getName(), field.getType(), insert,
                            childAnnotation.parentIdField(), childAnnotation.childIdField(),
                            childAnnotation.readOnly());
                }
                if (childListAnnotation != null) {
                    childInfo = new ChildEntityInfo(field.getName(), field.getType(), paramType,
                            childListAnnotation.parentIdField(), childListAnnotation.childIdField(),
                            childListAnnotation.manyToManyClass(),
                            childListAnnotation.cascadeDelete(),
                            childListAnnotation.readOnly());
                }
                if (externalEJBAnnoation != null && childInfo != null) {
                    childInfo.setEJBLocalClass(externalEJBAnnoation.ejbLocalClass());
                    childInfo.setLoadMethod(externalEJBAnnoation.loadMethod());
                    childInfo.setSaveMethod(externalEJBAnnoation.saveMethod());
                }
                if (redactInfo != null && childInfo != null) {
                    // Capture redact details but ignore the messageCode as this does not
                    // apply to List or child entity fields
                    childInfo.setRedact(true);
                    childInfo.setMinRedactClassification(
                            StringUtility.isEmpty(redactInfo.minClassification()) ? null
                            : redactInfo.minClassification());
                }
                if (childInfo != null) {
                    children.add(childInfo);
                }
            }
            childEntities.put(entityClass.getName(), children);
        }
        return children;
    }

    public static <T> T getEJB(Class<T> ejbLocalClass) {
        T ejb = null;

        String ejbLookupName = "java:app/" + ejbLocalClass.getSimpleName();
        try {
            InitialContext ic = new InitialContext();
            ejb = (T) ic.lookup(ejbLookupName);
        } catch (NamingException ex) {
            throw new SOLAException(CommonMessage.GENERAL_UNEXPECTED,
                    // Capture the specific details so they are added to the log
                    new Object[]{"Unable to locate EJB " + ejbLookupName, ex});
        }
        return ejb;
    }

    public static <T> T tryGetEJB(Class<T> ejbLocalClass) {
        T ejb = null;

        String ejbLookupName = "java:app/" + ejbLocalClass.getSimpleName();
        try {
            InitialContext ic = new InitialContext();
            ejb = (T) ic.lookup(ejbLookupName);
        } catch (NamingException ex) {
            // Ignore the naming exception and return null; 
        }
        return ejb;
    }

    public static <T> T tryGetEJB(String ejbLocalClass) {
        T ejb = null;

        String ejbLookupName = "java:app/" + ejbLocalClass;
        try {
            InitialContext ic = new InitialContext();
            ejb = (T) ic.lookup(ejbLookupName);
        } catch (NamingException ex) {
            // Ignore the naming exception and return null; 
        }
        return ejb;
    }

    /**
     * Issue #192 Compare two arrays to determine if they are equal or not.
     * Performs a deep comparison of all array members using the Arrays.equal
     * method. Uses the array class to determine the correct cast to apply to
     * the object parameters.
     *
     * @param arrayClass The class for the array
     * @param array1 One of the arrays to compare
     * @param array2 The other array to compare
     * @return true if both arrays are equal
     */
    public static boolean arraysAreEqual(Class<?> arrayClass, Object array1, Object array2) {
        boolean result = false;
        if (byte[].class.isAssignableFrom(arrayClass)) {
            result = Arrays.equals((byte[]) array1, (byte[]) array2);
        } else if (Object[].class.isAssignableFrom(arrayClass)) {
            result = Arrays.equals((Object[]) array1, (Object[]) array2);
        } else if (int[].class.isAssignableFrom(arrayClass)) {
            result = Arrays.equals((int[]) array1, (int[]) array2);
        } else if (char[].class.isAssignableFrom(arrayClass)) {
            result = Arrays.equals((char[]) array1, (char[]) array2);
        } else if (long[].class.isAssignableFrom(arrayClass)) {
            result = Arrays.equals((long[]) array1, (long[]) array2);
        } else if (short[].class.isAssignableFrom(arrayClass)) {
            result = Arrays.equals((short[]) array1, (short[]) array2);
        } else if (boolean[].class.isAssignableFrom(arrayClass)) {
            result = Arrays.equals((boolean[]) array1, (boolean[]) array2);
        } else if (float[].class.isAssignableFrom(arrayClass)) {
            result = Arrays.equals((float[]) array1, (float[]) array2);
        } else if (double[].class.isAssignableFrom(arrayClass)) {
            result = Arrays.equals((double[]) array1, (double[]) array2);
        }
        return result;
    }

    /**
     * Issue #192 Compare two arrays to determine if they are equal or not.
     * Geometries are held as byte arrays, however a basic comparison of the
     * byte data does not give an accurate equals test due to Big Endian vs
     * Little Endian issues. Instead it is necessary to create the geometries
     * and use the explicit equals to their geometric equivalence.
     *
     * @param geom1 A geom to compare - can be NULL
     * @param geom2 The second geom to compare - can be NULL
     * @return TRUE if both geoms are NULL or geometrically equivalent. FALSE
     * otherwise.
     */
    public static boolean geometriesAreEqual(Object geom1, Object geom2) {
        boolean result = false;
        if (geom1 == null && geom2 == null) {
            result = true;
        } else if (geom1 != null && geom2 != null) {
            try {
                Geometry g1 = new WKBReader().read((byte[]) geom1);
                Geometry g2 = new WKBReader().read((byte[]) geom2);
                result = g1.equals(g2);
            } catch (ParseException ex) {
                LogUtility.log("Unable to compare geometries. Parse Error:" + ex.getMessage(), ex);
                result = false;
            }
        }
        return result;
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
    public static boolean hasSecurityClearance(String classificationCode) {
        boolean result;
        if (StringUtility.isEmpty(classificationCode)
                || RolesConstants.CLASSIFICATION_UNRESTRICTED.equals(classificationCode)) {
            result = true;
        } else if (RolesConstants.CLASSIFICATION_RESTRICTED.equals(classificationCode)) {
            result = LocalInfo.isInRole(RolesConstants.CLASSIFICATION_RESTRICTED,
                    RolesConstants.CLASSIFICATION_CONFIDENTIAL,
                    RolesConstants.CLASSIFICATION_SECRET,
                    RolesConstants.CLASSIFICATION_TOPSECRET);
        } else if (RolesConstants.CLASSIFICATION_CONFIDENTIAL.equals(classificationCode)) {
            result = LocalInfo.isInRole(RolesConstants.CLASSIFICATION_CONFIDENTIAL,
                    RolesConstants.CLASSIFICATION_SECRET,
                    RolesConstants.CLASSIFICATION_TOPSECRET);
        } else if (RolesConstants.CLASSIFICATION_SECRET.equals(classificationCode)) {
            result = LocalInfo.isInRole(RolesConstants.CLASSIFICATION_SECRET,
                    RolesConstants.CLASSIFICATION_TOPSECRET);
        } else if (RolesConstants.CLASSIFICATION_TOPSECRET.equals(classificationCode)) {
            result = LocalInfo.isInRole(RolesConstants.CLASSIFICATION_TOPSECRET);
        } else {
            // Specialty Classification so allow users with that clearance or 
            // TOP SECRET to view it. Note that the Speciality Classification must
            // be statically declared using the @DeclareRoles annotation on 
            // AbstractEJB otherwise it will be ignored by the isInRole check. 
            result = LocalInfo.isInRole(classificationCode,
                    RolesConstants.CLASSIFICATION_TOPSECRET);
        }
        return result;
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
    public static boolean isRedactRequired(AbstractEntityInfo columnInfo, String redactCode) {
        boolean result = false;
        if (columnInfo.isRedact()) {
            if (!StringUtility.isEmpty(redactCode)) {
                // An override redact code is set, so check if the user has this
                // security clearance. If not, redact the field. 
                result = !hasSecurityClearance(redactCode);
            } else {
                // No override redact code, so check if the user has the minimum
                // classificaiton required (where one is set). If not, redact the field
                result = !hasSecurityClearance(columnInfo.getMinRedactClassification());
            }
        }

        return result;
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
    public static Object getRedactedValue(ColumnInfo columnInfo) {
        Object result = null;
        Class<?> fieldType;
        String msg;
        if (columnInfo.getFieldType().isPrimitive()) {
            fieldType = ClassUtils.primitiveToWrapper(columnInfo.getFieldType());
        } else {
            fieldType = columnInfo.getFieldType();
        }
        if (!StringUtility.isEmpty(columnInfo.getRedactMessageCode())) {
            // Determine the message to use when redacting the field
            msg = CommonMessageUtility.getLocalizedMessageText(columnInfo.getRedactMessageCode(),
                    LocalInfo.get(CommonSqlProvider.PARAM_LANGUAGE_CODE, String.class));
            if (String.class.isAssignableFrom(fieldType)
                    || Character.class.isAssignableFrom(fieldType)
                    || char.class.isAssignableFrom(fieldType)) {
                // The field is a string or char type
                result = msg;
            } else if (Date.class.isAssignableFrom(fieldType)) {
                // The field is a date, use the DateUtility to parse the date time based
                // on the specified redact date format
                String dateFormat =  CommonMessageUtility.getLocalizedMessageText(CommonMessage.REDACT_DATE_FORMAT);
                result = DateUtility.convertToDate(msg, dateFormat);
                if (result == null) {
                    org.flossola.common.services.logging.LogUtility.log("Unable to parse redacted date "
                            + "value for " + columnInfo.getColumnName() + ". Date must be "
                            + "in " + dateFormat + " format. See RepositoryUtility.getRedactedValue", Level.SEVERE);
                }
            } else {
                // The field is not a string or char, so attempt to convert the 
                //redact message to the appropriate data type
                try {
                    result = fieldType.getConstructor(String.class).newInstance(msg);
                } catch (Exception e) {
                    org.flossola.common.services.logging.LogUtility.log("Failed to cast redact "
                            + "value for " + columnInfo.getColumnName(), e);
                }
            }
        } else {
            // No redact message code is provided, so try to create a default object to use
            // for the redact value. 
            try {
                result = fieldType.newInstance();
            } catch (Exception ex) {
                org.flossola.common.services.logging.LogUtility.log("Failed to instantiate default"
                        + " redact value for " + columnInfo.getColumnName(), ex);
            }
        }
        return result;
    }
}
