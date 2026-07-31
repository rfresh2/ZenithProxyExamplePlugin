package graalvm.org.example;

import graalvm.com.zenith.AbstractGraalVMReflectionFeature;
import org.example.BuildConstants;

import java.util.List;

public class ExamplePluginReflectionFeature extends AbstractGraalVMReflectionFeature {

    public ExamplePluginReflectionFeature() {
        super(List.of(
            BuildConstants.MAVEN_GROUP
        ));
    }
}
