# Museum Memories
**A Personal Artifact Collection System**


## 1. Overview
- Museum Memories is **a personalized artifact collection management system** designed to help users **record their favourite artifacts**, including their names, the museums they belong to, descriptions and ratings. Users can **search for all 5-star artifacts in their collection and all artifacts from a specific museum**.

- This project is designed for people who enjoy visiting museums, exploring cultural heritage, and recording their travel experiences.

- For me, preserving the artifacts and knowledge gained from each museum visit is important, as I do not want these meaningful experiences to be gradually forgotten. Therefore, I aim to create a personal artifact collection system that allows me to organize, preserve, and revisit my discoveries. This project provides an effective and intuitive way to transform scattered museum visits into a structured personal artifact collection archive.


## 2. User stories
- As a user, I want to be able to add an artifact to my artifact collection and specify its name, the museum it belongs to, description, and rating.
- As a user, I want to be able to view the list of all artifacts in  my artifact collection.
- As a user, I want to find all 5-star artifacts in my artifact collection.
- As a user, I want to be able to find all artifacts from a specific museum in my artifact collection.
- As a user, I want to be able to save my artifact collection to file (if I so choose) so that I can preserve my recorded items.
- As a user, I want to be able to load my artifact collection from file when I start the application (if I so choose).


## 3. Instructions for End User
- You can view the panel that displays the artifacts that have already been added to the artifact collection by clicking the "🗂️ Show All" button.
- You can generate the first required action related to the user story "add an artifact to the artifact collection" by clicking the "🏺 Add Artifact" button and entering the artifact's name, the museum it belongs to, description, and rating.
- You can generate the second required action related to the user story "find all 5-star artifacts in my artifact collection" by clicking the "⭐️ Five Stars" button to display all five-star artifacts.
- You can locate my visual component in the background of the main application window.
- You can save the state of my application by clicking the "💾 Save" button.
- You can reload the state of my application by clicking the "📂 Load" button.

*Image Attribution:*
*The background image and artifact icon used in this application were generated using OpenAI's ChatGPT.*

## Phase 4: Task 2
The following is a representative sample of events logged during two runs of the application:

### First run
```text
Sun Aug 09 15:10:58 PDT 2026
Artifact added to Museum Memories: Houmuwu Ding
Sun Aug 09 15:11:21 PDT 2026
Artifact added to Museum Memories: Zeng Houyi Bianzhong
Sun Aug 09 15:11:43 PDT 2026
Artifact added to Museum Memories: Gilded Silver Pot with Dancing Horse
Sun Aug 09 15:11:45 PDT 2026
Viewed all five-star artifacts.
```
### Second run
```text
Sun Aug 09 15:12:16 PDT 2026
Artifact collection loaded.
Sun Aug 09 15:12:40 PDT 2026
Artifact added to Museum Memories: Gold-inlaid Bronze Rhinoceros Zun
Sun Aug 09 15:12:49 PDT 2026
Viewed all artifacts.
Sun Aug 09 15:12:50 PDT 2026
Viewed all five-star artifacts.
```

## Phase 4: Task 3
If I had more time to refactor my application, I would separate some of the responsibilities currently handled by the MuseumMemoriesGUI class. This class is responsible for constructing the graphical interface, processing user input, displaying artifacts, handling button actions, and coordinating the saving and loading of the artifact collection. Since this single class has multiple responsibilities, it could be refactored into several smaller classes.

I would extract the artifact display area and button panel into separate GUI component classes, while leaving the main MuseumMemoriesGUI class responsible for coordinating the application. This would improve cohesion and make each class easier to understand and modify. However, introducing additional classes could make the structure of this relatively small application more complex.