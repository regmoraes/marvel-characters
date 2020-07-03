# Marvel Characters

An app where users can interact with the Marvel Comics API to fetch characters data.

## Features

- The user should be able see a list of characters in alphabetical order  
  - The list should be fetched as the users scrolls the list
  - The list can be filtered once fetched
- The user can search characters by name :construction:   
- Tapping on a character should show a detailed view of it 
  - This view should also show a list of comics and series where the character appears   
- The user can (un)favorite a character
  - The character basic info is persisted locally and can be viewed offline
  - All comics and series are persisted :construction: 
  



## Running

| ⚠️   Warning                                                                                                                 |
| :---------------------------------------------------------------------------|
| You need to provide your `Private and Public Marvel API Keys` in the root `local.properties`. |

## Architecture

The code was made to be as simple and straightforward as possible, but not abdicating of some good patterns and practices.
The project doesn't use an specific architecture pattern, instead it combines good practices and elements from multiples 
architectures, methodologies and concepts. Some of them come from :
- [Domain-Driven Design](https://www.amazon.com.br/Domain-Driven-Design-Tackling-Complexity-Software/dp/0321125215)
- [OnionArchitecture](http://jeffreypalermo.com/blog/the-onion-architecture-part-1/) 
- [CleanArchitecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) 
- [Functional Programming](https://en.wikipedia.org/wiki/Functional_programming)
- [Model-View-ViewModel](https://docs.microsoft.com/en-us/archive/msdn-magazine/2009/february/patterns-wpf-apps-with-the-model-view-viewmodel-design-pattern)
- [Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html)

The project was divided into four packages:

- **application**: Contains the application use cases (analogous to DDD's Application Services and Clean Architecture's Use Case) and events. 
- **model**: Anemic Domain Models that represents the domain entities.
- **infrastructure**: Repositories definition, Database and Network implementations, DI modules, and other low layer level implementations.
- **presentation**: Activities, Fragments, ViewModels, view DTOs, that is, everything related to views.

The overall app architecture is as follows

![Marvel Characters](https://user-images.githubusercontent.com/4440882/86497318-9fdbad80-bd57-11ea-9504-4648354c9e0c.png)

### Notes

1. The app is heavily based on application events, so every domain model from bottom layers is sent to top wrapped in a event class.
2. Character model is used as an aggregate root and because of that, almost all repositories and use cases manipulates Character classes.
4. Repository *always* exposes domain models.
3. Due to a necessity to sync local favorites data with API provided characters, there's a dedicated FavoriteStatusSynchronizer that handles 
this synchronization in repository level.
5. View DTOs were created to make it easier to send data between screens and avoid scoped instances and side-effects . 

## Tests
Tests were made using the Behavior-driven Development approach to ensure the use cases were working as expected.

:construction: Instrumentation tests 

## Design Rationale

As good as showing why something was made, it's important to show why something was **not** made.

**Why not Clean Architecture?**

Even not explicitly using Clean Architecture this project uses (almost all) *basic* concepts 
that originated the Clean Architecture, with the benefit of not over-engineering the app with 
unnecessary elements/layers and mappings. In fact most concepts were extracted from 
Domain-Driven Design which is also one of the basis of Clean Architecture.

**Why not mutiple modules?**

Modules are useful when you need to expose/export those module independently as let's say a library. 
Breaking an app in multiple modules that only works together just generates a "distribute monolith" 
that is difficult to mantain and navigate. A beter approach is to create a well organized package structure 
that is dividide into "contexts" that can be exported independently if needed.

**Why not more Interfaces to provide a "better" Dependency Inversion?**

Agressively using Interfaces to provide Dependency Inversion can cause an over-abstraction problem where tracking data flow becomes 
exhausting. Interfaces should be used wisely and declaring interfaces is not an excuse for better testing since classes can be mocked.

**Why not Data Sources with Repository**

The DAO and RestService interfaces are already a Data Source abstraction so no need create 
another abstraction on top of them. The Repository Mediator is responsible to manage their 
communication and data. 

**Why not Dagger?**

Dagger is unnecessarily complex (even so that [another library](https://dagger.dev/hilt/) was created to make it easier) 
Instead of using it, we opt for Koin, a much simpler DI framework with good Kotlin DSL.

**Why not RxJava?**

The best benefit of RxJava is to have a cleaner way to offload tasks to background. 
Instead of using it, we opt for Kotlin coroutines with Flow which is more lightweight, 
is more easier to understand and also provides a good transformation/map API.
