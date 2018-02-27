package com.piotrwalkusz.scriptrepository.util;

public class ExceptionUtils {

    @FunctionalInterface
    public interface FunctionWithCheckedException {

        void run() throws Exception;
    }

    @FunctionalInterface
    public interface SupplierWithCheckedException<T> {

        T run() throws Exception;
    }

    public static void wrapCheckedException(FunctionWithCheckedException func) {
        try {
            func.run();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T wrapCheckedException(SupplierWithCheckedException<T> func) {
        try {
            return func.run();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
