package me.trilis.au.java.hw5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.util.HashSet;

public class Reflector {

    public static void printStructure(Class<?> someClass) throws FileNotFoundException {
        var writer = new PrintWriter(new File(someClass.getSimpleName() + ".java"));
        writer.print(printClass(someClass, 0));
        writer.close();
    }

    public static void diffClasses(Class<?> a, Class<?> b, PrintStream out) {
        var aFields = getFieldWrappers(a);
        var bFields = getFieldWrappers(b);
        var aMethods = getMethodWrappers(a);
        var bMethods = getMethodWrappers(b);
        var fieldsUniqueToA = aMinusB(aFields, bFields);
        var fieldsUniqueToB = aMinusB(bFields, aFields);
        var methodsUniqueToA = aMinusB(aMethods, bMethods);
        var methodsUniqueToB = aMinusB(bMethods, aMethods);
        if (fieldsUniqueToA.size() != 0) {
            out.println("Fields unique to the first class:");
            out.println(fieldsUniqueToA);
        }
        if (fieldsUniqueToB.size() != 0) {
            out.println("Fields unique to the second class:");
            out.println(fieldsUniqueToB);
        }
        if (methodsUniqueToA.size() != 0) {
            out.println("Methods unique to the first class:");
            out.println(methodsUniqueToA);
        }
        if (methodsUniqueToB.size() != 0) {
            out.println("Methods unique to the second class:");
            out.println(methodsUniqueToB);
        }
    }

    private static HashSet<String> getFieldWrappers(Class<?> clazz) {
        var result = new HashSet<String>();
        for (var field : clazz.getDeclaredFields()) {
            if (!field.isSynthetic()) {
                result.add(printFieldDeclaration(field, 0).toString());
            }
        }
        return result;
    }

    private static HashSet<String> getMethodWrappers(Class<?> clazz) {
        var result = new HashSet<String>();
        for (var method : clazz.getDeclaredMethods()) {
            if (!method.isSynthetic()) {
                result.add(printMethodDeclaration(method, 0).toString());
            }
        }
        return result;
    }

    private static HashSet<String> aMinusB(HashSet<String> a, HashSet<String> b) {
        var result = new HashSet<>(a);
        result.removeAll(b);
        return result;
    }

    private static StringBuilder printClass(Class<?> someClass, int indentation) {
        var result = new StringBuilder();
        if (someClass.isSynthetic()) {
            return result;
        }
        result.append(printDeclaration(someClass, someClass.getSimpleName(), indentation));
        for (var constructor : someClass.getConstructors()) {
            result.append(printConstructor(constructor, indentation + 1));
        }
        for (var field : someClass.getDeclaredFields()) {
            result.append(printField(field, indentation + 1));
        }
        for (var method : someClass.getDeclaredMethods()) {
            result.append(printMethod(method, indentation + 1));
        }
        for (var clazz : someClass.getDeclaredClasses()) {
            result.append(printClass(clazz, indentation + 1));
        }
        result.append(printIndentation(indentation)).append("}\n");
        return result;
    }

    private static StringBuilder printConstructor(Constructor<?> constructor,
                                                  int indentation) {
        var result = new StringBuilder();
        if (constructor.isSynthetic()) {
            return result;
        }
        result.append(printIndentation(indentation))
                .append(printModifiers(constructor.getModifiers()))
                .append(constructor.getDeclaringClass().getSimpleName())
                .append('(')
                .append(printParameters(constructor.getParameters()))
                .append(")")
                .append(printExceptions(constructor.getGenericExceptionTypes()))
                .append(" {\n")
                .append(printIndentation(indentation + 1))
                .append("throw new UnsupportedOperationException();\n")
                .append(printIndentation(indentation))
                .append("}\n");
        return result;
    }

    private static StringBuilder printMethod(Method method, int indentation) {
        var result = printMethodDeclaration(method, indentation);
        result.append(" {\n")
                .append(printIndentation(indentation + 1))
                .append("throw new UnsupportedOperationException();\n")
                .append(printIndentation(indentation))
                .append("}\n");
        return result;
    }

