package me.trilis.au.java.test2;

import java.util.ArrayList;
import java.util.List;

public class Injector {

    private static List<Object> implObjects = new ArrayList<>();
    private static List<String> implementedClasses = new ArrayList<>();
    private static List<Class> stackOfImplementation = new ArrayList<>();

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        stackOfImplementation.add(Class.forName(rootClassName));
        for (var implType : implementationClassNames) {
            if (!implementedClasses.contains(implType)) {
                implementedClasses.add(implType);
                implObjects.add(initialize(implType, implementationClassNames));
            }
        }
        var clazz = Class.forName(rootClassName);
        var constructor = clazz.getConstructors()[0];
        var parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length == 0) {
            return constructor.newInstance();
        }
        var arguments = new ArrayList<>();
        for (var type : parameterTypes) {
            var implementations = new ArrayList<>();
            for (var implObject : implObjects) {
                if (type.isAssignableFrom(implObject.getClass())) {
                    implementations.add(implObject);
                }
            }
            if (implementations.size() == 0) {
                for (var typeFromStack : stackOfImplementation) {
                    System.out.println(type + " " + typeFromStack);
                    if (type.isAssignableFrom(typeFromStack)) {
                        throw new InjectionCycleException();
                    }
                }
                throw new ImplementationNotFoundException();
            }
            if (implementations.size() > 1) {
                throw new AmbiguousImplementationException();
            }
            arguments.add(implementations.get(0));
            implObjects.remove(implementations.get(0));
        }
        stackOfImplementation.remove(Class.forName(rootClassName));
        return constructor.newInstance(arguments.toArray());
    }
}
