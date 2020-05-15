# Több alkalmazásból álló rendszerek

## Ajánlott irodalom

* Domain-Driven Design Distilled

## Tematika

* Igény az elosztott rendszerekre
* Történeti háttér (ad-hoc integrációk, fájl és adatbázis alapú integráció, EAI, SOA, microservices)
* EAI, p2p, hub-and-spoke
* SOA, BUS, business rule engine, orchestrator (BPEL)
* BPMN
	* https://www.jtechlog.hu/2014/07/25/pehelysulyu-workflow-activitivel.html
	* Egy előadásom az Activity-ről: https://www.youtube.com/watch?v=66FavTUB7AM
* Microservices
	* Martin Fowler definíció: https://martinfowler.com/articles/microservices.html
	* Red Hat bevezető videója: https://www.youtube.com/watch?v=WK3tbQCzVxg
	* Előfeltételek:
		* DDD
		* DevOps
		* Self-service, on-demand, elastic infrastructure as code (Immutable image), Kubernetes
		* Automatizáció
		* CI/CD
		* Deployment pipeline
		* Monitoring
		* Rollback, visszaállás
		* Blue/green deployment, canary testing
* Mono repo - multi repo

## Twelve-factor app

* [Twelve-factor app](https://12factor.net/) egy manifesztó, metodológia felhőbe telepíthető alkalmazások fejlesztésére
* Heroku platform fejlesztőinek ajánlása
* Előtérben a cloud, PaaS, continuous deployment
* PaaS: elrejti az infrastruktúra részleteit
    * Pl. Google App Engine, Redhat Open Shift, Pivotal Cloud Foundry, Heroku, AppHarbor, Amazon AWS, stb.

## Cloud native

* Jelző olyan szervezetekre, melyek képesek az automatizálás előnyeit kihasználva gyorsabban megbízható és skálázható alkalmazásokat szállítani
* Pivotal, többek között a Spring mögött álló cég
* Előtérben a continuous delivery, DevOps, microservices
* Alkalmazás jellemzői
    * PaaS-on fut (cloud)
    * Elastic: automatikus horizontális skálázódás

## Twelve-factor app ajánlások

* Verziókezelés: "One codebase tracked in revision control, many deploys"
* Függőségek: "Explicitly declare and isolate dependencies"
* Konfiguráció: "Store config in the environment"
* Háttérszolgáltatások: "Treat backing services as attached resources"
* Build, release, futtatás: "Strictly separate build and run stages"
* Folyamatok: "Execute the app as one or more stateless processes"
* Port hozzárendelés: "Export services via port binding"
* Párhuzamosság: "Scale out via the process model"
* Disposability: "Maximize robustness with fast startup and graceful shutdown"
* Éles és fejlesztői környezet hasonlósága: "Keep development, staging, and production as similar as possible"
* Naplózás: "Treat logs as event streams"
* Felügyeleti folyamatok: "Run admin/management tasks as one-off processes"

## Beyond the Twelve-Factor App

* One Codebase, One Application
* API first
* Dependency Management
* Design, Build, Release, Run
* Configuration, Credentials and Code
* Logs
* Disposability
* Backing services
* Environment Parity
* Administrative Processes
* Port Binding
* Stateless Processes
* Concurrency
* Telemetry
* Authentication and Authorization