    private static StringBuilder printMethodDeclaration(Method method, int indentation) {
        var result = new StringBuilder();
        if (method.isSynthetic()) {
            return result;
        }
        result.append(printIndentation(indentation))
                .append(printModifiers(method.getModifiers()))
                .append(method.getGenericReturnType())
                .append(' ')
                .append(method.getName())
                .append('(')
                .append(printParameters(method.getParameters())).
                append(")")
                .append(printExceptions(method.getGenericExceptionTypes()));
        return result;
    }

    private static StringBuilder printExceptions(Type[] exceptions) {
        var result = new StringBuilder();
        var isFirstException = true;
        for (var exception : exceptions) {
            if (!isFirstException) {
                result.append(", ");
            } else {
                result.append(" throws ");
                isFirstException = false;
            }
            result.append(exception.getTypeName());
        }
        return result;
    }

    private static StringBuilder printParameters(Parameter[] parameters) {
        var result = new StringBuilder();
        var isFirstParameter = true;
        for (var parameter : parameters) {
            if (parameter.isSynthetic()) {
                continue;
            }
            if (!isFirstParameter) {
                result.append(", ");
            }
            result.append(parameter.toString());
            isFirstParameter = false;
        }
        return result;
    }


    private static StringBuilder printField(Field field, int indentation) {
        var result = printFieldDeclaration(field, indentation);
        if (field.isSynthetic()) {
            return result;
        }
        if (Modifier.isFinal(field.getModifiers())) {
            result.append(" = ");
            if (field.getType().isPrimitive()) {
                result.append(field.getType().equals(boolean.class) ? "false" : "0");
            } else {
                result.append("null");
            }
        }
        result.append(";\n");
        return result;
    }

    private static StringBuilder printFieldDeclaration(Field field, int indentation) {
        var result = new StringBuilder();
        if (field.isSynthetic()) {
            return result;
        }
        result.append(printIndentation(indentation))
                .append(printModifiers(field.getModifiers()))
                .append(field.getGenericType().getTypeName())
                .append(' ')
                .append(field.getName());
        return result;
    }

    private static StringBuilder printDeclaration(Class<?> someClass,
                                                  String name,
                                                  int indentation) {
        var result = new StringBuilder();
        result.append(printIndentation(indentation))
                .append(printModifiers(someClass.getModifiers()))
                .append(Modifier.isInterface(someClass.getModifiers()) ? "" : "class ")
                .append(name)
                .append(printTypeParameters(someClass.getTypeParameters()));
        if (someClass.getSuperclass() != null &&
                !someClass.getSuperclass().equals(Object.class)) {
            result.append(" extends ");
            result.append(someClass.getGenericSuperclass().getTypeName());
        }
        boolean isFirstInterface = true;
        for (var clazz : someClass.getGenericInterfaces()) {
            if (!isFirstInterface) {
                result.append(", ");
            } else {
                result.append(" implements ");
                isFirstInterface = false;
            }
            result.append(clazz.getTypeName());
        }
        result.append(" {")
                .append("\n");
        return result;
    }

    private static StringBuilder printTypeParameters(
            TypeVariable<? extends Class<?>>[] typeParameters) {
        var result = new StringBuilder();
        if (typeParameters.length == 0) {
            return result;
        }
        result.append('<');
        boolean isFirstParameter = true;
        for (var typeParameter : typeParameters) {
            if (!isFirstParameter) {
                result.append(", ");
            }
            result.append(typeParameter.getName());
            boolean isFirstBound = true;
            for (var bound : typeParameter.getBounds()) {
                if (bound.getTypeName().equals("java.lang.Object")) {
                    continue;
                }
                if (!isFirstBound) {
                    result.append(" & ");
                } else {
                    result.append(" extends ");
                    isFirstBound = false;
                }
                result.append(bound.getTypeName());
            }
            isFirstParameter = false;
        }
        result.append('>');
        return result;
    }

    private static StringBuilder printModifiers(int modifiers) {
        var result = new StringBuilder(Modifier.toString(modifiers));
        if (result.length() > 0) {
            result.append(' ');
        }
        return result;
    }

    private static String printIndentation(int indentation) {
        return " ".repeat(4 * indentation);
    }

}
