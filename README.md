# Team Project

## Schedule / plan 

- Nov 10-14 - PRs + something to show (one PR each member, runnable code, be able to make API calls)
    - plan is to look at each UseCase and decide how to implement each
- Nov 17-21 - one unit test per Interactor + close-to-complete prototype of program to get final feedback
- Nov 24-25 - tests achieving 100% code coverage
- Dec 1 - Presentation 

## Repository Layout
If you're confused by any of this, check out the ca-lab repository at https://github.com/ponsiclius/ca-lab/tree/main/src. The layout is copied from there. 

### 1. `entities/` (Enterprise Business Rules)
*   **Purpose:** Contains the core business objects and data structures.
*   **Examples:** `GameRecord`, `GameSession`, `Difficulty`.
*   **Dependencies:** Independent of all other layers.

### 2. `usecases/` (Application Business Rules)
*   **Purpose:** Contains the application-specific business logic. These classes orchestrate the flow of data between the UI and Entities.
*   **Examples:** `GameplayUseCase`, `RetrieveGamesUseCase`.
*   **Dependencies:** Depends only on `entities` and interfaces defined in `interface_adapters`.

### 3. `interface_adapters/` (Interface Adapters)
**Purpose:** Converts data between the format most convenient for the Use Cases and the format most convenient for external agencies (UI, DB, Web).

-   `ViewModel.java`: A generic class that holds the `State` for a view. Our specific ViewModels will extend this.
-   `ViewManagerModel.java`: Manages which view is currently active on the screen.

The sub-folders (`game`, `menu`, `gallery`, etc.) correspond to the features each of us is working on.

#### `Controller` (e.g., `GameController`)
https://github.com/ponsiclius/ca-lab/blob/main/src/main/java/interface_adapter/login/LoginController.java
*   **Role:** The "Input Handler".
*   **Your Task:** When a user interacts with your `gui` view, the view calls a method in this Controller. Your controller's only job is to package the user's input and send it to the correct `UseCase` for processing.

#### `Presenter` (e.g., `GamePresenter`)
https://github.com/ponsiclius/ca-lab/blob/main/src/main/java/interface_adapter/login/LoginPresenter.java
*   **Role:** The "Output Handler".
*   **Your Task:** After a `UseCase` finishes, it calls your Presenter with the results. Your presenter's job is to take that data, format it for display by updating the `ViewModel`. **It should never talk to the `gui` view directly.**

#### `State` (e.g., `GameState`)
https://github.com/ponsiclius/ca-lab/blob/main/src/main/java/interface_adapter/login/LoginState.java
*   **Role:** A simple data-holding class.
*   **Your Task:** This class holds all the information your `gui` view needs to display itself (e.g., `currentPrompt`, `timeRemaining`, `errorMessage`). It should only have fields, getters, and setters.

#### `ViewModel` (e.g., `GameViewModel`)
https://github.com/ponsiclius/ca-lab/blob/main/src/main/java/interface_adapter/login/LoginViewModel.java
*   **Role:** The "State Manager" for a view.
*   **Your Task:** This class holds your `State` object. Your `gui` view will "listen" to this ViewModel. When your Presenter updates the state, the ViewModel notifies the view, which then redraws itself with the new information.

### 4. `gui/` (Frameworks & Drivers)
*   **Purpose:** Contains all Java Swing code and the main application entry point.
*   **Examples:** `Application.java`, `Game.java`, `MainMenu.java`.

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
