![Screenshot](images/screenshot.png)
![Screenshot](images/screenshot1.png)
![Screenshot](images/screenshot2.png)
![Screenshot](images/screenshot3.png)

# IdeaFlow Swing (No External Libraries)

Modern-ish, soft UI built with **pure Swing** (no downloads).  
Includes: Top bar with search, sidebar navigation, inbox cards, basic "Kanban" placeholder, idea details panel, and a tiny in-memory store.

## Run (no Gradle/Maven)
### Windows
- Double click `run.bat`

### Linux/macOS
- `bash run.sh`

## Project structure
- `src/com/yahia/ideaflow/app`      -> app bootstrap + main frame
- `src/com/yahia/ideaflow/theme`    -> theme + colors + fonts
- `src/com/yahia/ideaflow/model`    -> Idea + store
- `src/com/yahia/ideaflow/ui`       -> reusable components (rounded panels/buttons, chips, etc.)
- `src/com/yahia/ideaflow/screens`  -> screens (Inbox / Board / Details)

## Notes
- Uses Nimbus Look&Feel (built-in) + UIManager tweaks.
- Rounded corners + soft shadows are custom-painted with Graphics2D.
- Hover animations are done with Swing `Timer` (no heavy effects).

## Page transitions
Uses `AnimatedNavigator` (fade + slide) implemented with snapshots + Swing Timer.

## Style upgrades (no downloads)
- Floating top bar card
- Subtle background gradient
- Thin modern scrollbars
- Chips auto-size

## Notes (requested)
- Dark mode disabled (light only).
- Transitions are slower, smoother, and stronger (fade + slide + subtle scale).
