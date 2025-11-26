What Your Presentation Plan Must Include
Your group must prepare a draft outline of your presentation that includes the following:
Slide-by-Slide Outline 
Create a preliminary outline of your presentation (E.g., using a spreadsheet, word doc, a copy of the pptx presentation template from Final Project Presentation Expectations).

For each slide:
provide at least one bullet point summarizing the key idea or content of that slide.
indicate who will be responsible for presenting that slide.
This does not need to be fully polished, but it should clearly indicate the structure and flow of your presentation.
Reminder that every team member must contribute to the presentation of your project — presenting at least their use case.
Please refer to the Final Project Presentation Expectations page and rubric linked from it to ensure that your team's presentation plan aligns with the rubric elements — and the levels your team is aiming to achieve within each element.
Demo Plan
Provide a brief description of how you will demo your project, including:

What features you will show.
The order in which you will show them.
Who will be responsible for running the demo (narration and/or interaction).
Will your team pre-record parts of the demo or present it completely live?


# Slides 

## User Stories (5 slides) 
1. Main gameplay 
* Draw a prompt assigned randomly (e.g. pen, bug)
* AI will guess the prompt 
* You win if the AI guesses within the time limit, lose if it doesn't 
* After finishing the game the user sees an endscreen summarizing the game just played

2. Gallery 
* Can scroll through past games in the gallery
* Clicking into one will show more info (win/loss, prompt given, time taken)

3. Settings 
* User can choose what time limit they want to set as well as easy/medium/hard)

4. Advanced Drawing Features 
* User can use the undo function and eraser function

5. Saving picture locally 
* After clicking on a picture in the gallery, they can choose to save it locally to their computer (it's normally saved in the application's `resources/` folder)

## API usage (1 slide)

* We are using the HuggingFace API, sending a POST request of the picture that the user draws and sending it to a model hosted there
* We return a top-5 dictionary of the predictions, alongside the confidence
* The top confidence is used as the "AI's guess"
* Trained manually using ResNet-18 architecture on Quick, Draw! dataset

![alt text](image.png)

We require preprocessing of the image. 
* resize to 28x28
* make black and white 
* normalize to [0, 1]

## Data Persistence 

* **What data is persistent:** We persist the user's game history. This includes the drawing, the prompt word, whether the AI guessed it correctly, and a timestamp.
* **How it's stored:** This data is saved in a `games.csv` file located in the `src/main/resources` directory.
* **Implementation:** The `GameDataBase` class handles all read and write operations to this CSV file, ensuring that players' game records are available across different sessions.

## Use case walkthrough 
• Briefly state your user story and which associated use case you will focus on
• Show the before and after views for when the use case executes
• Show a UML class diagram for the use case; it should be clear from
the diagram that your code adheres to CA!
• Show the code for your Use Case Interactor class.
• Discuss the flow of control when your use case executes.
• [this should be rehearsed so that it is around 1 minute per member]

Zac - main gameplay
![alt text](image-1.png)

Ziyi - advanced drawing features and endscreen 

Munkh - settings
![alt text](image-4.png)

David - Gallery 
![alt text](image-2.png)

Laney - Gallery window
![alt text](image-3.png)

## Design principles 
* **SOLID Principles:**
    * **Single Responsibility Principle (SRP):** Our classes are designed to have one specific role. For example, the `GameSession` entity only manages the state of an active game, the `HuggingFaceAPICaller` is solely responsible for interacting with the external AI model, and the `GameDataBase` class is dedicated to persisting game records. Use case interactors like `DeleteGameUseCase` encapsulate only the business logic for their specific function.
    * **Dependency Inversion Principle (DIP):** We depend on abstractions, not concretions. Use cases interact with data through interfaces (e.g., a data access interface) rather than directly with the `GameDataBase` class. This decouples the business logic from the data storage mechanism, allowing us to switch from a CSV file to a different database system in the future without altering the use cases.
* **Clean Architecture:**
    * The project is structured into the four main layers of Clean Architecture: Entities, Use Cases, Interface Adapters, and Frameworks & Drivers.
    * This is reflected in our package structure (`entities`, `usecases`, `interface_adapters`, `gui`, `database`, `api`), which enforces the separation of concerns and the inward-pointing dependency rule.
* **Design Patterns:**
    * **Observer Pattern:** We use the Observer pattern to keep the UI synchronized with the application's state. `ViewModel` classes hold the state and notify the `View` (the observer) when data changes. For instance, when a game ends, the relevant ViewModel is updated by a Presenter, which then triggers the UI to refresh, displaying the final score or navigating to a different screen. This decouples the UI from the business logic.

## Functionality Demonstration

We will just show the runthrough of the application here in front of the professor / TA, running on one of our computers, completely live. 

## Code organization 
* **Package by Layer:** We've organized our code into packages that correspond to the layers of Clean Architecture. The root package is `com.sketchandguess`.
* **Key Packages:**
    * `entities`: Contains the core business objects like `GameSession`.
    * `usecases`: Holds application-specific business rules, such as `EditSettingsUseCase`.
    * `interface_adapters`: Acts as the translator between the business logic and the outer layers (UI, database).
    * `gui`: Includes all Swing components for the user interface, like `MainMenu` and `Game`.
    * `database`: Manages data persistence, specifically `GameDataBase`.
    * `api`: Handles communication with the Hugging Face API.
