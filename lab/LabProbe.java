import java.util.Arrays;
import java.util.Base64;

/** Local, bounded decoder observation for the Java4Shell educational lab. */
public final class LabProbe {
    private static final int EXPECTED_OUTPUT_BYTES = 48;
    private static final byte SENTINEL = (byte) 0x5a;

    public static void main(String[] args) {
        System.out.println("vendor=" + System.getProperty("java.vendor", "unknown"));
        System.out.println("runtime=" + System.getProperty("java.runtime.version", "unknown"));
        System.out.println("os.arch=" + System.getProperty("os.arch", "unknown"));
        System.out.println("UseBASE64Intrinsics=" +
                java.lang.management.ManagementFactory.getRuntimeMXBean()
                        .getInputArguments().stream()
                        .filter(a -> a.contains("UseBASE64Intrinsics"))
                        .findFirst().orElse("default / not explicitly set"));

        byte[] input = new byte[65];
        Arrays.fill(input, 0, 64, (byte) 'A');
        input[64] = (byte) '=';
        byte[] output = new byte[128];
        Arrays.fill(output, SENTINEL);

        try {
            Base64.getDecoder().decode(input, output);
            System.out.println("result=INPUT_ACCEPTED");
        } catch (IllegalArgumentException expected) {
            System.out.println("result=INPUT_REJECTED");
        } catch (Throwable error) {
            System.out.println("result=INCONCLUSIVE error=" + error.getClass().getName());
            System.exit(2);
        }

        int changedPastExpected = changed(output, EXPECTED_OUTPUT_BYTES, output.length);
        System.out.println("bytes_changed_past_expected_output=" + changedPastExpected);
        if (changedPastExpected > 0) {
            System.out.println("assessment=REPRODUCED_OUT_OF_BOUNDS_WRITE");
            System.exit(1);
        }
        System.out.println("assessment=NOT_REPRODUCED_IN_THIS_RUN");
        System.out.println("note=This result does not prove the JVM or application is safe.");
    }

    private static int changed(byte[] bytes, int start, int end) {
        int count = 0;
        for (int i = start; i < end; i++) {
            if (bytes[i] != SENTINEL) count++;
        }
        return count;
    }
}
