package com.lk.cloud.role.karate;

import com.intuit.karate.junit5.Karate;

public class ExampleControllerRunner {
    
    @Karate.Test
    Karate testAll() {
        return Karate.run().relativeTo(getClass());
    }
}
