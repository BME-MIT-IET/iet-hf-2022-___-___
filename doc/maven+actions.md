# Maven és GitHub Actions

A fordításhoz Mavent használtunk, a folytonos integrációt pedig GitHub Actions segítségével valósítottuk meg: két jobot definiáltunk, a "build" job lefordítja a programot Mavennel és SonarClouddal statikus analízist is végeztet, a "run_tests" job pedig egységteszteket és stressztesztet futtat.