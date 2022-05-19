# Maven és GitHub Actions

Maven keretrendszer beüzemelése:

Az eredeti projektben egy lib nevű mappában voltak jar file-ként függőségek. Jónak láttuk a Maven keretrendszert ráhúzni a projektre a flexibilisebb dependency kezeléshez. Ez nagyon jól jött a későbbiekben is, amikor logging utilt, vagy CI-t tettünk a projekthez, mivel elég volt a pom.xml-t szerkeszteni. Volt kettő eredeti dependency a lib mappában, ami nem, vagy rosszul volt fent Mavenen.
Ezeket kezdetben statikusan belinkeltük, majd később megoldottuk, hogy a keretrendszer befordítsa a target mappába, így kapva egy sokkal modernebb megoldást az eredeti mappastruktúra helyett.

A fordításhoz Mavent használtunk, a folytonos integrációt pedig GitHub Actions segítségével valósítottuk meg: két jobot definiáltunk, a "build" job lefordítja a programot Mavennel és SonarClouddal statikus analízist is végeztet, a "run_tests" job pedig egységteszteket és stressztesztet futtat.
