function fn() {
    // run with:  ./gradlew integrationTest "-Purl=http://domain:port/example-demo/app"
    var config = {
        baseUrl : karate.properties['url'] || 'http://localhost:8082/example-demo/app'
    };

    return config;
}