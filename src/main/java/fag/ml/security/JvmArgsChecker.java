package fag.ml.security;

import fag.ware.client.tracker.impl.ModuleTracker;
import fag.ware.client.tracker.impl.ScreenTracker;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Set;

/**
 * @author graphicalinterface
 * @see ScreenTracker#ScreenTracker()
 */
public final class JvmArgsChecker {
    private static final Set<String> BLOCKED_ARGS = Set.of(
            "-Xrunjdwp", "-agentlib:jdwp", "-javaagent",
            "-XX:+AllowAttachSelf", "-XX:+EnableDynamicAgentLoading",
            "-noverify", "-Xbootclasspath", "--add-opens", "--enable-preview",
            "-Xverify");

    public static void force() {
        List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
        for (String arg : args) {
            if (BLOCKED_ARGS.stream().anyMatch(arg::contains)) {
                while (true);
            }
        }
    }
}
