# Team Project

## Schedule / plan 

- Nov 10-14 - PRs + something to show (one PR each member, runnable code, be able to make API calls)
    - plan is to look at each UseCase and decide how to implement each
- Nov 17-21 - one unit test per Interactor + close-to-complete prototype of program to get final feedback
- Nov 24-25 - tests achieving 100% code coverage
- Dec 1 - Presentation 

## Repository Layout

### 1. `entities/` (Enterprise Business Rules)
*   **Purpose:** Contains the core business objects and data structures.
*   **Examples:** `GameRecord`, `GameSession`, `Difficulty`, `Player`.
*   **Dependencies:** Independent of all other layers.

### 2. `usecases/` (Application Business Rules)
*   **Purpose:** Contains the application-specific business logic. These classes orchestrate the flow of data between the UI and Entities.
*   **Examples:** `GameplayUseCase`, `RetrieveGamesUseCase`.
*   **Dependencies:** Depends only on `entities` and interfaces defined in `interface_adapters`.

### 3. `interface_adapters/` (Interface Adapters)
*   **Purpose:** Converts data between the format most convenient for the Use Cases and the format most convenient for external agencies (UI, DB, Web).
*   **Sub-folders:**
    *   **`database/` (Interfaces):** Defines the contracts (interfaces) for data storage that Use Cases rely on.
    *   **`data_access/` (Implementations):** The actual code that implements the interfaces to store data (e.g., to memory, CSV, or SQL).
    *   **`api/`:** Handles external API calls (e.g., HuggingFace).
    *   **`controllers/`:** Adapters for the UI that serve as the entry point for user actions. When a user interacts with the View (e.g., clicks a button), the Controller takes that input and triggers the appropriate Use Case.
    *   **`presenters/`:** Adapters for the UI that handle the output. After the Use Case finishes processing, it passes data to the Presenter, which formats it for the UI (often updating a ViewModel) so the View can display the result.

### 4. `gui/` (Frameworks & Drivers)
*   **Purpose:** Contains all Java Swing code and the main application entry point.
*   **Examples:** `Application.java`, `Game.java`, `MainMenu.java`.

### Clarification: Data Access vs. Database
This structure separates the *definition* of data operations from their *implementation*:

*   **Use Case:** "I need to `save(game)`." (It calls the **Interface** in `interface_adapters/database`).
*   **The Data Access Object says:** "Okay, I will `save(game)` by writing to `games.csv`." (It **Implements** the interface in `interface_adapters/data_access`).

## Responsibilities

### Zac: Main menu + gameplay 
- gameplayUseCase
- recordGameUseCase 
- retrieveSettingsUseCase
- randomPromptUseCase(Difficulty difficulty, UserSettingsDataBase??? idk)

- APICaller 
    - String APIToken
    + call(APIToken apiToken, ???) (returns the json / structured data from the call) 

    - HuggingFaceAPICaller
        - String APIToken

        + call()

### Ziyi: Advanced drawing features

- GameRecord
    - String imagePath (where the image is stored in our database)
    - DateTime date (i think this type exists)
    - bool hasWon (win/lose)
    - double timeTaken
    - double timeLimit
    - Difficulty difficulty 
    - String prompt

    + getImagePath()
    + getDate()
    + getHasWon()
    + getTimeTaken()
    + getTimeLimit()
    + getDifficulty()
    + getPrompt()

### David: gallery window 
- retrieveGamesUseCase

- DataBase
    - GameDataBase
    - UserSettingsDataBase


### OG: Settings
- editSettingsUseCase

- Difficulty
    - String difficultyName
    - String[] prompts

### Laney: Saving pictures + new picture window
- deleteGameUseCase
- saveImageToUserUseCase


UML / project layout: 
- main gameplay loop 
- gallery 
    - new window + download 
- settings



### Overall 
- Application (implement last)
- Game (Zac + Ziyi)
- MainMenu (Zac)
- Gallery (David)
- PictureWindow (Laney)
- Settings (OG)




## NOTES ON CLEAN ARCHITECTURE
base - all dependencies should point inward 
![[Pasted image 20251117140434.png]]
isolating what the system does 

entity - GameRecord
use case - retrieveGame
interface adapter - DataBase, APICaller
- .save, .retrieve, . find, .search 

```java
interface DataBase {
	save() {}
	retrieve() {}
}
```

interface - GameDataBase, GUI, APIs,
- .save {csv.parse}
- java.swing
- HuggingFaceAPIHandler

```java
class GameDataBase implements DataBase {

}
```

- **entities** hold core business logic / rules (Ride, Passengers, Drivers), doesn't care about where data comes from or how it's stored 
Ride - status, rideId, assignDriver 
- always takes in rideIds and passengerIds, not the actual objects. 

- **use cases** orchestrate entities - RequestRide matches passenger with available driver 

RequestRide - only dependent on the interface adapter rideRepository (independent of how data is stored)
- makes a new Ride object and saves it in the repository

- **interface adapters** are translators between core logic and APIs / databases - REST controller takes an incoming hTTP request, calls usecase, then formats response back to client 

RideRepository - an **interface** that has `save`, `findById`, `update`. customizable depending on the db. 

Controller - **connects HTML requests with usecases** - gets POST request and triggers usecases 

- **interfaces** - UI, DB, APIs 

Repository - implements RideRepository

when http req comes in, 
- controller triggers usecase 
- usecase interacts with repo and returns response 

```python
TextField(label: "prompt", text: "banana")

later...

import Controller 

info = controller.get(game)
TextField(label: "prompt", text: info.prompt)
```
