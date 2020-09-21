package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public final class Reflection {
    private Reflection() {
    }

    public static String getInfoForClass(String fullClassName, boolean onlyPublic) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class infoClass = Class.forName(fullClassName);
            Class superInfoClass = infoClass.getSuperclass();

            stringBuilder.append(Modifier.toString(infoClass.getModifiers()));
            if (stringBuilder.length() > 0) stringBuilder.append(' ');
            stringBuilder.append(fullClassName);
            if (superInfoClass != null) stringBuilder.append(" extends ")
                    .append(superInfoClass.getName());
            stringBuilder.append("\n{\n");

            stringBuilder.append("Fields:\n");
            stringBuilder.append(getInfoForFields(infoClass, onlyPublic));

            stringBuilder.append("Constructors:\n");
            stringBuilder.append(getInfoForConstuctors(infoClass, onlyPublic));

            stringBuilder.append("Methods:\n");
            stringBuilder.append(getInfoForMethods(infoClass, onlyPublic));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            stringBuilder.append("Not founded class for classname: ")
                    .append(fullClassName);
        }
        return stringBuilder.toString();
    }

    public static String getInfoForFields(Class infoClass, boolean onlyPublic) {
        StringBuilder stringBuilder = new StringBuilder();
        Field[] fields;

        if (onlyPublic)
            fields = infoClass.getFields();
        else
            fields = infoClass.getDeclaredFields();

        for (Field f : fields) {
            for(Annotation annotation: f.getAnnotations())
            {
                stringBuilder.append('\t')
                            .append(annotation.toString())
                            .append('\n');
            }
            stringBuilder.append('\t');
            stringBuilder.append(Modifier.toString(f.getModifiers()));
            if (stringBuilder.length() > 1) stringBuilder.append(' ');

            stringBuilder.append(f.getType().getName())
                    .append(' ');
            stringBuilder.append(f.getName())
                    .append('\n');
        }

        return stringBuilder.toString();
    }

    public static String getInfoForConstuctors(Class infoClass, boolean onlyPublic) {
        StringBuilder stringBuilder = new StringBuilder();
        Constructor[] constructors;

        if (onlyPublic)
            constructors = infoClass.getConstructors();
        else
            constructors = infoClass.getDeclaredConstructors();

        for (Constructor c : constructors) {
            appendAnnotation(stringBuilder, c.getAnnotations(), c.getModifiers());

            writeParameters(stringBuilder, c.getName(), c.getParameterTypes());
        }
        return stringBuilder.toString();
    }

    public static String getInfoForMethods(Class infoClass, boolean onlyPublic) {
        StringBuilder stringBuilder = new StringBuilder();
        Method[] methods;

        if (onlyPublic)
            methods = infoClass.getMethods();
        else
            methods = infoClass.getDeclaredMethods();

        for (Method m : methods) {
            appendAnnotation(stringBuilder, m.getAnnotations(), m.getModifiers());

            stringBuilder.append(m.getReturnType().getName())
                    .append(' ');

            writeParameters(stringBuilder, m.getName(), m.getParameterTypes());
        }
        return stringBuilder.toString();
    }

    private static void appendAnnotation(StringBuilder stringBuilder, Annotation[] annotations, int modifiers) {
        for(Annotation annotation: annotations)
        {
            stringBuilder.append('\t')
                    .append(annotation.toString())
                    .append('\n');
        }
        stringBuilder.append('\t');

        String modifier = Modifier.toString(modifiers);
        stringBuilder.append(modifier);
        if (modifier.length() > 0) stringBuilder.append(' ');
    }

    private static void writeParameters(StringBuilder stringBuilder, String className, Class[] parameterType) {
        stringBuilder.append(className).append('(');

        for (int i = 0; i < parameterType.length; ++i) {
            if (i != 0) stringBuilder.append(", ");
            stringBuilder.append(parameterType[i].getName());
        }

        stringBuilder.append(");\n");
    }
}
