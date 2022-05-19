# SonarCloud

SonarClouddal végeztünk statikus analízist, az így talált code smelleket nagyrészt kijavítottuk, például az általános kivételeket specifikusabbakra cseréltük, megváltoztattuk a logolás módját és a biztonsági szempontból szenzitívnek jelölt kódrészeket is lecseréltük.

Statikus analízis problémák: Exceptions<br/>
Az eredeti projektben mindenhol csak general Exceptionöket dobtak és kaptak el, nagyon átláthatatlanná téve, hogy milyen hibák keletkezhetnek a kódban. Ezeket a statikusan analizáló eszközünk észrevette.
Átírtuk a kódot úgy, hogy mindenhol csak a kellő kivételeket kapjuk el, így a konzolban hibába futásnál is sokkal több információt kap a felhasználó, amellett, hogy a kód is sokkal tisztább<br/><br/>
Statikus analízis problémák: Logging <br/> 
A statikus analízis kiírta, hogy nem használ a program modern logging programokat, ezért a Maven keretrendszerrel felvettünk egy új logging toolt a projekthez, és minden eredeti információt, ami a std::out-ra ment, azt a használt logutil-lal íratjuk ki.
<br/>
