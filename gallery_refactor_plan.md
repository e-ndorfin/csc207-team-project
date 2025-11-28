# Refactor Gallery to Follow Clean Architecture

## Current Violations in Gallery.java

The current `Gallery.java` violates Clean Architecture by:
- Directly instantiating `GameDataBase` (lines 35-36)
- Performing search logic in the view (line 65)
- Direct database manipulation in action listeners (lines 62-76)
- No separation between UI and business logic

## Implementation Steps

### 1. Create Use Cases Layer

Create in `src/main/java/com/sketchandguess/usecases/gallery/`:

**RetrieveGamesUseCase** - Loads all games from database
- `RetrieveGamesInputBoundary.java` - interface with `execute()` method
- `RetrieveGamesOutputBoundary.java` - interface with `present(RetrieveGamesOutputData)` method
- `RetrieveGamesInputData.java` - empty or with optional filters
- `RetrieveGamesOutputData.java` - contains `List<GameRecord> games`
- `RetrieveGamesUseCase.java` - implements InputBoundary, uses `GameDataAccessInterface`

**SearchGamesUseCase** - Searches games by query
- `SearchGamesInputBoundary.java` - interface with `execute(SearchGamesInputData)` method
- `SearchGamesOutputBoundary.java` - interface with `present(SearchGamesOutputData)` method
- `SearchGamesInputData.java` - contains `String query`
- `SearchGamesOutputData.java` - contains `List<GameRecord> games, String query`
- `SearchGamesUseCase.java` - implements InputBoundary, uses `GameDataAccessInterface.searchGames()`

### 2. Implement Interface Adapters

**GalleryState.java** - holds display data
```java
- List<GameRecord> gameRecords
- String searchQuery
- boolean isEmpty
+ getters and setters
```

**GalleryViewModel.java** - extends `ViewModel<GalleryState>`
```java
- static final VIEW_NAME = "Gallery"
- GalleryState state
- PropertyChangeSupport
+ firePropertyChange(), addPropertyChangeListener()
```

**GalleryPresenter.java** - implements both OutputBoundaries
```java
- GalleryViewModel viewModel
+ present(RetrieveGamesOutputData) - updates state with all games
+ present(SearchGamesOutputData) - updates state with filtered games
```

**GalleryController.java** - delegates to use cases
```java
- RetrieveGamesInputBoundary retrieveGamesUseCase
- SearchGamesInputBoundary searchGamesUseCase
- ViewManagerModel viewManagerModel
+ refreshGallery() - calls retrieveGamesUseCase
+ searchGames(String query) - calls searchGamesUseCase
+ clearSearch() - calls retrieveGamesUseCase
+ selectGameRecord(GameRecord) - delegates to existing galleryWindowController
+ goBackToMainMenu() - updates viewManagerModel
```

### 3. Refactor Gallery.java

Transform `Gallery.java` into a pure view:
- Remove `mainDataBase` and `currentDataBase` fields
- Remove direct database instantiation
- Add `GalleryController` and `GalleryViewModel` as dependencies
- Add `PropertyChangeListener` to listen for state changes
- Move action listener logic to call controller methods:
  - searchButton → `controller.searchGames(searchBarField.getText())`
  - clearButton → `controller.clearSearch()`
  - backButton → `controller.goBackToMainMenu()`
  - imageButton → `controller.selectGameRecord(record)`
- Replace `refresh()` method to call `controller.refreshGallery()`
- Update `updateGalleryView()` to read from `viewModel.getState().getGameRecords()`

### 4. Wire Up in Application.java

Update `Application.java`:
- Instantiate `GalleryViewModel` and `GalleryPresenter`
- Instantiate both use cases with `gameDataBase` and `galleryPresenter`
- Instantiate `GalleryController` with use cases and `viewManagerModel`
- Pass both controllers to `Gallery` constructor: `new Gallery(galleryController, galleryWindowController, galleryViewModel)`
- Add PropertyChangeListener to `GalleryViewModel` to trigger UI updates
- Call `galleryController.refreshGallery()` in `showGallery()` method

## Key Files to Modify

- Create: `src/main/java/com/sketchandguess/usecases/gallery/*` (10 new files)
- Modify: `src/main/java/com/sketchandguess/interface_adapters/gallery/*` (4 files)
- Modify: `src/main/java/com/sketchandguess/gui/Gallery.java`
- Modify: `src/main/java/com/sketchandguess/gui/Application.java`

## Architecture Flow

**Refresh Gallery:** Gallery → GalleryController.refreshGallery() → RetrieveGamesUseCase → GameDataBase → GalleryPresenter → GalleryViewModel → Gallery (updates UI)

**Search:** Gallery (search button) → GalleryController.searchGames(query) → SearchGamesUseCase → GameDataBase.searchGames() → GalleryPresenter → GalleryViewModel → Gallery (updates UI)

## Reference Files

- Current Gallery.java: `src/main/java/com/sketchandguess/gui/Gallery.java`
- Example use case pattern: `src/main/java/com/sketchandguess/usecases/RecordGameUseCase/`
- Example controller pattern: `src/main/java/com/sketchandguess/interface_adapters/game/GameController.java`
- Example presenter pattern: `src/main/java/com/sketchandguess/interface_adapters/game/GamePresenter.java`
- Database interface: `src/main/java/com/sketchandguess/usecases/GameDataAccessInterface.java`

