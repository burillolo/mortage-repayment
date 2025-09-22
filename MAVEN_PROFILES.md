# Maven Profiles Documentation

This project includes several Maven profiles to control when and how the Angular frontend is built during the Maven lifecycle.

## Available Profiles

### 1. **Default (No Profile)**
```bash
mvn clean install
```
- **Description**: Builds only the Spring Boot backend
- **Use case**: Backend development, CI/CD for backend-only changes
- **Build time**: Fast (~5-10 seconds)

### 2. **Frontend Profile** (`-Pfrontend`)
```bash
mvn clean install -Pfrontend
```
- **Description**: Builds backend + Angular frontend (development build)
- **Use case**: Full-stack development, testing integration
- **Build time**: Moderate (~15-30 seconds)
- **Phase**: `generate-resources`

### 3. **Production Profile** (`-Pproduction`)
```bash
mvn clean install -Pproduction
```
- **Description**: Builds backend + optimized Angular frontend for production
- **Features**:
  - Runs `npm ci` (clean install)
  - Angular production build with optimizations
  - Minification, tree-shaking, AOT compilation
- **Use case**: Production deployments, release builds
- **Build time**: Longer (~30-60 seconds)

### 4. **Frontend Development Profile** (`-Pfrontend-dev`)
```bash
mvn clean compile -Pfrontend-dev
```
- **Description**: Builds backend + Angular frontend in watch mode
- **Features**: Angular build with `--watch` flag
- **Use case**: Development with automatic frontend rebuilds
- **Build time**: Initial build + continuous watching

## Common Usage Patterns

### Backend Development Only
```bash
# Fast compilation for backend changes
mvn clean compile

# Run Spring Boot application (backend only)
mvn spring-boot:run
```

### Full-Stack Development
```bash
# Build everything for development
mvn clean install -Pfrontend

# Run Spring Boot with frontend included
mvn spring-boot:run -Pfrontend
```

### Production Deployment
```bash
# Create optimized production build
mvn clean package -Pproduction

# Run the JAR with embedded frontend
java -jar target/simulator-0.0.1-SNAPSHOT.jar
```

### Testing Builds
```bash
# Test backend only
mvn test

# Test with frontend build
mvn test -Pfrontend
```

## Profile Details

| Profile | Angular Build | npm Command | Optimization | Use Case |
|---------|---------------|-------------|--------------|----------|
| (none) | ❌ | - | - | Backend only |
| `frontend` | ✅ | `npm run build` | Development | Full-stack dev |
| `production` | ✅ | `npm ci && npm run build --configuration=production` | Production | Deployment |
| `frontend-dev` | ✅ | `npm run build --watch` | Development + Watch | Live development |

## Build Output

### Backend Only
- **Target**: `target/classes/` (Java compiled classes)
- **Size**: ~2-5 MB
- **Includes**: Spring Boot application, REST APIs

### With Frontend
- **Target**: `target/classes/static/` (Angular build output)
- **Size**: ~3-8 MB (dev) / ~2-4 MB (prod)
- **Includes**: Spring Boot + Angular SPA

## Troubleshooting

### Frontend Build Fails
```bash
# Check Node.js version
node --version  # Should be 16+

# Check npm installation
cd src/main/frontend/cuadro-web
npm install

# Manual frontend build
npm run build
```

### Profile Not Working
```bash
# Verify active profiles
mvn help:active-profiles -Pfrontend

# Check Maven version
mvn --version  # Should be 3.6+
```

### Build Performance
- **Backend only**: ~5-10 seconds
- **With frontend (dev)**: ~15-30 seconds  
- **With frontend (prod)**: ~30-60 seconds

## Integration with IDEs

### IntelliJ IDEA
1. Open Maven panel
2. Select desired profile in "Profiles" section
3. Run Maven goals with selected profile

### VS Code
```bash
# Use integrated terminal
mvn clean install -Pfrontend
```

### Eclipse
1. Right-click project → Properties
2. Maven → Select Active Maven Profiles
3. Enter profile name (e.g., `frontend`)

## Environment-Specific Builds

### Development
```bash
mvn clean compile -Pfrontend-dev
# Starts with watch mode for live reloading
```

### Testing
```bash
mvn clean test -Pfrontend
# Includes frontend in test environment
```

### Staging/Production
```bash
mvn clean package -Pproduction
# Optimized build for deployment
```

This profile system provides flexibility for different development workflows while maintaining build performance and separation of concerns.
