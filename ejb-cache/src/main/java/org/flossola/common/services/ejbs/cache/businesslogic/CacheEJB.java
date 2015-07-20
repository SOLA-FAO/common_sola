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
package org.flossola.common.services.ejbs.cache.businesslogic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.logging.Level;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.flossola.common.utilities.StringUtility;
import org.flossola.common.utilities.logging.LogUtility;

/**
 * Singleton EJB used as a cache for server side entities marked with the
 * Cacheable Java Persistence Annotation. Primarily intended to cache lists of
 * reference codes.
 *
 * No support is provided to cache individual entities. Care should be taken if
 * individual entities are cached as the cache key is based on the entity class.
 * Attempting to cache individual entities my result it cache entries being
 * overwritten by mistake.
 *
 * Uses Container Concurrency to ensure each method is managed with READ
 * (multiple access) or WRITE (single access) locks
 *
 * @author soladev
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@AccessTimeout(value = 20, unit = SECONDS) // Only wait 20 seconds in the case of a deadlock. 
@EJB(name = "java:app/CacheEJBLocal", beanInterface = CacheEJBLocal.class)
public class CacheEJB implements CacheEJBLocal {

    Map<String, Object> cache = new HashMap<String, Object>();
    private static final String KEY_DIVIDER = "_";

    /**
     * Determines the key to use for the entity using the class name and a
     * suffix. In most cases the suffix should be the language code, so that the
     * different reference code language mappings can be correctly cached. The
     * class name and suffix are appended to create a single cache key.
     *
     * This cache is provided to reduce the number of database queries between
     * the service layer and database. Querying a local cache is much quicker
     * (i.e. 10 to 100 times) than obtaining the data from the database.
     *
     * @param entityClass The class of the entity to cache
     * @param suffix The suffix to use for the entity. Should be the language
     * code, but can be another value if necessary to differentiate lists of the
     * same entity class in the cache. If suffix is null, the text ALL is used
     * to indicate that the list is likely to contain the full text for any
     * display values without translation.
     * @return
     */
    @Override
    public String getKey(Class entityClass, String suffix) {
        return entityClass.getName() + KEY_DIVIDER
                + (StringUtility.isEmpty(suffix) ? "ALL" : suffix);
    }

    /**
     * Checks if the cache already contains a list matching the cache key.
     *
     * @param key Cache key determined using the
     * {@linkplain #getKey(java.lang.Class, java.lang.String) getKey} method
     * @return True if the list is already cached, false otherwise
     */
    @Override
    public boolean isCachedList(String key) {
        return !StringUtility.isEmpty(key) && cache.containsKey(key)
                && List.class.isAssignableFrom(cache.get(key).getClass());
    }

    /**
     * Retrieves a list from the cache
     *
     * @param <T>
     * @param entityClass The entity class of the list
     * @param key Cache key determined using the
     * {@linkplain #getKey(java.lang.Class, java.lang.String) getKey} method
     * @return
     */
    @Override
    public <T> List<T> getList(Class<T> entityClass, String key) {
        LogUtility.log("Get from cache > " + key, Level.INFO);
        return isCachedList(key) ? (List<T>) cache.get(key) : null;
    }

    /**
     * Stores the list into the cache. This method creates a WRITE lock blocking
     * all other access until it is completed.
     *
     * If the cache already contains an entry for the key, the entry is removed
     * and the new list replaces the original.
     *
     * @param <T>
     * @param key Cache key determined using the
     * {@linkplain #getKey(java.lang.Class, java.lang.String) getKey} method
     * @param list The list to cache.
     */
    @Lock(LockType.WRITE)
    @Override
    public <T> void putList(String key, List<T> list) {
        LogUtility.log("Put in cache > " + key, Level.INFO);
        if (cache.containsKey(key)) {
            clearEntry(key);
        }
        cache.put(key, list);
    }

    /**
     * Removes a specific entry as identified by the key from the cache. This
     * method has been extracted from the
     * {@linkplain #clear(java.lang.String) clear} method so that it can be used
     * without from putList without causing a deadlock.
     *
     * @param key Cache key determined using the
     * {@linkplain #getKey(java.lang.Class, java.lang.String) getKey} method
     */
    private void clearEntry(String key) {
        if (cache.containsKey(key)) {
            LogUtility.log("Removing from cache > " + key, Level.INFO);
            cache.remove(key);
        }
    }

    /**
     * Removes a specific entry as identified by the key from the cache.
     *
     * This method creates a WRITE lock blocking all other access until it is
     * completed.
     *
     * @param key Cache key determined using the
     * {@linkplain #getKey(java.lang.Class, java.lang.String) getKey} method
     */
    @Lock(LockType.WRITE)
    @Override
    public void clear(String key) {
        clearEntry(key);
    }

    /**
     * Removes all entries in the cache based on the class of the entity. Used
     * to ensure the cache can be correctly reloaded after an reference code
     * entity is updated.
     *
     * This method creates a WRITE lock blocking all other access until it is
     * completed.
     *
     * @param entityClass The entity class that requires all associated cache
     * lists to be removed/cleared
     */
    @Lock(LockType.WRITE)
    @Override
    public void clearEntityLists(Class entityClass) {
        if (entityClass != null) {
            String className = entityClass.getName();
            Iterator<Entry<String, Object>> it = cache.entrySet().iterator();
            while (it.hasNext()) {
                String tmpKey = it.next().getKey();
                if (className.equals(tmpKey.split(KEY_DIVIDER)[0])) {
                    LogUtility.log("Removing from cache > " + tmpKey, Level.INFO);
                    it.remove();
                }
            }
        }
    }

    /**
     * Completely clears the cache of all entries. Includes a default schedule
     * so that the cache is cleared regularly each day at 5am. More complex
     * scheduling can be configured as necessary.
     *
     * This method creates a WRITE lock blocking all other access until it is
     * completed.
     */
    @Lock(LockType.WRITE)
    @Schedule(hour = "5", info = "Clears the cache daily at 5am")
    @Override
    public void clearAll() {
        LogUtility.log("Empty cache", Level.INFO);
        cache.clear();
    }

}
