Maven keretrendszer beüzemelése:<br />
>Az eredeti projektben egy lib nevű mappában voltak jar file-ként dependencyk. Jónak láttuk a maven keretrendzsert ráhúzni a projektre a flexibilisebb dependency kezeléshez.
Ez nagyon jól jött a későbbiekben is, amikor logging utilt, vagy CI-t tettünk a projekthez, mivel elég volt a pom.xml-t szerkeszteni.
Volt kettő eredeti dependency a lib mappába, ami nem vagy rosszul volt fent mavenen.<br />
  Ezeket kezdetben statikusan belinkeltük majd később megoldottuk, hogy a keretrendszer befordítsa a target mappába, így kapva egy sokkal modernebb megoldást az eredeti mappastruktúra helyett.
