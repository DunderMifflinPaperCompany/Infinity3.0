# Dunder Mifflin Infinity 2.0 🚀

Welcome to the revolutionary Dunder Mifflin Infinity 2.0 website - a satirical corporate upgrade that parodies "The Office" and promises to fix all the mistakes of version 1.0!

## Features ✨

### 🎉 Major Upgrades from V1.0:
- **400% More Efficient** - Revolutionary performance improvements
- **73% Fewer Bugs** - Nearly bug-free experience (we hope)
- **Modern Responsive Design** - Works on desktop, mobile, and even BlackBerry!
- **AI-Powered Paper Recommendations** - Machine learning for paper selection
- **Blockchain Supply Chain** - Track your paper from tree to desk
- **Cloud Integration** - Access orders from anywhere (actual clouds not supported)

### 🏢 Office-Themed Content:
- Employee spotlight featuring Michael Scott, Jim Halpert, Dwight K. Schrute, and Pam Beesly
- Authentic Office quotes and references
- Corporate news and updates
- Satirical "2.0 upgrade" features that parody corporate software

### 🎨 Design Improvements:
- Modern gradient backgrounds and animations
- Glowing infinity symbol with animations
- Hover effects and transitions
- Professional corporate styling
- Mobile-first responsive design
- Clean, modern layout improving significantly on V1

## Technology Stack 🛠️

- **Groovy & Grails 8** - JVM web application framework
- **Gradle Wrapper** - Reproducible builds without a global Gradle or Grails installation
- **GSP** - Server-rendered Groovy Server Pages with HTML escaping
- **HTML5 & CSS3** - Modern responsive frontend
- **Responsive Design** - Mobile and desktop optimized

## How to Run 🚀

### Prerequisites
- JDK 21 (set `JAVA_HOME` to your JDK installation)
- Internet access for the first build to download Gradle and Maven dependencies

### Installation & Running
```bash
# Clone the repository
git clone https://github.com/DunderMifflinPaperCompany/Infinity3.0.git
cd Infinity3.0

# Run tests and build an executable JAR
./gradlew check bootJar

# Run the server
./gradlew bootRun
```

The website will be available at `http://localhost:3000`

On Windows, use `gradlew.bat` instead of `./gradlew`.

To run the packaged application (including its views and stylesheet):

```bash
java -jar build/libs/infinity-0.1.0.jar
```

The server binds to `0.0.0.0:3000` by default. Override the port with
`SERVER_PORT=8080 ./gradlew bootRun` or `java -jar build/libs/infinity-0.1.0.jar --server.port=8080`.
Set `CHAT_ENABLED=false` to hide the chat widget and return HTTP 503 from the chat APIs.

### Chat API

| Method | Path | Behavior |
|---|---|---|
| GET | `/api/offices` | Returns the five offices and their salesperson IDs |
| POST | `/api/chat/start` | Accepts `{"office_id":"scranton","customer_name":null}` and returns a session UUID, status, message, and salesperson (or `null`) |
| POST | `/api/chat/end/{session_id}` | Ends a session and returns the JSON string `"Chat ended successfully"` |

Scranton, Stamford, and Nashua connect to an available representative. Utica and Buffalo
return a waiting status. Unknown offices and invalid session IDs return HTTP 400;
unknown sessions return HTTP 404. Chat start requires JSON (HTTP 415 otherwise).

Sessions remain in memory and are lost on restart, just as in the original app. Each
application instance has its own sessions. Structured `CHAT_EVENT` JSON is logged for
starts and ends. The UI's message-send control is still a local placeholder, not a
real-time messaging service or a working queue.

### Tests

`./gradlew check` runs Spock tests, including HTTP tests on random ports for rendered
content, static assets, all five office flows, session lifecycle, malformed requests,
and disabled chat. GitHub Actions runs the tests and builds the executable JAR.

## Project Structure 📁
```
├── grails-app/
│   ├── conf/application.yml
│   ├── controllers/com/dundermifflin/  # HTTP endpoints and URL mappings
│   ├── init/com/dundermifflin/        # Groovy application entry point
│   ├── services/com/dundermifflin/    # In-memory chat state and event logging
│   └── views/index.gsp               # Server-rendered homepage
├── src/main/groovy/com/dundermifflin/ # Employee, news, office, salesperson data
├── src/main/resources/static/css/style.css
├── src/test/groovy/com/dundermifflin/ # Spock regression tests
├── build.gradle                     # Grails dependencies and build
├── gradlew / gradlew.bat             # Gradle Wrapper entry points
└── README.md
```

## The Parody 😄

This website satirizes corporate "version upgrades" that promise to fix past mistakes but often just add complexity. Infinity 2.0 includes:

- Buzzword-heavy feature descriptions
- Overly optimistic statistics (400% more efficient!)
- Corporate speak and marketing language
- References to trendy tech (AI, Blockchain, Cloud)
- Classic Office character quotes and situations
- The promise of fixing all V1.0 problems (with new ones introduced)

## Screenshots 📸

The website features a modern, professional design that significantly improves on the original V1 mockup, with both desktop and mobile responsive layouts.

---

*"Limitless Paper in a Paperless World™"* - Dunder Mifflin Paper Company

**Infinity 2.0 - The Future is Now** ✨