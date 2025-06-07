package fag.ml.security;


import fag.ware.client.tracker.impl.ModuleTracker;

import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * @author graphicalinterface
 * @see ModuleTracker#initialize()
 */
@SuppressWarnings("ALL")
public class JvmArgsChecker {
    private static String[] blockedArgs = {
            "-Xdebug", "-Xrunjdwp", "-agentlib:jdwp", "-javaagent",
            "-XX:+AllowAttachSelf", "-XX:+EnableDynamicAgentLoading",
            "-noverify", "-Xbootclasspath", "--add-opens", "--enable-preview",
            "-Xverify"
    };
    static List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();

    public static void force()  {
        for (String arg : args) {
            for (String sus : blockedArgs) {
                if (arg.contains(sus)) {
                    while (true);
                }
            }
        }
    }
}
