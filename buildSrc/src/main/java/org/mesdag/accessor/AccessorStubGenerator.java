package org.mesdag.accessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class AccessorStubGenerator {
    private AccessorStubGenerator() {}

    private static final Gson GSON = new Gson();
    private static final String CONFIG_PATH = "META-INF/accessor_interfaces.json";

    public static Map<String, List<String>> collectConfigs(List<File> resourceDirs, List<File> classpath)
            throws IOException {
        Map<String, List<String>> merged = new LinkedHashMap<>();

        for (File dir : resourceDirs) {
            File jsonFile = new File(dir, CONFIG_PATH);
            if (jsonFile.isFile()) {
                mergeConfig(merged, parseJson(Files.readString(jsonFile.toPath())));
            }
        }

        for (File entry : classpath) {
            if (entry.isDirectory()) {
                File jsonFile = new File(entry, CONFIG_PATH);
                if (jsonFile.isFile()) {
                    mergeConfig(merged, parseJson(Files.readString(jsonFile.toPath())));
                }
            } else if (entry.getName().endsWith(".jar")) {
                try (JarFile jar = new JarFile(entry)) {
                    JarEntry je = jar.getJarEntry(CONFIG_PATH);
                    if (je != null) {
                        mergeConfig(merged, parseJson(
                                new InputStreamReader(jar.getInputStream(je), StandardCharsets.UTF_8)));
                    }
                }
            }
        }
        return merged;
    }

    public static void generate(Map<String, List<String>> config, List<File> classpath, File outputDir)
            throws IOException {
        for (Map.Entry<String, List<String>> entry : config.entrySet()) {
            String targetFqn = entry.getKey();
            List<String> ifaceFqns = entry.getValue();

            String targetInternal = targetFqn.replace('.', '/');
            String classFile = targetInternal + ".class";

            byte[] original = findClass(classFile, classpath);
            if (original == null) {
                System.err.println("[AccessorStub] WARNING: target class not found: " + targetFqn);
                continue;
            }

            byte[] modified = original;
            boolean changed = false;
            for (String ifaceFqn : ifaceFqns) {
                String ifaceInternal = ifaceFqn.replace('.', '/');
                byte[] next = addInterface(modified, ifaceInternal);
                if (next != modified) {
                    modified = next;
                    changed = true;
                }
            }

            if (changed) {
                File outFile = new File(outputDir, classFile);
                outFile.getParentFile().mkdirs();
                Files.write(outFile.toPath(), modified);

                String innerPrefix = targetInternal + "$";
                copyInnerClasses(innerPrefix, classpath, outputDir);

                List<String> ifaces = getInterfaces(modified);
                System.out.println("[AccessorStub] " + targetFqn + " implements " + ifaces);
            }
        }
    }

    private static Map<String, List<String>> parseJson(String content) {
        return GSON.fromJson(content,
                new TypeToken<Map<String, List<String>>>() {}.getType());
    }

    private static Map<String, List<String>> parseJson(InputStreamReader reader) {
        return GSON.fromJson(reader,
                new TypeToken<Map<String, List<String>>>() {}.getType());
    }

    private static void mergeConfig(Map<String, List<String>> into, Map<String, List<String>> from) {
        for (Map.Entry<String, List<String>> e : from.entrySet()) {
            into.computeIfAbsent(e.getKey(), k -> new ArrayList<>()).addAll(e.getValue());
        }
    }

    private static byte[] findClass(String classFile, List<File> classpath) {
        for (File entry : classpath) {
            try {
                if (entry.isDirectory()) {
                    File f = new File(entry, classFile);
                    if (f.isFile()) return Files.readAllBytes(f.toPath());
                } else if (entry.getName().endsWith(".jar")) {
                    try (JarFile jar = new JarFile(entry)) {
                        JarEntry je = jar.getJarEntry(classFile);
                        if (je != null) return jar.getInputStream(je).readAllBytes();
                    }
                }
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    private static void copyInnerClasses(String innerPrefix, List<File> classpath, File outputDir)
            throws IOException {
        for (File entry : classpath) {
            if (entry.isDirectory()) {
                File pkgDir = new File(entry, innerPrefix.substring(0, innerPrefix.lastIndexOf('/')));
                String prefix = innerPrefix.substring(innerPrefix.lastIndexOf('/') + 1);
                File[] files = pkgDir.listFiles((d, n) -> n.startsWith(prefix) && n.endsWith(".class"));
                if (files != null) {
                    for (File f : files) {
                        Files.copy(f.toPath(),
                                new File(outputDir, innerPrefix.substring(0, innerPrefix.lastIndexOf('/') + 1) + f.getName()).toPath());
                    }
                }
            } else if (entry.getName().endsWith(".jar")) {
                try (JarFile jar = new JarFile(entry)) {
                    Enumeration<JarEntry> entries = jar.entries();
                    boolean found = false;
                    while (entries.hasMoreElements()) {
                        JarEntry je = entries.nextElement();
                        String name = je.getName();
                        if (name.startsWith(innerPrefix) && name.endsWith(".class")) {
                            File dest = new File(outputDir, name);
                            dest.getParentFile().mkdirs();
                            Files.write(dest.toPath(), jar.getInputStream(je).readAllBytes());
                            found = true;
                        }
                    }
                    if (found) return;
                }
            }
        }
    }

    private static List<String> getInterfaces(byte[] classBytes) {
        ClassReader cr = new ClassReader(classBytes);
        List<String> result = new ArrayList<>();
        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public void visit(int version, int access, String name,
                              String sig, String superName, String[] interfaces) {
                for (String itf : interfaces) {
                    result.add(itf.replace('/', '.'));
                }
            }
        }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        return result;
    }

    static byte[] addInterface(byte[] original, String ifaceInternal) {
        ClassReader cr = new ClassReader(original);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        boolean[] found = {false};
        cr.accept(new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public void visit(int version, int access, String name,
                              String sig, String superName, String[] interfaces) {
                for (String itf : interfaces) {
                    if (itf.equals(ifaceInternal)) {
                        found[0] = true;
                        break;
                    }
                }
                if (!found[0]) {
                    String[] newIfaces = Arrays.copyOf(interfaces, interfaces.length + 1);
                    newIfaces[interfaces.length] = ifaceInternal;
                    super.visit(version, access | Opcodes.ACC_ABSTRACT,
                            name, null, superName, newIfaces);
                    return;
                }
                super.visit(version, access, name, sig, superName, interfaces);
            }
        }, 0);
        return found[0] ? original : cw.toByteArray();
    }
}
