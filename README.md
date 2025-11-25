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


## Clean Architecture Reference 

### 1. Layers
*   **Frameworks & Drivers (Outer):**
    *   **View:** UI that displays `ViewModel` state and delegates user actions to the `Controller`.
    *   **Data Access:** Implements interfaces from the Use Case layer to persist data (DB, files).
*   **Interface Adapters:**
    *   **Controller:** Packages raw user input into `InputData` and invokes the `Use Case`.
    *   **Presenter:** Formats `OutputData` from the Use Case and updates the `ViewModel`.
    *   **ViewModel:** Holds the UI state. The View observes this for updates.
*   **Use Cases (Application Rules):**
    *   **Interactor:** Orchestration logic. Implements `InputBoundary`. Uses `Entities` and `DataAccessInterface`.
    *   **Boundaries:** `InputBoundary` (called by Controller), `OutputBoundary` (implemented by Presenter).
    *   **Data:** `InputData` (from Controller), `OutputData` (to Presenter).
*   **Entities (Enterprise Rules):**
    *   Core business objects (e.g., `GameRecord`) and high-level rules. Independent of all other layers.

### 2. Control Flow (The "Engine")
1.  **View** captures event -> calls **Controller**.
2.  **Controller** creates `InputData` -> calls **Use Case** (via `InputBoundary`).
3.  **Use Case** interacts with **Data Access** (via Interface) to fetch/save **Entities**.
4.  **Use Case** processes logic -> creates `OutputData` -> calls **Presenter** (via `OutputBoundary`).
5.  **Presenter** formats data -> updates **ViewModel**.
6.  **View** observes `ViewModel` update -> refreshes UI.

### Workflow Example: Record Game

This workflow demonstrates how the **Clean Architecture** layers interact when a user finishes a game.

1.  **UI Event (Frameworks & Drivers):**
    *   User clicks "Done" in `Game.java`.
    *   The listener gathers state (drawing image, prompt, difficulty, time taken) and calls `GameController.executeGameResult()`.

2.  **Input Processing (Interface Adapters):**
    *   `GameController` saves the drawing to disk (getting an `imagePath`).
    *   It packages all data into a `RecordGameInputData` object.
    *   It calls `RecordGameInputBoundary.execute(inputData)` (the Use Case).

3.  **Business Logic (Use Cases):**
    *   `RecordGameUseCase` creates a `GameRecord` entity from the input data.
    *   It calls `GameDataAccessInterface.save(gameRecord)` to persist the data.
    *   It creates `RecordGameOutputData` and calls `RecordGameOutputBoundary.present(outputData)`.

4.  **Data Persistence (Interface Adapters):**
    *   `GameDataBase` (implementing `GameDataAccessInterface`) adds the record to its list and appends it to `games.csv`.

5.  **Output Processing (Interface Adapters):**
    *   `GamePresenter` receives the output data.
    *   It updates the `ViewManagerModel` state to switch the active view to `"GameResult"`.

6.  **UI Update (Frameworks & Drivers):**
    *   `Application.java` (listening to `ViewManagerModel`) detects the change and switches the visible panel to `GameResult`.