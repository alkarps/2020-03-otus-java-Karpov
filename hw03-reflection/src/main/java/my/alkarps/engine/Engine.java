package my.alkarps.engine;

import my.alkarps.engine.model.ClassDetails;
import my.alkarps.engine.model.Statistics;

public class Engine {

    private final Analyzer analyzer;
    private final Runner runner;

    public Engine() {
        this.runner = new Runner();
        this.analyzer = new Analyzer();
    }

    public Engine(Analyzer analyzer, Runner runner) {
        this.analyzer = analyzer;
        this.runner = runner;
    }

    public void run(Class<?> testClass) {
        ClassDetails classDetails = analyzer.analyze(testClass);
        Statistics statistics = runner.run(classDetails);
        System.out.println(statistics);
    }
}
