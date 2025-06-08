package fag.ml.security;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

import java.util.ArrayList;

/**
 * Just checks some shit and returns a checksum
 * @author marie
 * @see fag.ware.client.tracker.impl.AuthTracker#authenticate(String, String)
 */
public class IntegrityCheck
{
    public static long check()
    {
        // name explains itself
        String[] classesToCheck = {
                "sun.management.VMManagementImpl", // jvm args
                "sun.management.RuntimeImpl",      // jvm args 2
                "java.net.URL",                    // often hooked inside a custom jdk
        };

        // I don't know what to call this tbh, this is just
        // the output of some math using the code below
        long hash = 0;

        for (String s : classesToCheck) {
            try
            {
                ClassReader cr = new ClassReader(s);
                ClassNode cn = new ClassNode();

                cr.accept(cn, 0);

                // I just wanted some access to class data and
                // this was the first thing that came to my mind
                hash |= cn.access;

                for (MethodNode method : cn.methods) {
                    // quick mix of name + desc
                    hash += method.name.hashCode() ^ method.desc.hashCode();

                    // if (for example) the native attribute has
                    // been removed as a result of hooking a method
                    hash ^= method.access;

                    for (AbstractInsnNode instruction : method.instructions) {
                        hash += instruction.getOpcode(); // yes
                    }

                    for (TryCatchBlockNode tryCatchBlock : method.tryCatchBlocks) {
                        int startOffset = method.instructions.indexOf(tryCatchBlock.start);
                        int handlerOffset = method.instructions.indexOf(tryCatchBlock.handler);
                        int endOffset = method.instructions.indexOf(tryCatchBlock.end);

                        hash -= ~(startOffset ^ handlerOffset ^ endOffset);
                    }
                }
            }
            catch (Throwable t)
            {
                throw new RuntimeException(t);
            }
        }

        return hash;
    }
}
