package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Date;
import java.util.List;

public class TestTimeMeasurements implements TestRule {

    private List<String> logsByTests;

    public TestTimeMeasurements(List<String> logsByTests) {
        this.logsByTests = logsByTests;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTime = new Date().getTime();
                base.evaluate();
                logsByTests.add(String.format("Test: %s evaluate on %d milliseconds", description.getMethodName(), (new Date().getTime()) - startTime));
            }
        };
    }
}
