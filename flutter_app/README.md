# DiviSync - Professional Currency Conversion App

A modern, professional currency conversion application built with Flutter that works seamlessly across Android, iOS, and Web platforms.

## 🌟 Features

### Core Functionality
- **Real-time Currency Conversion**: Get live exchange rates from multiple reliable sources
- **150+ Supported Currencies**: Support for all major world currencies with proper formatting
- **Offline Mode**: Continue using the app with cached rates when internet is unavailable
- **Secure Local Storage**: All data encrypted and stored locally for privacy
- **Professional UI/UX**: Modern Material Design 3 interface with smooth animations

### Advanced Features
- **Multiple Data Sources**: Primary (Fixer.io) and backup (ExchangeRate.host) APIs
- **Smart Caching**: Intelligent cache management with TTL (Time To Live)
- **Background Updates**: Automatic rate updates every hour
- **Cross-Platform**: Native performance on Android, iOS, and Web
- **Responsive Design**: Optimized for all screen sizes and orientations

### Technical Features
- **MVVM Architecture**: Clean, maintainable code structure
- **BLoC State Management**: Reactive state management with Flutter BLoC
- **Dependency Injection**: Modular and testable code with GetIt
- **Error Handling**: Comprehensive error handling and user feedback
- **Internationalization**: Ready for multiple languages
- **Accessibility**: Full accessibility support

## 🚀 Getting Started

### Prerequisites
- Flutter SDK (3.10.0 or higher)
- Dart SDK (3.0.0 or higher)
- Android Studio / Xcode (for mobile development)
- VS Code (recommended for development)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/divisync/app.git
   cd divisync
   ```

2. **Install dependencies**
   ```bash
   flutter pub get
   ```

3. **Configure API Keys**
   - Copy `lib/core/config/app_config.dart.example` to `lib/core/config/app_config.dart`
   - Add your Fixer.io API key
   - Configure other settings as needed

4. **Run the app**
   ```bash
   # For Android
   flutter run -d android
   
   # For iOS
   flutter run -d ios
   
   # For Web
   flutter run -d web
   ```

### Build for Production

#### Android
```bash
flutter build apk --release
# or
flutter build appbundle --release
```

#### iOS
```bash
flutter build ios --release
```

#### Web
```bash
flutter build web --release
```

## 📱 Platform Support

### Android
- **Minimum SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: ARM64, ARMv7, x86_64

### iOS
- **Minimum Version**: iOS 11.0
- **Target Version**: iOS 17.0
- **Architecture**: ARM64

### Web
- **Browsers**: Chrome, Firefox, Safari, Edge
- **Features**: PWA support, offline functionality
- **Performance**: Optimized for modern browsers

## 🏗️ Architecture

### Project Structure
```
lib/
├── core/                    # Core functionality
│   ├── config/             # App configuration
│   ├── di/                 # Dependency injection
│   ├── error/              # Error handling
│   ├── theme/              # App theming
│   └── utils/              # Utilities
├── features/               # Feature modules
│   ├── currency/           # Currency conversion
│   ├── history/            # Conversion history
│   └── settings/           # App settings
└── main.dart               # App entry point
```

### State Management
- **BLoC Pattern**: Business Logic Component for state management
- **Events**: User interactions and system events
- **States**: UI state representation
- **Use Cases**: Business logic implementation

### Data Flow
1. **UI** → **BLoC** → **Use Case** → **Repository** → **Data Source**
2. **Data Source** → **Repository** → **Use Case** → **BLoC** → **UI**

## 🔧 Configuration

### API Configuration
```dart
// lib/core/config/app_config.dart
class AppConfig {
  static const String fixerApiKey = 'YOUR_FIXER_API_KEY';
  static const String fixerApiUrl = 'https://data.fixer.io/api';
  static const String exchangeHostApiUrl = 'https://api.exchangerate.host';
}
```

### Cache Configuration
```dart
static const Duration cacheTtl = Duration(hours: 1);
static const Duration updateInterval = Duration(hours: 1);
```

## 🧪 Testing

### Run Tests
```bash
# Unit tests
flutter test

# Integration tests
flutter test integration_test/

# Coverage report
flutter test --coverage
```

### Test Structure
- **Unit Tests**: Individual component testing
- **Widget Tests**: UI component testing
- **Integration Tests**: End-to-end testing
- **BLoC Tests**: State management testing

## 📦 Dependencies

### Core Dependencies
- **flutter_bloc**: State management
- **get_it**: Dependency injection
- **dio**: HTTP client
- **hive**: Local storage
- **shared_preferences**: Settings storage
- **connectivity_plus**: Network status
- **workmanager**: Background tasks

### UI Dependencies
- **material_design_icons_flutter**: Icons
- **flutter_svg**: SVG support
- **lottie**: Animations
- **shimmer**: Loading effects
- **fl_chart**: Charts and graphs

## 🔒 Security

### Data Protection
- **Local Encryption**: AES-256 encryption for sensitive data
- **No Personal Data**: No collection of personal information
- **Secure Storage**: Encrypted local storage with Hive
- **API Security**: HTTPS-only API communication

### Privacy
- **Offline First**: Data stays on device
- **No Tracking**: No analytics or user tracking
- **Transparent**: Open source and auditable

## 🌐 Internationalization

### Supported Languages
- English (default)
- Spanish
- French
- German
- Italian
- Portuguese
- Chinese (Simplified)
- Japanese
- Korean

### Adding New Languages
1. Add language files to `lib/l10n/`
2. Update `pubspec.yaml` with new locale
3. Run `flutter gen-l10n`

## 🚀 Deployment

### Android Play Store
1. Build release APK/AAB
2. Upload to Google Play Console
3. Configure store listing
4. Submit for review

### iOS App Store
1. Build release IPA
2. Upload to App Store Connect
3. Configure app information
4. Submit for review

### Web Deployment
1. Build web assets
2. Deploy to hosting service
3. Configure PWA settings
4. Set up custom domain

## 🤝 Contributing

### Development Setup
1. Fork the repository
2. Create feature branch
3. Make changes
4. Add tests
5. Submit pull request

### Code Style
- Follow Dart/Flutter conventions
- Use meaningful variable names
- Add comprehensive comments
- Write unit tests
- Update documentation

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Fixer.io** for reliable exchange rate data
- **ExchangeRate.host** for backup API
- **Flutter Team** for the amazing framework
- **Material Design** for design guidelines
- **Open Source Community** for inspiration and tools

## 📞 Support

### Documentation
- [API Documentation](docs/api.md)
- [Architecture Guide](docs/architecture.md)
- [Deployment Guide](docs/deployment.md)

### Contact
- **Email**: support@divisync.app
- **GitHub**: [Issues](https://github.com/divisync/app/issues)
- **Website**: [divisync.app](https://divisync.app)

---

**DiviSync** - Professional Currency Conversion Made Simple 🚀
