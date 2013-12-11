package com.massivekinetics.ow.application;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bovy on 12/8/13.
 */
public class AppLocator {

    private AppLocator(){}

    public static void init(){
        if(instance == null)
            instance = new AppLocator();
    }

    public static void register(Class interfaceType, Class instanceType){
        instance.classTypeMap.put(interfaceType, instanceType);
    }

    public static void register(Class interfaceType, Object interfaceInstance){
        instance.objectMap.put(interfaceType, interfaceInstance);
    }

    public static <T> T resolve(Class interfaceType) {
        if(!canResolve(interfaceType))
            return null;

        if(instance.containsObject(interfaceType))
            return (T)instance.objectMap.get(interfaceType);

        Class<T> clsName = instance.classTypeMap.get(interfaceType);
        try {
            return clsName.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static boolean canResolve(Class key){
        return instance.containsObject(key) || instance.containsType(key);
    }

    private boolean containsObject(Class cls){
        return objectMap.containsKey(cls);
    }

    private boolean containsType(Class cls){
        return classTypeMap.containsKey(cls);
    }

    private Map<Class, Object> objectMap = new HashMap<Class, Object>();
    private Map<Class, Class> classTypeMap = new HashMap<Class, Class>();
    private static AppLocator instance;
}
