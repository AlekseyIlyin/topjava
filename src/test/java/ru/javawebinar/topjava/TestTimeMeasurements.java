package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.TimeUnit;

public class TestTimeMeasurements implements TestRule {

    public static final String FORMAT_STRING = "%-23s %5d %s";

    private StringBuilder sb;

    public TestTimeMeasurements(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTime = System.nanoTime();
                base.evaluate();
                sb.append(
                        String.format(FORMAT_STRING,
                                description.getMethodName(),
                                TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS),
                                "milliseconds"));
                sb.append("\n");
            }
        };
    }
}
