# NOVA Forge

AI Coding Agent on Android - Describe it. Build it. Run it.

## Overview
NOVA Forge is a complete Android application that helps users turn app ideas into working projects. Built with Kotlin and Jetpack Compose, featuring:

- **Dark Modern UI** - Material 3 design with custom dark theme
- **Arabic + English** - Full RTL support
- **Project Manager** - Create, manage, and organize projects
- **File Explorer** - Browse and edit project files
- **AI Integration** - Abstract AI provider layer (OpenAI, Gemini, Anthropic, Local)
- **Build System** - Gradle-based build automation
- **Git Integration** - Full Git workflow support
- **Security** - Encrypted storage, safe command validation
- **Error Recovery** - Automatic error detection and fixing
- **Tool System** - Safe execution layer with permission checks

## Features
✅ Kotlin + Jetpack Compose
✅ Material 3 Dark Theme
✅ Project Management
✅ File Manager
✅ AI Provider Abstraction
✅ Build Orchestration
✅ Git/GitHub Integration
✅ Security & Validation
✅ Error Recovery
✅ Terminal Support Ready

## Tech Stack
- **Language**: Kotlin 1.9.24
- **UI Framework**: Jetpack Compose
- **Design System**: Material 3
- **Build System**: Gradle 8.7
- **Android SDK**: compileSdk 35, targetSdk 35
- **Min SDK**: 26
- **JDK**: 17

## Build Instructions

### Prerequisites
- Android Studio (latest)
- Java JDK 17+
- Android SDK 35

### Building Debug APK
```bash
cd NOVA-Forge
./gradlew assembleDebug
```

### Building Release APK
```bash
./gradlew assembleRelease
```

### Running on Device/Emulator
```bash
./gradlew installDebug
```

## Installation
1. Clone the repository
2. Open in Android Studio
3. Wait for Gradle sync
4. Click Run or press Shift+F10

## Project Structure
```
app/src/main/
├── java/com/novaforge/app/
│   ├── MainActivity.kt
│   ├── NovaForgeApplication.kt
│   ├── NovaForgeApp.kt
│   ├── agent/
│   │   ├── Tool.kt
│   │   ├── ToolExecutor.kt
│   │   ├── BuildRunner.kt
│   │   ├── GitManager.kt
│   │   ├── ErrorRecovery.kt
│   │   └── SecurityManager.kt
│   ├── ai/
│   │   ├── AIProvider.kt
│   │   └── AISettings.kt
│   ├── data/
│   │   └── ProjectRepository.kt
│   ├── model/
│   │   └── ProjectSpec.kt
│   ├── security/
│   │   └── SecureSettings.kt
│   ├── util/
│   │   └── FileManager.kt
│   └── ui/
│       ├── screens/
│       │   ├── HomeScreen.kt
│       │   ├── ProjectScreen.kt
│       │   ├── SettingsScreen.kt
│       │   └── ProjectDetailScreen.kt
│       └── theme/
│           ├── Theme.kt
│           └── Type.kt
└── res/
    ├── values/
    │   ├── strings.xml
    │   ├── colors.xml
    │   └── themes.xml
    └── xml/
        ├── backup_rules.xml
        └── data_extraction_rules.xml
```

## Usage
1. **Home Screen** - Enter your app idea
2. **Build** - Click Build button to start project creation
3. **Projects** - Manage your created projects
4. **Settings** - Configure AI providers and preferences

## API Reference

### AI Providers
- OpenAI (GPT-4o-mini)
- Google Gemini (1.5-flash)
- Anthropic Claude (3.5-sonnet)
- Local Model Support

### Tools Available
- File operations (create, read, edit, delete)
- Build automation (Gradle)
- Git operations (status, commit, push)
- Testing framework
- Command execution (with safety checks)

## Security
- API keys stored in encrypted SharedPreferences
- Safe command validation
- Permission-based tool execution
- User confirmation for dangerous actions
- No secrets in source code

## Contributing
Feel free to fork and submit pull requests.

## License
MIT License

## Author
Lamrabti Said (@lamrabti-3)

## Support
For issues and questions, please open an issue on GitHub.